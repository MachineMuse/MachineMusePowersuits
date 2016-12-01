package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

/**
 * Created by User: Andrew2448
 * 7:13 PM 4/21/13
 */
public class LeafBlowerModule extends PowerModuleBase implements IRightClickModule {
    private static final String MODULE_LEAF_BLOWER = "Leaf Blower";
    private static final String LEAF_BLOWER_ENERGY_CONSUMPTION = "Energy Consumption";
    private static final String RADIUS = "Radius";


//    private static final String PLANT_RADIUS = "Plant Radius";
//    private static final String LEAF_RADIUS = "Leaf Radius";
//    private static final String SNOW_RADIUS = "Snow Radius";

    public LeafBlowerModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(LEAF_BLOWER_ENERGY_CONSUMPTION, 100, "J");

        addBaseProperty(RADIUS, 1, "m");


//        addBaseProperty(PLANT_RADIUS, 1, "m");
//        addBaseProperty(LEAF_RADIUS, 1, "m");
//        addBaseProperty(SNOW_RADIUS, 1, "m");

        addIntTradeoffProperty(RADIUS, RADIUS, 8, "m", 1, 0);

//        addIntTradeoffProperty(PLANT_RADIUS, PLANT_RADIUS, 8, "m", 1, 0);
//        addIntTradeoffProperty(LEAF_RADIUS, LEAF_RADIUS, 8, "m", 1, 0);
//        addIntTradeoffProperty(SNOW_RADIUS, SNOW_RADIUS, 5, "m", 1, 0);
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    private boolean blockCheckAndHarvest(String blocktype, EntityPlayer player, World world, int x, int y, int z) {
//        Block block = world.getBlock(x, y, z);
//        int meta = world.getBlockMetadata(x, y, z);
//        if (block == null)
//            return false;
//
//        // Plants
//        if (Objects.equals(blocktype, "plants") && (block instanceof BlockTallGrass || block instanceof BlockFlower) && block.canHarvestBlock(player, meta)) {
//            block.harvestBlock(world, player, x, y, z, meta);
//            world.setBlockToAir(x, y, z);
//            return true;
//        }
//
//        // Leaves
//        if (Objects.equals(blocktype, "leaves") && block instanceof BlockLeaves && block.canHarvestBlock(player, meta)) {
//            block.harvestBlock(world, player, x, y, z, meta);
//            world.setBlockToAir(x, y, z);
//            return true;
//        }
//
//        // Snow
//        if (Objects.equals(blocktype, "snow") && block instanceof BlockSnow && block.canHarvestBlock(player, meta)) {
//            block.harvestBlock(world, player, x, y, z, meta);
//            world.setBlockToAir(x, y, z);
//        }
        return false;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        /* FIXME: this really needs to be one radius for everything.

         */




        //        Block blockID = world.getBlock(x, y, z);
//        int plant = (int) ModuleManager.computeModularProperty(itemStack, PLANT_RADIUS);
//        int leaf = (int) ModuleManager.computeModularProperty(itemStack, LEAF_RADIUS);
//        int snow = (int) ModuleManager.computeModularProperty(itemStack, SNOW_RADIUS);
//        int totalEnergyDrain = 0;
//
//        // Plants
//        useBlower(plant, "plants", itemStack, player, world,  x, y, z);
//        // Leaves
//        useBlower(leaf, "leaves", itemStack, player, world,  x, y, z);
//        // Snow
//        useBlower(snow, "snow", itemStack, player, world,  x, y, z);
        return EnumActionResult.PASS;
    }

    private void useBlower(int radius, String blocktypename , ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {
        int totalEnergyDrain = 0;
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                for (int k = -radius; k < radius; k++) {
                    if (blockCheckAndHarvest(blocktypename, player, world, x + i, y + j, z + k)) {
                        totalEnergyDrain += ModuleManager.computeModularProperty(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.leafBlower;
    }
}