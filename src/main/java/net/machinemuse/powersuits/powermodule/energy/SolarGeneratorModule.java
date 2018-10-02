package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
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

public class SolarGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public SolarGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.solarPanel, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));

        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY, 15000);
        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT, 1500);
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 5, "pts");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
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
            boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0,1,0));
            boolean moonVisible = !world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0,1,0));
            if (!world.isRemote && !world.provider.hasSkyLight() && (world.getTotalWorldTime() % 80) == 0) {
                if (sunVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY));
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT));
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