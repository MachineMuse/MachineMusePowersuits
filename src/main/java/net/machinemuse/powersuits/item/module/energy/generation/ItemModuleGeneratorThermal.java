package net.machinemuse.powersuits.item.module.energy.generation;


import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 6:43 PM 4/23/13
 */
public class ItemModuleGeneratorThermal extends ItemAbstractPowerModule implements IPlayerTickModule {
    public ItemModuleGeneratorThermal(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_ENERGY_GENERATION);
        // Fixme: and maybe add options for a Magmatic Dynamo and maybe a stirling generator
//        if (ModCompatibility.isIndustrialCraftLoaded()) {
//            ModuleManager.INSTANCE.addInstallCost(getDataName(),ModCompatibility.getIC2Item("geothermalGenerator"));
        // <ic2:te:4> = geothermal generator

//            ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        } else {
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ironPlating, 1));
////        }
//
//        addBasePropertyDouble(MPSModuleConstants.THERMAL_ENERGY_GENERATION, 250);
//        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.THERMAL_ENERGY_GENERATION, 250, "RF");
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        double currentHeat = MuseHeatUtils.getPlayerHeat(player);
//        double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
//        if (player.world.getTotalWorldTime() % 20 == 0) {
//            if (player.isBurning()) {
//                ElectricItemUtils.givePlayerEnergy(player, (int) (4 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION)));
//            } else if (currentHeat >= 200) {
//                ElectricItemUtils.givePlayerEnergy(player, (int) (2 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION)));
//            } else if ((currentHeat / maxHeat) >= 0.5) {
//                ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION));
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}