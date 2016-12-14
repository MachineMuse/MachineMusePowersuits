package net.machinemuse.powersuits.powermodule.weapon;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 5:56 PM 6/14/13
 */
public class LightningModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LIGHTNING = "Lightning Summoner";
    public static final String LIGHTNING_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final String HEAT = "Heat Emission";

    public LightningModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addBaseProperty(LIGHTNING_ENERGY_CONSUMPTION, 490000, "");
        addBaseProperty(HEAT, 100, "");
    }

    @Override
    public String getTextureFile() {
        return "bluestar";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_LIGHTNING;
    }

    @Override
    public String getUnlocalizedName() {
        return "lightningSummoner";
    }

    @Override
    public String getDescription() {
        return "Allows you to summon lightning for a large energy cost.";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
        try {
            double range = 64;
            double energyConsumption = ModuleManager.computeModularProperty(item, LIGHTNING_ENERGY_CONSUMPTION);
            if (energyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
                MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, HEAT));
                MovingObjectPosition MOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
                world.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, MOP.hitVec.xCoord, MOP.hitVec.yCoord, MOP.hitVec.zCoord));

                /*for (int x = (int)player.posX-1; x < (int)player.posX+2; x++) {
                    for (int z = (int)player.posZ-1; z < (int)player.posZ+2; z++) {
                        if (player.canPlayerEdit(x, (int)player.posY, z, 1, item)) {
                            int id = world.getBlockId(x, (int)player.posY, z);
                            if (id == 0) {
                                world.setBlock(x, (int)player.posY, z, Block.fire.blockID);
                            }
                        }
                    }
                }*/
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}
