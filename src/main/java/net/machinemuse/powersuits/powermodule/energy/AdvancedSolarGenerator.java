package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Eximius88 on 1/12/14.
 */
public class AdvancedSolarGenerator extends PowerModuleBase implements IPlayerTickModule {
    public AdvancedSolarGenerator(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solarPanel, 3));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.computerChip, 1));

        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY, 45000, "RF");
        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT, 1500, "RF");
        addBasePropertyDouble(MPSModuleConstants.SOLAR_HEAT_GENERATION_DAY, 15);
        addBasePropertyDouble(MPSModuleConstants.SOLAR_HEAT_GENERATION_NIGHT, 5);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemPowerArmorHelmet) {
            World world = player.world;
            boolean isRaining, canRain = true;
            if (world.getTotalWorldTime() % 20 == 0) {
                canRain = world.getBiome(player.getPosition()).canRain();
            }
            isRaining = canRain && (world.isRaining() || world.isThundering());
            boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0, 1, 0));
            boolean moonVisible = !world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0, 1, 0));
            if (!world.isRemote && !world.provider.hasSkyLight() && (world.getTotalWorldTime() % 80) == 0) {
                if (sunVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_HEAT_GENERATION_DAY) / 2);
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_HEAT_GENERATION_NIGHT) / 2);
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.advSolarGenerator;
    }
}