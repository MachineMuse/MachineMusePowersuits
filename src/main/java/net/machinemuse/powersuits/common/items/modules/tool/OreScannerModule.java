package net.machinemuse.powersuits.common.items.modules.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.powersuits.common.items.modules.PropertyModifierIntLinearAdditive;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_ORE_SCANNER;

public class OreScannerModule extends PowerModuleBase implements IRightClickModule {
    public static final String ORE_SCANNER_ENERGY_CONSUMPTION = "Energy Per Block";
    public static final String ORE_SCANNER_RADIUS_X = "X Radius";
    public static final String ORE_SCANNER_RADIUS_Y = "Y Radius";
    public static final String ORE_SCANNER_RADIUS_Z = "Z Radius";
    private static Map<Map<ResourceLocation, Integer>, Integer> oreValuemap = new HashMap<>();

    public OreScannerModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(ORE_SCANNER_ENERGY_CONSUMPTION, 50);
        addBaseProperty(ORE_SCANNER_RADIUS_X, 1);
        addBaseProperty(ORE_SCANNER_RADIUS_Y, 1);
        addBaseProperty(ORE_SCANNER_RADIUS_Z, 1);
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
        int xRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X);
        int yRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y);
        int zRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z);

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
                    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION));
                    totalEnergy += ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION);
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

                if (MPSSettings.general.useAdvancedOreScannerMessage) {
//                    player.addChatMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue + "\nSearch radius: " +
//                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
//                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
//                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
//                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));

                    player.sendMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestvalueblockname + "\nSearch radius: " +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
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
                oreValuemap = MPSSettings.getOreValues();

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
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_ORE_SCANNER;
    }

    @Override
    public String getUnlocalizedName() {
        return "oreScanner";
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

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.oreScanner;
    }
}
