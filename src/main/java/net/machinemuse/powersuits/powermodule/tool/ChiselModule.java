package net.machinemuse.powersuits.powermodule.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class ChiselModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_CHISEL = "Chisel";
    public static final String CHISEL_HARVEST_SPEED = "CHISEL Harvest Speed";
    public static final String CHISEL_ENERGY_CONSUMPTION = "CHISEL Energy Consumption";
    private Item ITEM_CHISEL = null;

    public ChiselModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(GameRegistry.findItem("minecraft", "obsidian"), 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(CHISEL_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(CHISEL_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", CHISEL_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", CHISEL_HARVEST_SPEED, 22);

        try {
            ITEM_CHISEL = ChiselGetter2.getChisel();
        } catch (NoClassDefFoundError f) {
            MuseLogger.logException("Couldn't get Chisel reference item", f);
        }
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_CHISEL;
    }

    @Override
    public String getUnlocalizedName() { return "chisel";
    }

    @Override
    public String getDescription() {
        return "This won't let you chisel blocks, but it will at least let you harvest them.";
    }

    @Override
    public String getTextureFile() {
        return "toolpinch";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (ModCompatibility.isChiselLoaded() && ITEM_CHISEL != null) {
            if (ForgeHooks.canToolHarvestBlock(block, meta, new ItemStack(ITEM_CHISEL))) {
                return ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, CHISEL_ENERGY_CONSUMPTION);
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        if (canHarvestBlock(stack, block, meta, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, CHISEL_ENERGY_CONSUMPTION));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), CHISEL_HARVEST_SPEED);
    }
}
