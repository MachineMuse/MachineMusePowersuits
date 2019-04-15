package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.modulehelpers.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

import javax.annotation.Nonnull;

public abstract class CoolingSystemBase extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {

    public CoolingSystemBase(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (!player.world.isRemote) {
            double currentHeat = MuseHeatUtils.getPlayerHeat(player);
            if (currentHeat <= 0)
                return;

            double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
            FluidUtils fluidUtils = new FluidUtils(player, itemStack, this.getDataName());
            double fluidEfficiencyBoost = fluidUtils.getCoolingEfficiency();

            // if not overheating
            if (currentHeat < maxHeat) {
                double coolJoules = (fluidEfficiencyBoost + getCoolingBonus(itemStack)) * getCoolingFactor();
//                System.out.println("cool joules: " + coolJoules);

                if (ElectricItemUtils.getPlayerEnergy(player) > coolJoules) {

//                    System.out.println("cooling normally");

                    coolJoules = MuseHeatUtils.coolPlayer(player, coolJoules);

                    ElectricItemUtils.drainPlayerEnergy(player,
                            (int) (coolJoules * getEnergyConsumption(itemStack)));
                }

                // sacrificial emergency cooling
            } else {
                // how much player is overheating
                double overheatAmount = currentHeat - maxHeat;

                int fluidLevel = fluidUtils.getFluidLevel();

                boolean usedEmergencyCooling = false;
                // if system has enough fluid using this "very special" formula
                if (fluidLevel >= (int) (fluidEfficiencyBoost * overheatAmount)) {
                    fluidUtils.drain((int) (fluidEfficiencyBoost * overheatAmount));
                    MuseHeatUtils.coolPlayer(player, overheatAmount + 1);
                    usedEmergencyCooling = true;

                    // sacrifice whatever fluid is in the system
                } else if (fluidLevel > 0) {
                    fluidUtils.drain(fluidLevel);
                    MuseHeatUtils.coolPlayer(player, fluidEfficiencyBoost * fluidLevel);
                    usedEmergencyCooling = true;
                }

                if (usedEmergencyCooling)
                    for (int i = 0; i < 4; i++) {
                        player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX, player.posY + 0.5, player.posZ, 0.0D, 0.0D, 0.0D);
                    }
            }
        }
    }

    public abstract double getCoolingFactor();

    public abstract double getCoolingBonus(@Nonnull ItemStack itemStack);

    public abstract double getEnergyConsumption(@Nonnull ItemStack itemStack);

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public abstract TextureAtlasSprite getIcon(ItemStack item);

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public abstract String getDataName();
}
