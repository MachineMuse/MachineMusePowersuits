package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        addInstallCost(new ItemStack(Items.IRON_INGOT, 3));
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
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, item);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block blockID = world.getBlockState(pos).getBlock();
        int plant = (int) ModuleManager.computeModularProperty(itemStack, PLANT_RADIUS);
        int leaf = (int) ModuleManager.computeModularProperty(itemStack, LEAF_RADIUS);
        int snow = (int) ModuleManager.computeModularProperty(itemStack, SNOW_RADIUS);
        int totalEnergyDrain = 0;

        // Plants
        useBlower(plant, "plants", itemStack, player, world,  pos);
        // Leaves
        useBlower(leaf, "leaves", itemStack, player, world,  pos);
        // Snow
        useBlower(snow, "snow", itemStack, player, world,  pos);
        return EnumActionResult.PASS;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    private boolean blockCheckAndHarvest(String blocktype, EntityPlayer player, World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block == null)
            return false;

        // Plants
        if (blocktype == "plants" && (block != null) && ((block instanceof BlockTallGrass) || (block instanceof BlockFlower)) && block.canHarvestBlock(player.worldObj, pos, player)) {
            block.harvestBlock(world, player, pos, blockState, null, null);
            world.setBlockToAir(pos);
            return true;
        }

        // Leaves
        if (blocktype == "leaves" && block instanceof BlockLeaves && block.canHarvestBlock(player.worldObj, pos, player)) {
            block.harvestBlock(world, player, pos, blockState, null, null);
            world.setBlockToAir(pos);
            return true;
        }

        // Snow
        if (blocktype == "snow" && block instanceof BlockSnow && block.canHarvestBlock(player.worldObj, pos, player)) {
            block.harvestBlock(world, player, pos, blockState, null, null);
            world.setBlockToAir(pos);
        }
        return false;
    }

    private void useBlower(int radius, String blocktypename , ItemStack itemStack, EntityPlayer player, World world, BlockPos pos) {
        int totalEnergyDrain = 0;
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                for (int k = -radius; k < radius; k++) {
                    if (blockCheckAndHarvest(blocktypename, player, world, pos.add(i, j, k))) {
                        totalEnergyDrain += ModuleManager.computeModularProperty(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.leafBlower;
    }
}
