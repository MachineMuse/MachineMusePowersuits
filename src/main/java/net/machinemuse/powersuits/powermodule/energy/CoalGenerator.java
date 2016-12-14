package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Eximius88 on 1/16/14.
 */
public class CoalGenerator extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_COAL_GEN = "Coal Generator";
    public static final String COAL_ENERGY_GEN = "Energy per coal";
    public static final String COAL_HEAT_GEN = "Heat Generation";
    public static final String MAX_COAL_STORAGE = "Maximum storage amount";


    public CoalGenerator(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(MAX_COAL_STORAGE, 128);
        addBaseProperty(COAL_HEAT_GEN, 2.5);
        addBaseProperty(MuseCommonStrings.WEIGHT, 500);
        addBaseProperty(COAL_ENERGY_GEN, 300);
        addInstallCost(new ItemStack(Blocks.furnace));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        IInventory inv = player.inventory;
        int coalNeeded = (int) ModuleManager.computeModularProperty(item, MAX_COAL_STORAGE) - MuseItemUtils.getCoalLevel(item);
        if (coalNeeded > 0) {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.coal) {
                    int loopTimes = coalNeeded < stack.stackSize ? coalNeeded : stack.stackSize;
                    for (int i2 = 0; i2 < loopTimes; i2++) {
                        MuseItemUtils.setCoalLevel(item, MuseItemUtils.getCoalLevel(item) + 1);
                        player.inventory.decrStackSize(i, 1);
                        if (stack.stackSize == 0) {
                            player.inventory.setInventorySlotContents(i, null);
                        }
                    }
                    if (ModuleManager.computeModularProperty(item, MAX_COAL_STORAGE) - MuseItemUtils.getCoalLevel(item) < 1) {
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
    public String getTextureFile() {
        return "coalgen";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
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
    public String getDescription() {
        return "Generate power with solid fuels";
    }
}
