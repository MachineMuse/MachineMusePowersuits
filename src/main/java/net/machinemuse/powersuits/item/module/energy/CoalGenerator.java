package net.machinemuse.powersuits.item.module.energy;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.module.helpers.CoalGenHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Eximius88 on 1/16/14.
 */
public class CoalGenerator extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String COAL_ENERGY_GEN = "Energy per coal";
    public static final String COAL_HEAT_GEN = "Heat Generation";
    public static final String MAX_COAL_STORAGE = "Maximum storage amount";

    public CoalGenerator(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TORSOONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(MAX_COAL_STORAGE, 128);
        addBasePropertyDouble(COAL_HEAT_GEN, 2.5);
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 500);
        addBasePropertyDouble(COAL_ENERGY_GEN, 300);
        addInstallCost(new ItemStack(Blocks.FURNACE));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        IInventory inv = player.inventory;
        int coalNeeded = (int) ModuleManager.getInstance().computeModularPropertyDouble(item, MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item);
        if (coalNeeded > 0) {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.COAL) {
                    int loopTimes = coalNeeded < stack.getCount() ? coalNeeded : stack.getCount();
                    for (int i2 = 0; i2 < loopTimes; i2++) {
                        CoalGenHelper.setCoalLevel(item, CoalGenHelper.getCoalLevel(item) + 1);
                        player.inventory.decrStackSize(i, 1);
                        if (stack.getCount() == 0) {
                            player.inventory.setInventorySlotContents(i, null);
                        }
                    }
                    if (ModuleManager.getInstance().computeModularPropertyDouble(item, MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item) < 1) {
                        i = inv.getSizeInventory() + 1;
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENERGY;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.coalGenerator;
    }
}