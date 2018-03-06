package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.nbt.PropertyModifierIntLinearAdditive;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
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

/**
 * Created by User: Andrew2448
 * 7:13 PM 4/21/13
 */
public class LeafBlowerModule extends PowerModuleBase implements IRightClickModule {
    private static final String LEAF_BLOWER_ENERGY_CONSUMPTION = "Energy Consumption";
    private static final String RADIUS = "Radius";


//    private static final String PLANT_RADIUS = "Plant Radius";
//    private static final String LEAF_RADIUS = "Leaf Radius";
//    private static final String SNOW_RADIUS = "Snow Radius";

    public LeafBlowerModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyDouble(LEAF_BLOWER_ENERGY_CONSUMPTION, 100, "J");

        addBasePropertyDouble(RADIUS, 1, "m");


//        addBasePropertyDouble(PLANT_RADIUS, 1, "m");
//        addBasePropertyDouble(LEAF_RADIUS, 1, "m");
//        addBasePropertyDouble(SNOW_RADIUS, 1, "m");

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
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        int radius = (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, RADIUS);
        if (useBlower(radius, itemStackIn, playerIn, worldIn, playerIn.getPosition()))
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);

        //        Block blockID = world.getBlock(x, y, z);
//        int plant = (int) ModuleManager.computeModularPropertyDouble(itemStack, PLANT_RADIUS);
//        int leaf = (int) ModuleManager.computeModularPropertyDouble(itemStack, LEAF_RADIUS);
//        int snow = (int) ModuleManager.computeModularPropertyDouble(itemStack, SNOW_RADIUS);
//        int totalEnergyDrain = 0;
//
//        // Plants
//        useBlower(plant, "plants", itemStack, player, world,  x, y, z);
//        // Leaves
//        useBlower(leaf, "leaves", itemStack, player, world,  x, y, z);
//        // Snow
//        useBlower(snow, "snow", itemStack, player, world,  x, y, z);



        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    private boolean useBlower(int radius, ItemStack itemStack, EntityPlayer player, World world, BlockPos pos) {
        int totalEnergyDrain = 0;
        BlockPos newPos;
        for (int i = pos.getX() - radius; i < pos.getX() + radius ; i++) {
            for (int j = pos.getY() - radius; j < pos.getY() + radius; j++) {
                for (int k = pos.getZ() - radius; k < pos.getZ() + radius; k++) {
                    newPos = new BlockPos(i, j, k);
                    if (ToolHelpers.blockCheckAndHarvest(player, world, newPos)) {
                        totalEnergyDrain += ModuleManager.getInstance().computeModularPropertyDouble(itemStack, LEAF_BLOWER_ENERGY_CONSUMPTION);
                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
        return true;
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
