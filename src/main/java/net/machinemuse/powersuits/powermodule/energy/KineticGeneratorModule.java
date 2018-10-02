package net.machinemuse.powersuits.powermodule.energy;


import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KineticGeneratorModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {


    public KineticGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_GENERATION, 2000);
        addTradeoffPropertyDouble("Energy Generated", MPSModuleConstants.KINETIC_ENERGY_GENERATION, 6000, "RF");
        addBasePropertyDouble(MPSModuleConstants.KINETIC_HEAT_GENERATION, 5);
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty("Energy Generated", MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
  }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (!player.isAirBorne) {
            NBTTagCompound tag = MuseNBTUtils.getMuseItemTag(item);
            boolean isNotWalking = (player.getRidingEntity() != null) || (player.isInWater());
            if ((!tag.hasKey("x")) || (isNotWalking))
                tag.setInteger("x", (int) player.posX);
            if ((!tag.hasKey("z")) || (isNotWalking))
                tag.setInteger("z", (int) player.posZ);
            double distance = Math.sqrt((tag.getInteger("x") - (int) player.posX) * (tag.getInteger("x") - (int) player.posX) + (tag.getInteger("z") - (int) player.posZ) * (tag.getInteger("z") - (int) player.posZ));
            if (distance >= 5.0) {
                tag.setInteger("x", (int) player.posX);
                tag.setInteger("z", (int) player.posZ);
                if (player.isSprinting()) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_ENERGY_GENERATION));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_HEAT_GENERATION));
                } else {
                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_ENERGY_GENERATION) / 2));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_HEAT_GENERATION) / 2);
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.kineticGenerator;
    }
}