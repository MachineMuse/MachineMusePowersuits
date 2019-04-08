package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class ItemAbstractCoolingSystemModule extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {

    public ItemAbstractCoolingSystemModule(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        if (!player.world.isRemote) {
//            double currentHeat = MuseHeatUtils.getPlayerHeat(player);
//            if (currentHeat <= 0)
//                return;
//
//            double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
//            FluidUtils fluidUtils = new FluidUtils(player, itemStack, this.getDataName());
//            double fluidEfficiencyBoost = fluidUtils.getCoolingEfficiency();
//
//            // if not overheating
//            if (currentHeat < maxHeat) {
//                double coolJoules = (fluidEfficiencyBoost + getCoolingBonus(itemStack)) * getCoolingFactor();
////                System.out.println("cool joules: " + coolJoules);
//
//                if (ElectricItemUtils.getPlayerEnergy(player) > coolJoules) {
//
////                    System.out.println("cooling normally");
//
//                    coolJoules = MuseHeatUtils.coolPlayer(player, coolJoules);
//
//                    ElectricItemUtils.drainPlayerEnergy(player,
//                            (int) (coolJoules * getEnergyConsumption(itemStack)));
//                }
//
//                // sacrificial emergency cooling
//            } else {
//                // how much player is overheating
//                double overheatAmount = currentHeat - maxHeat;
//
//                int fluidLevel = fluidUtils.getFluidLevel();
//
//                boolean usedEmergencyCooling = false;
//                // if system has enough fluid using this "very special" formula
//                if (fluidLevel >= (int) (fluidEfficiencyBoost * overheatAmount)) {
//                    fluidUtils.drain((int) (fluidEfficiencyBoost * overheatAmount));
//                    MuseHeatUtils.coolPlayer(player, overheatAmount + 1);
//                    usedEmergencyCooling = true;
//
//                    // sacrifice whatever fluid is in the system
//                } else if (fluidLevel > 0) {
//                    fluidUtils.drain(fluidLevel);
//                    MuseHeatUtils.coolPlayer(player, fluidEfficiencyBoost * fluidLevel);
//                    usedEmergencyCooling = true;
//                }
//
//                if (usedEmergencyCooling)
//                    for (int i = 0; i < 4; i++) {
//                        player.world.addParticle(Particles.SMOKE, player.posX, player.posY + 0.5, player.posZ, 0.0D, 0.0D, 0.0D);
//                    }
//            }
//        }
    }

    public abstract double getCoolingFactor();

    public abstract double getCoolingBonus(@Nonnull ItemStack itemStack);

    public abstract double getEnergyConsumption(@Nonnull ItemStack itemStack);

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
