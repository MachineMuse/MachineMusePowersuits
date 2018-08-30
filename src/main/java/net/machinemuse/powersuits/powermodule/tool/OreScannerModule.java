//package net.machinemuse.powersuits.powermodule.tool;
//
//import net.machinemuse.numina.api.module.EnumModuleCategory;
//import net.machinemuse.numina.api.module.EnumModuleTarget;
//import net.machinemuse.numina.api.module.IRightClickModule;
//import net.machinemuse.numina.utils.item.MuseItemUtils;
//import net.machinemuse.powersuits.api.module.ModuleManager;
//import net.machinemuse.powersuits.client.event.MuseIcon;
//import net.machinemuse.powersuits.common.Config;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.numina.api.nbt.PropertyModifierIntLinearAdditive;
//import net.machinemuse.powersuits.utils.ElectricItemUtils;
//import net.machinemuse.powersuits.utils.MuseCommonStrings;
//import net.machinemuse.powersuits.utils.MuseStringUtils;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.*;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextComponentString;
//import net.minecraft.world.World;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class OreScannerModule extends PowerModuleBase implements IRightClickModule {
//    public static final String MODULE_ORE_SCANNER = "Ore Scanner";
//    public static final String ORE_SCANNER_ENERGY_CONSUMPTION = "Energy Per Block";
//    public static final String ORE_SCANNER_RADIUS_X = "X Radius";
//    public static final String ORE_SCANNER_RADIUS_Y = "Y Radius";
//    public static final String ORE_SCANNER_RADIUS_Z = "Z Radius";
//    private static Map<Map<ResourceLocation, Integer>, Integer> oreValuemap = new HashMap<>();
//
//    public OreScannerModule(EnumModuleTarget moduleTarget) {
//        super(moduleTarget);
//        addBaseProperty(ORE_SCANNER_ENERGY_CONSUMPTION, 50);
//        addBaseProperty(ORE_SCANNER_RADIUS_X, 1);
//        addBaseProperty(ORE_SCANNER_RADIUS_Y, 1);
//        addBaseProperty(ORE_SCANNER_RADIUS_Z, 1);
//        addIntTradeoffProperty("X Radius", ORE_SCANNER_RADIUS_X, 3, "m", 1, 0);
//        addIntTradeoffProperty("Y Radius", ORE_SCANNER_RADIUS_Y, 3, "m", 1, 0);
//        addIntTradeoffProperty("Z Radius", ORE_SCANNER_RADIUS_Z, 3, "m", 1, 0);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.computerChip, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
//    }
//
//    private static final ArrayList<List<ItemStack>> ores = new ArrayList<>();
//    private static final HashMap<List, String> oreMap = new HashMap();
//    private static final HashMap<String, Integer> valueMap = new HashMap();
//
//    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit, int roundTo, int offset) {
//        units.put(propertyName, unit);
//        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, multiplier, roundTo, offset));
//    }
//
//    public void betterSearchForValuables(ItemStack itemStack, EntityPlayer player, World world) {
//        int xRadius = (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X);
//        int yRadius = (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y);
//        int zRadius = (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z);
//
//        BlockPos pos =  player.getPosition();
//
//        int getX = pos.getX();
//        int getY = pos.getY();
//        int z = pos.getZ();
//
//        int totalValue = 0, totalEnergy = 0, highestValue = 0;//, getValue;
//        Integer getValue;
//        String highestvalueblockname = "";
//        for (int sX = getX - xRadius; sX <= getX + xRadius; sX++) {
//            for (int sY = getY - yRadius; sY <= getY + yRadius; sY++) {
//                for (int sZ = z - zRadius; sZ <= z + zRadius; sZ++) {
//                    BlockPos newpos = new BlockPos(sX, sY, sZ);
//                    IBlockState state = world.getBlockState(newpos);
//                    getValue = getValue(state);
//                    totalValue += getValue;
//                    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION));
//                    totalEnergy += ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION);
//                    if (getValue > highestValue) {
//                        highestValue = getValue;
//                        Block block = state.getBlock();
//                        ItemStack stack = block.getPickBlock(state, null, world, newpos, player);
//                        try {
//                            highestvalueblockname = stack.getDisplayName();
//                        } catch (Exception e) {
//                            highestvalueblockname = block.getUnlocalizedName();
//                        }
//                    }
//                }
//            }
//        }
//
//        if (ElectricItemUtils.getMaxPlayerEnergy(player) > totalEnergy) {
//            ElectricItemUtils.drainPlayerEnergy(player, totalEnergy);
//            if (world.isRemote) {
//                if (highestvalueblockname.isEmpty())
//                    highestvalueblockname = "None";
//
//                if (Config.useAdvancedOreScannerMessage()) {
////                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore getValue: " + totalValue + " --- Most valuable: " + highestValue + "\nSearch radius: " +
////                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "getX" +
////                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "getX" +
////                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
////                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));
//
//                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore getValue: " + totalValue + " --- Most valuable: " + highestvalueblockname + "\nSearch radius: " +
//                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "getX" +
//                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "getX" +
//                            (2 * (int) ModuleManager.INSTANCE.INSTANCE.getOrSetModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
//                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));
//                } else {
////                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore getValue: " + totalValue + " --- Most valuable: " + highestValue));
//                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore getValue: " + totalValue + " --- Most valuable: " + highestvalueblockname));
//                }
//            }
//        }
//    }
//
//    /*
//     * actual checking now... immagine that.
//     */
//    public static int getValue(IBlockState state) {
//        try {
//            Block block = state.getBlock();
//            if (block == Blocks.AIR || block == Blocks.STONE)
//                return 0;
//
//            if (oreValuemap.isEmpty())
//                oreValuemap = Config.getOreValues();
//
//            int meta = block.getMetaFromState(state);
//            Map<ResourceLocation, Integer> regNameMeta = new HashMap<>();
//            regNameMeta.put(block.getRegistryName(), meta);
//            Integer getValue = oreValuemap.get(regNameMeta);
//            if (getValue == null)
//                getValue = 0;
//            return getValue;
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    @Override
//    public EnumModuleCategory getCategory() {
//        return EnumModuleCategory.CATEGORY_TOOL;
//    }
//
//    @Override
//    public String getDataName() {
//        return MODULE_ORE_SCANNER;
//    }
//
//    @Override
//    public String getUnlocalizedName() {
//        return "oreScanner";
//    }
//
//    @Override
//    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        betterSearchForValuables(itemStackIn, playerIn, worldIn);
//        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//    }
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return MuseIcon.oreScanner;
//    }
//}
