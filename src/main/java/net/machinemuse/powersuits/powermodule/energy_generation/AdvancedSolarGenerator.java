package net.machinemuse.powersuits.powermodule.energy_generation;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
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
        return EnumModuleCategory.CATEGORY_ENERGY_GENERATION;
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
            boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().up());
            boolean moonVisible = !world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().up());

            if (!world.isRemote && world.provider.hasSkyLight() && (world.getTotalWorldTime() % 80) == 0) {
                double lightLevelScaled = (world.getLightFor(EnumSkyBlock.SKY, player.getPosition().up()) - world.getSkylightSubtracted())/15D;

                if (sunVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY) * lightLevelScaled));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_HEAT_GENERATION_DAY) * lightLevelScaled / 2);
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT) * lightLevelScaled));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_HEAT_GENERATION_NIGHT) * lightLevelScaled / 2);
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