package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableButton;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableItem;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableModule;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MusePacketInstallModuleRequest;
import net.machinemuse.powersuits.network.packets.MusePacketSalvageModuleRequest;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
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
    public List<String> getToolTip(int x, int y) {
        if (targetItem.getSelectedItem() != null
                && targetModule.getSelectedModule() != null) {
            ItemStack stack = targetItem.getSelectedItem().getItem();
            IPowerModule module = targetModule.getSelectedModule().getModule();
            NonNullList<ItemStack> itemsToCheck = ModuleManager.INSTANCE.getInstallCost(module.getDataName());
            double yoffset;
            if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getDataName())) {
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
    public void draw() {
        if (targetItem.getSelectedItem() != null
                && targetModule.getSelectedModule() != null) {
            drawBackground();
            drawItems();
            drawButtons();
        }
    }

    private void drawBackground() {
        super.draw();
    }

    private void drawItems() {
        ItemStack stack = targetItem.getSelectedItem().getItem();
        IPowerModule module = targetModule.getSelectedModule().getModule();
        NonNullList<ItemStack> itemsToDraw = ModuleManager.INSTANCE.getInstallCost(module.getDataName());
        double yoffset;
        if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getDataName())) {
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

    private void drawButtons() {
        ItemStack stack = targetItem.getSelectedItem().getItem();
        IPowerModule module = targetModule.getSelectedModule().getModule();
        if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getDataName())) {
            int installedModulesOfType = ModuleManager.INSTANCE.getNumberInstalledModulesOfType(stack, module.getCategory());
            installButton.setEnabled(player.capabilities.isCreativeMode ||
                    (MuseItemUtils.hasInInventory(ModuleManager.INSTANCE.getInstallCost(module.getDataName()), player.inventory) &&
                    installedModulesOfType < MPSConfig.INSTANCE.getMaxModulesOfType(module.getCategory())));
            installButton.draw();
        } else {
            salvageButton.draw();
        }
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        ClickableItem selItem = targetItem.getSelectedItem();
        ClickableModule selModule = targetModule.getSelectedModule();
        if (selItem != null && selModule != null) {
            ItemStack stack = selItem.getItem();
            IPowerModule module = selModule.getModule();

            if (!ModuleManager.INSTANCE.itemHasModule(stack, module.getDataName())) {
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
        IPowerModule module = targetModule.getSelectedModule().getModule();
        MPSPackets.sendToServer(new MusePacketSalvageModuleRequest(
                player,
                targetItem.getSelectedItem().inventorySlot,
                module.getDataName()));
    }

    /**
     * Performs all the functions associated with the install button. This
     * requires communicating with the server.
     */
    private void doInstall() {
        IPowerModule module = targetModule.getSelectedModule().getModule();
        if (player.capabilities.isCreativeMode || MuseItemUtils.hasInInventory(ModuleManager.INSTANCE.getInstallCost(module.getDataName()), player.inventory)) {
            Musique.playClientSound(SoundDictionary.SOUND_EVENT_GUI_INSTALL, SoundCategory.BLOCKS, 1, null);
            // Now send request to server to make it legit
            MPSPackets.sendToServer(new MusePacketInstallModuleRequest(
                    player,
                    targetItem.getSelectedItem().inventorySlot,
                    module.getDataName()));
        }
    }
}