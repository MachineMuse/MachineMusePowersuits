package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.nbt.PropertyModifierIntLinearAdditive;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OreScannerModule extends PowerModuleBase implements IRightClickModule {
    public static final String ORE_SCANNER_ENERGY_CONSUMPTION = "Energy Per Block";
    public static final String ORE_SCANNER_RADIUS_X = "X Radius";
    public static final String ORE_SCANNER_RADIUS_Y = "Y Radius";
    public static final String ORE_SCANNER_RADIUS_Z = "Z Radius";
    private static Map<Map<ResourceLocation, Integer>, Integer> oreValuemap = new HashMap<>();

    public OreScannerModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain,UnlocalizedName);
        addBasePropertyDouble(ORE_SCANNER_ENERGY_CONSUMPTION, 20);
        addBasePropertyDouble(ORE_SCANNER_RADIUS_X, 1);
        addBasePropertyDouble(ORE_SCANNER_RADIUS_Y, 1);
        addBasePropertyDouble(ORE_SCANNER_RADIUS_Z, 1);
        addIntTradeoffProperty("X Radius", ORE_SCANNER_RADIUS_X, 3, "m", 1, 0);
        addIntTradeoffProperty("Y Radius", ORE_SCANNER_RADIUS_Y, 3, "m", 1, 0);
        addIntTradeoffProperty("Z Radius", ORE_SCANNER_RADIUS_Z, 3, "m", 1, 0);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.computerChip, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    private static final ArrayList<List<ItemStack>> ores = new ArrayList<>();
    private static final HashMap<List, String> oreMap = new HashMap();
    private static final HashMap<String, Integer> valueMap = new HashMap();

    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, multiplier, roundTo, offset));
    }

    public void betterSearchForValuables(ItemStack itemStack, EntityPlayer player, World world) {
        int xRadius = (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X);
        int yRadius = (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y);
        int zRadius = (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z);

        BlockPos pos =  player.getPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int totalValue = 0, totalEnergy = 0, highestValue = 0;//, value;
        Integer value;
        String highestvalueblockname = "";
        for (int sX = x - xRadius; sX <= x + xRadius; sX++) {
            for (int sY = y - yRadius; sY <= y + yRadius; sY++) {
                for (int sZ = z - zRadius; sZ <= z + zRadius; sZ++) {
                    BlockPos newpos = new BlockPos(sX, sY, sZ);
                    IBlockState state = world.getBlockState(newpos);
                    value = getValue(state);
                    totalValue += value;
                    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION));
                    totalEnergy += ModuleManager.getInstance().computeModularPropertyInteger(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION);
                    if (value > highestValue) {
                        highestValue = value;
                        Block block = state.getBlock();
                        ItemStack stack = block.getPickBlock(state, null, world, newpos, player);
                        try {
                            highestvalueblockname = stack.getDisplayName();
                        } catch (Exception e) {
                            highestvalueblockname = block.getUnlocalizedName();
                        }
                    }
                }
            }
        }

        if (ElectricItemUtils.getPlayerEnergy(player) > totalEnergy) {
            ElectricItemUtils.drainPlayerEnergy(player, totalEnergy);
            if (MuseItemUtils.isServerSide()) {
                if (highestvalueblockname.isEmpty())
                    highestvalueblockname = "None";

                if (MPSConfig.getInstance().useAdvancedOreScannerMessage()) {
//                    player.addChatMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue + "\nSearch radius: " +
//                            (2 * (int) ModuleManager.computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
//                            (2 * (int) ModuleManager.computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
//                            (2 * (int) ModuleManager.computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
//                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));

                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestvalueblockname + "\nSearch radius: " +
                            (2 * (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
                            (2 * (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
                            (2 * (int) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));
                } else {
//                    player.addChatMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue));
                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestvalueblockname));
                }
            }
        }
    }

    /*
     * actual checking now... immagine that.
     */
    public static int getValue(IBlockState state) {
        try {
            Block block = state.getBlock();
            if (block == Blocks.AIR || block == Blocks.STONE)
                return 0;

            if (oreValuemap.isEmpty())
                oreValuemap = MPSConfig.getInstance().getOreValues();

            int meta = block.getMetaFromState(state);
            Map<ResourceLocation, Integer> regNameMeta = new HashMap<>();
            regNameMeta.put(block.getRegistryName(), meta);
            Integer value = oreValuemap.get(regNameMeta);
            if (value == null)
                value = 0;
            return value;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        betterSearchForValuables(itemStackIn, playerIn, worldIn);
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
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
        return MuseIcon.oreScanner;
    }
}
