package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LUX_CAPACITOR = "Lux Capacitor";
    public static final String ENERGY = "Lux Capacitor Energy Consumption";
    public static final String RED =  "Lux Capacitor Red Hue";
    public static final String GREEN = "Lux Capacitor Green Hue";
    public static final String BLUE = "Lux Capacitor Blue Hue";

    public LuxCapacitor(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.glowstone_dust, 1));
        addInstallCost(new ItemStack(Items.iron_ingot, 2));
        addBaseProperty(ENERGY, 100, "J");
        addTradeoffProperty("Red", RED, 1, "%");
        addTradeoffProperty("Green", GREEN, 1, "%");
        addTradeoffProperty("Blue", BLUE, 1, "%");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_LUX_CAPACITOR;
    }

    @Override
    public String getUnlocalizedName() { return "luxCapacitor";
    }

    @Override
    public String getDescription() {
        return "Launch a virtually infinite number of attractive light sources at the wall.";
    }

    @Override
    public String getTextureFile() {
        return "bluelight";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack itemStack) {
        player.setItemInUse(itemStack, 10);
        if (!world.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStack, ENERGY);
            // MuseHeatUtils.heatPlayer(player, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);

                double red = ModuleManager.computeModularProperty(itemStack, RED);
                double green = ModuleManager.computeModularProperty(itemStack, GREEN);
                double blue = ModuleManager.computeModularProperty(itemStack, BLUE);
                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(world, player, red, green, blue);
                world.spawnEntityInWorld(luxCapacitor);
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        // TODO Auto-generated method stub
    }
}
