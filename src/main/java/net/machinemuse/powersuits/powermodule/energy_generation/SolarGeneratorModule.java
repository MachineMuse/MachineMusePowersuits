package net.machinemuse.powersuits.powermodule.energy_generation;

import net.machinemuse.numina.energy.ElectricItemUtils;
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

public class SolarGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public SolarGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solarPanel, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));

        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY, 15000);
        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT, 1500);

    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY_GENERATION;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_SOLAR_GENERATOR__DATANAME;
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
            if (!world.isRemote && world.provider.hasSkyLight() && (world.getTotalWorldTime() % 80) == 0) {
                double lightLevelScaled = (world.getLightFor(EnumSkyBlock.SKY, player.getPosition().up()) - world.getSkylightSubtracted())/15D;
                if (sunVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY) * lightLevelScaled));
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT) * lightLevelScaled));
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.solarGenerator;
    }
}