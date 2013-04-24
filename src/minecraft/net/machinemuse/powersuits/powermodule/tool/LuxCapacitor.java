package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public static final String ENERGY = "Lux Capacitor Energy Consumption";

    public LuxCapacitor(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Item.lightStoneDust, 1));
        addInstallCost(new ItemStack(Item.ingotIron, 2));
        addBaseProperty(ENERGY, 100, "J");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getName() {
        return "Lux Capacitor";
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

                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(world, player);
                world.spawnEntityInWorld(luxCapacitor);
            }
        }
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
                                  float hitZ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        // TODO Auto-generated method stub

    }

}
