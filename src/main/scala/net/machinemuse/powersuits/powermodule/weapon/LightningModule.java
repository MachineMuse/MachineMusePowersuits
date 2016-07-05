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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

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
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_LIGHTNING;
    }

    @Override
    public String getUnlocalizedName() { return "lightningSummoner";
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
                RayTraceResult MOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
                world.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, MOP.hitVec.xCoord, MOP.hitVec.yCoord, MOP.hitVec.zCoord, false));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.lightning;
    }
}
