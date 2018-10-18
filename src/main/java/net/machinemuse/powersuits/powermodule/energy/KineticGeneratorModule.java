package net.machinemuse.powersuits.powermodule.energy;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class KineticGeneratorModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_KINETIC_GENERATOR = "Kinetic Generator";
    public static final String KINETIC_ENERGY_GENERATION = "Energy Per 5 Blocks";
    public static final String KINETIC_HEAT_GENERATION = "Heat Generation";

    public KineticGeneratorModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(KINETIC_HEAT_GENERATION, 5);
        addBaseProperty(MuseCommonStrings.WEIGHT, 1000);
        addBaseProperty(KINETIC_ENERGY_GENERATION, 200);
        addTradeoffProperty("Energy Generated", KINETIC_ENERGY_GENERATION, 600, " Joules");
        addTradeoffProperty("Energy Generated", MuseCommonStrings.WEIGHT, 3000, "g");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getTextureFile() {
        return "kineticgen";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_KINETIC_GENERATOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "kineticGenerator";
    }

    @Override
    public String getDescription() {
        return "Generate power with your movement.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (!player.isAirBorne) {
            NBTTagCompound tag = MuseItemUtils.getMuseItemTag(item);
            boolean isNotWalking = (player.ridingEntity != null) || (player.isInWater());
            if ((!tag.hasKey("x")) || (isNotWalking))
                tag.setInteger("x", (int) player.posX);
            if ((!tag.hasKey("z")) || (isNotWalking))
                tag.setInteger("z", (int) player.posZ);
            double distance = Math.sqrt((tag.getInteger("x") - (int) player.posX) * (tag.getInteger("x") - (int) player.posX) + (tag.getInteger("z") - (int) player.posZ) * (tag.getInteger("z") - (int) player.posZ));
            if (distance >= 5.0) {
                tag.setInteger("x", (int) player.posX);
                tag.setInteger("z", (int) player.posZ);
                if (player.isSprinting()) {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, KINETIC_ENERGY_GENERATION));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, KINETIC_HEAT_GENERATION));
                } else {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, KINETIC_ENERGY_GENERATION) / 2);
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, KINETIC_HEAT_GENERATION) / 2);
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
