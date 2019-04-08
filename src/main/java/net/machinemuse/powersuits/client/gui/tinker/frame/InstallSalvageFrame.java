package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.client.gui.clickable.ClickableButton;
import net.machinemuse.numina.client.gui.clickable.ClickableItem;
import net.machinemuse.numina.client.gui.clickable.ClickableModule;
import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MusePacketSalvageModuleRequest;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class InstallSalvageFrame extends ScrollableFrame {
    protected ItemSelectionFrame targetItem;
    protected ModuleSelectionFrame targetModule;
    protected ClickableButton installButton;
    protected ClickableButton salvageButton;
    protected EntityPlayerSP player;

    public InstallSalvageFrame(EntityPlayerSP player, MusePoint2D topleft,
                               MusePoint2D bottomright,
                               Colour borderColour, Colour insideColour,
                               ItemSelectionFrame targetItem, ModuleSelectionFrame targetModule) {
        super(topleft, bottomright, borderColour, insideColour);
        this.player = player;
        this.targetItem = targetItem;
        this.targetModule = targetModule;
        double sizex = border.right() - border.left();
        double sizey = border.bottom() - border.top();

        this.installButton = new ClickableButton(I18n.format("gui.powersuits.install"), new MusePoint2D(
                border.right() - sizex / 2.0, border.bottom() - sizey
                / 4.0),
                true);
        this.salvageButton = new ClickableButton(I18n.format("gui.powersuits.salvage"), new MusePoint2D(
                border.left() + sizex / 2.0, border.top() + sizey / 4.0),
                true);

    }

    @Override
    public void update(double mousex, double mousey) {
//        if (targetItem.getSelectedItem() != null && targetModule.getSelectedModule() != null)
//            System.out.println("update method would run here");


        // TODO: update the install/uninstall button when a module is installed
    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        if (targetItem.getSelectedItem() != null
                && targetModule.getSelectedModule() != null) {
            ItemStack stack = targetItem.getSelectedItem().getItem();
            ItemStack module = targetModule.getSelectedModule().getModule();
            NonNullList<ItemStack> itemsToCheck = NonNullList.create(); // ModuleManager.INSTANCE.getInstallCost(module.getDataName());
            double yoffset;
            if (!ModuleManager.INSTANCE.itemHasModule(stack, module)) {
                yoffset = border.top() + 4;
            } else {
                yoffset = border.bottom() - 20;
            }
            if (yoffset + 16 > y && yoffset < y) {
                double xoffset = -8.0 * itemsToCheck.size()
                        + (border.left() + border.right()) / 2;
                if (xoffset + 16 * itemsToCheck.size() > x && xoffset < x) {
                    int index = (int) (x - xoffset) / 16;
                    return itemsToCheck.get(index).getTooltip(player, ITooltipFlag.TooltipFlags.NORMAL);
                }
            }
        }
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (targetItem.getSelectedItem() != null
                && targetModule.getSelectedModule() != null) {
            drawBackground(mouseX, mouseY, partialTicks);
            drawItems(mouseX, mouseY, partialTicks);
            drawButtons(mouseX, mouseY, partialTicks);
        }
    }

    private void drawBackground(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
    }

    private void drawItems(int mouseX, int mouseY, float partialTicks) {
        ItemStack stack = targetItem.getSelectedItem().getItem();
        ItemStack module = targetModule.getSelectedModule().getModule();
        NonNullList<ItemStack> itemsToDraw = NonNullList.create();//ModuleManager.INSTANCE.getInstallCost(module.getDataName()); // FIXME!!
        double yoffset;
        if (!ModuleManager.INSTANCE.itemHasModule(stack, module)) {
            yoffset = border.top() + 4;
        } else {
            yoffset = border.bottom() - 20;
        }
        double xoffset = -8.0 * itemsToDraw.size()
                + (border.left() + border.right()) / 2;
        int i = 0;
        for (ItemStack costItem : itemsToDraw) {
            MuseRenderer.drawItemAt(
                    16 * i++ + xoffset,
                    yoffset,
                    costItem);
        }
    }

    private void drawButtons(int mouseX, int mouseY, float partialTicks) {
        ItemStack stack = targetItem.getSelectedItem().getItem();
        ItemStack module = targetModule.getSelectedModule().getModule();
        if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getItem().getRegistryName())) {
            int installedModulesOfType = ModuleManager.INSTANCE.getNumberInstalledModulesOfType(stack, ((IPowerModule)module.getItem()).getCategory());
            installButton.setEnabled(true); // fixme!!!!
//                    player.abilities.isCreativeMode ||
//                    (MuseItemUtils.hasInInventory(ModuleManager.INSTANCE.getInstallCost(module.getDataName()), player.inventory)
//                            && installedModulesOfType < MPSConfig.INSTANCE.getMaxModulesOfType(module.getCategory())));
            installButton.render(mouseX, mouseY, partialTicks);
        } else {
            salvageButton.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        ClickableItem selItem = targetItem.getSelectedItem();
        ClickableModule selModule = targetModule.getSelectedModule();
        if (selItem != null && selModule != null) {
            ItemStack stack = selItem.getItem();
            ItemStack module = selModule.getModule();

            if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getItem().getRegistryName())) {
                if (installButton.hitBox(x, y)) {
                    doInstall();
                }
            } else {
                if (salvageButton.hitBox(x, y)) {
                    doSalvage();
                }
            }
        }
    }

    private void doSalvage() {
        ItemStack module = targetModule.getSelectedModule().getModule();

        if (!module.isEmpty())
            MPSPackets.sendToServer(new MusePacketSalvageModuleRequest(
                    player.getEntityId(),
                    targetItem.getSelectedItem().inventorySlot,
                    module.getItem().getRegistryName().toString()));
    }

    /**
     * Performs all the functions associated with the install button. This
     * requires communicating with the server.
     */
    private void doInstall() { // FIXME: no more install costs
        ItemStack module = targetModule.getSelectedModule().getModule();
//        if (!module.isEmpty())
//        if (player.abilities.isCreativeMode || MuseItemUtils.hasInInventory(ModuleManager.INSTANCE.getInstallCost(module.getDataName()), player.inventory)) {
//            Musique.playClientSound(SoundDictionary.SOUND_EVENT_GUI_INSTALL, 1);
//            // Now send request to server to make it legit
//
//
//            MPSPackets.sendToServer(new MusePacketInstallModuleRequest(
//                    player.getEntityId(),
//                    targetItem.getSelectedItem().inventorySlot,
//                    module.getItem().getRegistryName().toString()));
//        }
    }
}