package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.modulehelpers.CoalGenHelper;
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
    public static final String MODULE_COAL_GEN = "Coal Generator";
    public static final String COAL_ENERGY_GEN = "Energy per coal";
    public static final String COAL_HEAT_GEN = "Heat Generation";
    public static final String MAX_COAL_STORAGE = "Maximum storage amount";


    public CoalGenerator(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addBaseProperty(MAX_COAL_STORAGE, 128);
        addBaseProperty(COAL_HEAT_GEN, 2.5);
        addBaseProperty(MPSModuleConstants.WEIGHT, 500);
        addBaseProperty(COAL_ENERGY_GEN, 300);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.FURNACE));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        IInventory inv = player.inventory;
        int coalNeeded = (int) ModuleManager.INSTANCE.computeModularProperty(item, MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item);
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
                    if (ModuleManager.INSTANCE.computeModularProperty(item, MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item) < 1) {
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
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_COAL_GEN;
    }

    @Override
    public String getUnlocalizedName() {
        return "coalGenerator";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.coalGenerator;
    }
}