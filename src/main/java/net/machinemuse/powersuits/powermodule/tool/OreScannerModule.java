package net.machinemuse.powersuits.powermodule.tool;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
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

    private static String[] oreNames = {"oreCopper", "oreTin", "oreSilver", "oreLead", "oreNickel", "orePlatinum", "oreZinc", "oreApatite", "oreUranium"};
    private static ArrayList<ArrayList<ItemStack>> ores = new ArrayList<ArrayList<ItemStack>>();
    private static HashMap<List, String> oreMap = new HashMap();
    private static HashMap<String, Integer> valueMap = new HashMap();

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

    public void searchForValuables(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side) {
        int xRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X);
        int yRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y);
        int zRadius = (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z);

        int totalValue = 0, totalEnergy = 0, highestValue = 0, value;
        ForgeDirection fdSide = ForgeDirection.getOrientation(side).getOpposite();
        int cX = x + (fdSide.offsetX * xRadius);
        int cY = y + (fdSide.offsetY * yRadius);
        int cZ = z + (fdSide.offsetZ * zRadius);

        for (int sX = cX - xRadius; sX <= cX + xRadius; sX++) {
            for (int sY = cY - yRadius; sY <= cY + yRadius; sY++) {
                for (int sZ = cZ - zRadius; sZ <= cZ + zRadius; sZ++) {
                    value = getValue(world.getBlock(sX, sY, sZ), world.getBlockMetadata(sX, sY, sZ));
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
                    player.addChatMessage(new ChatComponentText("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue + "\nSearch radius: " +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_X) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Y) + 1) + "x" +
                            (2 * (int) ModuleManager.computeModularProperty(itemStack, ORE_SCANNER_RADIUS_Z) + 1) +
                            " --- Energy used: " + MuseStringUtils.formatNumberFromUnits(totalEnergy, "J")));
                } else {
                    player.addChatMessage(new ChatComponentText("[Ore Scanner] Total ore value: " + totalValue + " --- Most valuable: " + highestValue));
                }
            }
        }
    }

    public static int getValue(Block blockID, int meta) {
        if ((oreMap.containsKey(Arrays.asList(blockID, meta))) && (valueMap.containsKey(oreMap.get(Arrays.asList(blockID, meta))))) {
            return valueMap.get(oreMap.get(Arrays.asList(blockID, meta)));
        }
        return 0;
    }

    public static void fillMap() {
        for (int a = 0; a < ores.size(); a++) {
            for (int b = 0; b < ores.get(a).size(); b++) {
                oreMap.put(Arrays.asList(ores.get(a).get(b), ores.get(a).get(b).getItemDamage()), oreNames[a]);
            }
        }
        oreMap.put(Arrays.asList(Blocks.coal_ore, 0), "oreCoal");
        oreMap.put(Arrays.asList(Blocks.iron_ore, 0), "oreIron");
        oreMap.put(Arrays.asList(Blocks.gold_ore, 0), "oreGold");
        oreMap.put(Arrays.asList(Blocks.redstone_ore, 0), "oreRedstone");
        oreMap.put(Arrays.asList(Blocks.diamond_ore, 0), "oreDiamond");
        oreMap.put(Arrays.asList(Blocks.emerald_ore, 0), "oreEmerald");
        oreMap.put(Arrays.asList(Blocks.lapis_ore, 0), "oreLapis");
        oreMap.put(Arrays.asList(Blocks.quartz_ore, 0), "oreNetherQuartz");
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
    public String getTextureFile() {
        return "orescanner";
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
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        searchForValuables(itemStack, player, world, x, y, z, side);
        return true;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}
