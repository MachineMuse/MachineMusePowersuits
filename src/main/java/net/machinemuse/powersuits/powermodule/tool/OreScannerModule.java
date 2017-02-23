package net.machinemuse.powersuits.powermodule.tool;


import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OreScannerModule extends PowerModuleBase implements IRightClickModule {

    public static final String MODULE_ORE_SCANNER = "Ore Scanner";
    public static final String ORE_SCANNER_ENERGY_CONSUMPTION = "Energy Per Block";
    public static final String ORE_SCANNER_RADIUS_X = "X Radius";
    public static final String ORE_SCANNER_RADIUS_Y = "Y Radius";
    public static final String ORE_SCANNER_RADIUS_Z = "Z Radius";

    private static final String[] oreNames = {"oreCopper",
                                                "oreTin",
                                                "oreSilver",
                                                "oreLead",
                                                "oreNickel",
                                                "orePlatinum",
                                                "oreZinc",
                                                "oreApatite",
                                                "oreUranium"};
    private static final ArrayList<List<ItemStack>> ores = new ArrayList<>();
    private static final HashMap<List, String> oreMap = new HashMap();
    private static final HashMap<String, Integer> valueMap = new HashMap();

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
        for (int i = 0; i < oreNames.length; i++) {
            ores.add(i, OreDictionary.getOres(oreNames[i]));
        }
        fillMap();
    }

    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, multiplier, roundTo, offset));
    }

    public void searchForValuables(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        int xRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X);
        int yRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y);
        int zRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int totalValue = 0, totalEnergy = 0, highestValue = 0, value;
        EnumFacing fdSide = side.getOpposite();
        int cX = x + (fdSide.getFrontOffsetX() * xRadius);
        int cY = y + (fdSide.getFrontOffsetY() * yRadius);
        int cZ = z + (fdSide.getFrontOffsetZ() * zRadius);

        for (int sX = cX - xRadius; sX <= cX + xRadius; sX++) {
            for (int sY = cY - yRadius; sY <= cY + yRadius; sY++) {
                for (int sZ = cZ - zRadius; sZ <= cZ + zRadius; sZ++) {
                    BlockPos newpos = new BlockPos(sX, sY, sZ);
                    IBlockState state = world.getBlockState(newpos);


                    value = getValue(state.getBlock(), 0); // FIXME: there is no accessable meta anymore
                    totalValue += value;
                    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION));
                    totalEnergy += ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_ENERGY_CONSUMPTION);
                    if (value > highestValue) {
                        highestValue = value;
                    }
                }
            }
        }

        if (ElectricItemUtils.getPlayerEnergy(player) > totalEnergy) {
            ElectricItemUtils.drainPlayerEnergy(player, totalEnergy);
            if (MuseItemUtils.isServerSide()) {
                if (Config.useAdvancedOreScannerMessage()) {
                    player.addChatMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue + "\nSearch radius: " +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));
                } else {
                    player.addChatMessage(new TextComponentString("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue));
                }
            }
        }
    }

    public static int getValue(Block block, int meta) {
        if ((oreMap.containsKey(Arrays.asList(block, meta))) && (valueMap.containsKey(oreMap.get(Arrays.asList(block, meta))))) {
            return valueMap.get(oreMap.get(Arrays.asList(block, meta)));
        }
        return 0;
    }

    public static void fillMap() {
        for (int a = 0; a < ores.size(); a++) {
            for (int b = 0; b < ores.get(a).size(); b++) {
                oreMap.put(Arrays.asList(ores.get(a).get(b), ores.get(a).get(b).getItemDamage()), oreNames[a]);
            }
        }
        oreMap.put(Arrays.asList(Blocks.COAL_ORE, 0), "oreCoal");
        oreMap.put(Arrays.asList(Blocks.IRON_ORE, 0), "oreIron");
        oreMap.put(Arrays.asList(Blocks.GOLD_ORE, 0), "oreGold");
        oreMap.put(Arrays.asList(Blocks.REDSTONE_ORE, 0), "oreRedstone");
        oreMap.put(Arrays.asList(Blocks.DIAMOND_ORE, 0), "oreDiamond");
        oreMap.put(Arrays.asList(Blocks.EMERALD_ORE, 0), "oreEmerald");
        oreMap.put(Arrays.asList(Blocks.LAPIS_ORE, 0), "oreLapis");
        oreMap.put(Arrays.asList(Blocks.QUARTZ_ORE, 0), "oreNetherQuartz");
        valueMap.put("oreCoal", 1);
        valueMap.put("oreIron", 4);
        valueMap.put("oreGold", 6);
        valueMap.put("oreRedstone", 3);
        valueMap.put("oreDiamond", 16);
        valueMap.put("oreEmerald", 18);
        valueMap.put("oreLapis", 12);
        valueMap.put("oreNetherQuartz", 8);
        valueMap.put("oreCopper", 4);
        valueMap.put("oreTin", 5);
        valueMap.put("oreSilver", 5);
        valueMap.put("oreLead", 6);
        valueMap.put("oreNickel", 14);
        valueMap.put("orePlatinum", 8);
        valueMap.put("oreZinc", 1);
        valueMap.put("oreApatite", 2);
        valueMap.put("oreUranium", 14);
        valueMap.put("oreXychorium", 2);
        valueMap.put("oreNaturalAluminum", 3);
        valueMap.put("oreCertusQuartz", 5);
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
    public String getDescription() {
        return "A way to see how valuable the land around you is.";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        searchForValuables(stack, playerIn, worldIn, pos, facing);


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