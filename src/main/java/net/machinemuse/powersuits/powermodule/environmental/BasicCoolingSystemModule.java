package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.capabilities.MPSChestPlateFluidHandler;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.modulehelpers.FluidUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.io.PrintStream;

public class BasicCoolingSystemModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public BasicCoolingSystemModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.ENDER_EYE, 4));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.COOLING_BONUS, 4, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION, 100, "RF/t");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1, "pts");
        addIntTradeoffProperty(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.SLOT_POINTS, 4, "m", 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME;
    }

    /**
     * @param player
     * @param itemStack this is the itemstack being ticked.
     */
    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        double currentHeat = MuseHeatUtils.getPlayerHeat(player);
        double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
        FluidUtils fluidUtils = new FluidUtils(player, itemStack, this.getDataName());
        double fluidEfficiencyBoost = fluidUtils.getCoolingEfficiency();

        // if not overheating
        if (currentHeat < maxHeat) {
            double coolJoules = (fluidEfficiencyBoost + ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.COOLING_BONUS)) * 0.1;
            if (ElectricItemUtils.getPlayerEnergy(player) > coolJoules) {
                coolJoules = MuseHeatUtils.coolPlayer(player, coolJoules);
                ElectricItemUtils.drainPlayerEnergy(player,
                        (int) (coolJoules * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION)));
            }

        // sacrificial emergency cooling
        } else {
            // how much player is overheating
            double overheatAmount = currentHeat - maxHeat;

            int fluidLevel = fluidUtils.getFluidLevel();

            // if system has enough fluid using this "very special" formula
            if (fluidLevel >= (int) (fluidEfficiencyBoost * overheatAmount)) {
                fluidUtils.drain((int) (fluidEfficiencyBoost * overheatAmount));
                MuseHeatUtils.coolPlayer(player, overheatAmount + 1);

            // sacrifice whatever fluid is in the system
            } else {
                fluidUtils.drain(fluidLevel);
                MuseHeatUtils.coolPlayer(player, fluidEfficiencyBoost * fluidLevel);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicCoolingSystem;
    }
}