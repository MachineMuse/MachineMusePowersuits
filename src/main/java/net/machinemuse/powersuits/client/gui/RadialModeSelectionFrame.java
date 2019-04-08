package net.machinemuse.powersuits.client.gui;

import net.machinemuse.numina.capabilities.inventory.CapabilityModeChangingModularItem;
import net.machinemuse.numina.capabilities.inventory.IModeChangingItem;
import net.machinemuse.numina.client.gui.clickable.ClickableModule;
import net.machinemuse.numina.client.gui.frame.IGuiFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.math.geometry.SpiralPointToPoint2D;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.numina.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.util.LazyOptional.empty;

public class RadialModeSelectionFrame implements IGuiFrame {
    protected final long spawnTime;
    protected List<ClickableModule> modeButtons = new ArrayList<>();
    protected int selectedModuleOriginal = -1;
    protected int selectedModuleNew = -1;


    protected EntityPlayer player;
    protected MusePoint2D center;
    protected double radius;
    protected ItemStack stack;

    public RadialModeSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright, EntityPlayer player) {
        spawnTime = System.currentTimeMillis();
        this.player = player;
        this.center = bottomright.plus(topleft).times(0.5);
        this.radius = Math.min(center.minus(topleft).getX(), center.minus(topleft).getY());
        this.stack = player.inventory.getCurrentItem();
        loadItems();
        //Determine which mode is currently active

        // FIXME--- access mode changing item Capability
        if (!stack.isEmpty()) {
            LazyOptional<IModeChangingItem> modeChangingCapability = getModeChangingCapability();
            if (modeChangingCapability.isPresent()) {
                if (modeButtons != null) {
                    int i = 0;
                    for (ClickableModule mode : modeButtons) {
                        ItemStack activeModule = stack.getCapability(CapabilityModeChangingModularItem.MODE_CHANGING_MODULAR_ITEM_HANDLER_CAPABILITY).map(m ->m.getActiveModule()).orElse(ItemStack.EMPTY);

                        if (!activeModule.isEmpty() && activeModule.getItem().getRegistryName().equals(mode.getModule().getItem().getRegistryName())) {
                            selectedModuleOriginal = i;
                            break;
                        }
                        i++;
                    }
                }
            }
        }
    }

    public RadialModeSelectionFrame() {
        spawnTime = System.currentTimeMillis();
    }

    LazyOptional<IModeChangingItem> getModeChangingCapability() {
        if(!stack.isEmpty())
            return stack.getCapability(CapabilityModeChangingModularItem.MODE_CHANGING_MODULAR_ITEM_HANDLER_CAPABILITY);
        return empty();
    }

    private boolean alreadyAdded(ItemStack module) {
        for (ClickableModule clickie : modeButtons) {
            if (clickie.getModule().getItem().getRegistryName().equals(module.getItem().getRegistryName())) {
                return true;
            }
        }
        return false;
    }

    private void loadItems() {
        if (player != null) {
            NonNullList<ItemStack> modules = NonNullList.create();
            for (ItemStack module : ModuleManager.INSTANCE.getInstalledModulesOfType(stack, IRightClickModule.class)) {
                if (ModuleManager.INSTANCE.isValidForItem(stack, module))
                    modules.add(module);
            }

            int modeNum = 0;
            for (ItemStack module : modules) {
                if (!alreadyAdded(module)) {
                    ClickableModule clickie = new ClickableModule(module, new SpiralPointToPoint2D(center, radius, (3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / modules.size()), 250));
                    modeButtons.add(clickie);
                    modeNum++;
                }
            }
        }
    }

    private void selectModule(double x, double y) {
        if (modeButtons != null) {
            int i = 0;
            for (ClickableModule module : modeButtons) {
                if (module.hitBox(x, y)) {
                    selectedModuleNew = i;
                    break;
                }
                i++;
            }
        }
    }

    public ClickableModule getSelectedModule() {
        if (modeButtons.size() > selectedModuleNew && selectedModuleNew != -1) {
            return modeButtons.get(selectedModuleNew);
        } else {
            return null;
        }
    }

    @Override
    public void update(double mousex, double mousey) {
        //Update items
        loadItems();
        //Determine which mode is selected
        if (System.currentTimeMillis() - spawnTime > 250) {
            selectModule(mousex, mousey);
        }
        //Switch to selected mode if mode changed
        if (getSelectedModule() != null &&
                selectedModuleOriginal != selectedModuleNew && !stack.isEmpty()
                &&
                stack.getCapability(CapabilityModeChangingModularItem.MODE_CHANGING_MODULAR_ITEM_HANDLER_CAPABILITY).isPresent()) {
            // update to detect mode changes
            selectedModuleOriginal = selectedModuleNew;
            NuminaPackets.sendToServer(new MusePacketModeChangeRequest(player.getEntityId(), getSelectedModule().getModule().getItem().getRegistryName().toString(), player.inventory.currentItem));
        }
    }

    public void drawSelection() {
        ClickableModule module = getSelectedModule();
        if (module != null) {
            MusePoint2D pos = module.getPosition();
            MuseRenderer.drawCircleAround(pos.getX(), pos.getY(), 10);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)  {
        //Draw the installed power fist modes
        for (ClickableModule mode : modeButtons) {
            mode.render(mouseX, mouseY, partialTicks);
        }
        //Draw the selected mode indicator
        drawSelection();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {

    }

    @Override
    public void onMouseUp(double x, double y, int button) {

    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        ClickableModule module = getSelectedModule();
        if (module != null) {
            ItemStack selectedModule = module.getModule();
            return Collections.singletonList(module.getLocalizedName(selectedModule));
        }
        return null;
    }
}
