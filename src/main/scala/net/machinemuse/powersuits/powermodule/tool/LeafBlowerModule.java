package net.machinemuse.powersuits.powermodule.tool;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockClay;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;


import java.util.List;

/**
 * Created by User: Andrew2448
 * 7:13 PM 4/21/13
 */
public class LeafBlowerModule extends PowerModuleBase implements IRightClickModule {
    private static final String MODULE_LEAF_BLOWER = "Leaf Blower";
    private static final String LEAF_BLOWER_ENERGY_CONSUMPTION = "Energy Consumption";
    private static final String PLANT_RADIUS = "Plant Radius";
    private static final String LEAF_RADIUS = "Leaf Radius";
    private static final String SNOW_RADIUS = "Snow Radius";

    public LeafBlowerModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.iron_ingot, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(LEAF_BLOWER_ENERGY_CONSUMPTION, 100, "J");
        addBaseProperty(PLANT_RADIUS, 1, "m");
        addBaseProperty(LEAF_RADIUS, 1, "m");
        addBaseProperty(SNOW_RADIUS, 1, "m");
        addIntTradeoffProperty(PLANT_RADIUS, PLANT_RADIUS, 8, "m", 1, 0);
        addIntTradeoffProperty(LEAF_RADIUS, LEAF_RADIUS, 8, "m", 1, 0);
        addIntTradeoffProperty(SNOW_RADIUS, SNOW_RADIUS, 5, "m", 1, 0);
    }

    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, multiplier, roundTo, offset));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_LEAF_BLOWER;
    }

    @Override
    public String getUnlocalizedName() {
        return "leafBlower";
    }

    @Override
    public String getDescription() {
        return "Create a torrent of air to knock plants out of the ground and leaves off of trees.";
    }

    @Override
    public String getTextureFile() {
        return "leafblower";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block blockID = world.getBlock(x, y, z);
        int plant = (int) ModuleManager.computeModularProperty(itemStack, PLANT_RADIUS);
        int leaf = (int) ModuleManager.computeModularProperty(itemStack, LEAF_RADIUS);
        int snow = (int) ModuleManager.computeModularProperty(itemStack, SNOW_RADIUS);
        int totalEnergyDrain = 0;

        // Leaves
        if (blockID != null && blockID.isLeaves(world, x, y, z)) {
            for (int i = -leaf; i < leaf; i++) {
                for (int j = -leaf; j < leaf; j++) {
                    for (int k = -leaf; k < leaf; k++) {
                        Block leafBlock = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);
                        if (leafBlock != null && leafBlock.isLeaves(world, x + i, y + j, z + k)) {
                            if (leafBlock.canHarvestBlock(player, meta)) {
                                leafBlock.harvestBlock(world, player, x + i, y + j, z + k, meta);
                                totalEnergyDrain += ModuleManager.computeModularProperty(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                            }
                            world.setBlockToAir(x + i, y + j, z + k);
                        }
                    }
                }
            }
        }

        //Snow
        for (int i = -snow; i < snow; i++) {
            for (int j = -snow; j < snow; j++) {
                for (int k = -snow; k < snow; k++) {
                    Block snowBlock = world.getBlock(x + i, y + j, z + k);
                    int meta = world.getBlockMetadata(x + i, y + j, z + k);
                    if (snowBlock instanceof BlockSnow) {
                        snowBlock.harvestBlock(world, player, x + i, y + j, z + k, meta);
                        totalEnergyDrain += ModuleManager.computeModularProperty(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                        world.setBlockToAir(x + i, y + j, z + k);

                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);


        // Plants
        for (int i = -plant; i < plant; i++) {
            for (int j = -plant; j < plant; j++) {
                for (int k = -plant; k < plant; k++) {
                    Block flowerBlock = world.getBlock(x + i, y + j, z + k);
                    int meta = world.getBlockMetadata(x + i, y + j, z + k);
                    if (flowerBlock != null && flowerBlock instanceof BlockFlower) {
                        if (flowerBlock.canHarvestBlock(player, meta)) {
                            flowerBlock.harvestBlock(world, player, x + i, y + j, z + k, meta);
                            totalEnergyDrain += ModuleManager.computeModularProperty(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                            world.setBlockToAir(x + i, y + j, z + k);
                        }
                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }
}
