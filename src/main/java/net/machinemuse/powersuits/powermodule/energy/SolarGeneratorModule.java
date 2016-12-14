package net.machinemuse.powersuits.powermodule.energy;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class SolarGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public static final String MODULE_SOLAR_GENERATOR = "Solar Generator";
    public static final String SOLAR_ENERGY_GENERATION_DAY = "Daytime Energy Generation";
    public static final String SOLAR_ENERGY_GENERATION_NIGHT = "Nighttime Energy Generation";

    public SolarGeneratorModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(SOLAR_ENERGY_GENERATION_DAY, 1500);
        addBaseProperty(SOLAR_ENERGY_GENERATION_NIGHT, 150);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solarPanel, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    @Override
    public String getTextureFile() {
        return "solarhelmet";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_SOLAR_GENERATOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "solarGenerator";
    }

    @Override
    public String getDescription() {
        return "Let the sun power your adventures.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        ItemStack helmet = player.getCurrentArmor(3);
        if (helmet != null && helmet.equals(item)) {
            World world = player.worldObj;
            int xCoord = MathHelper.floor_double(player.posX);
            int zCoord = MathHelper.floor_double(player.posZ);
            boolean isRaining, canRain = true;
            if (world.getTotalWorldTime() % 20 == 0) {
                canRain = world.getWorldChunkManager().getBiomeGenAt(xCoord, zCoord).getIntRainfall() > 0;
            }

            isRaining = canRain && (world.isRaining() || world.isThundering());
            boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeTheSky(xCoord, MathHelper.floor_double(player.posY) + 1, zCoord);
            boolean moonVisible = !world.isDaytime() && !isRaining && world.canBlockSeeTheSky(xCoord, MathHelper.floor_double(player.posY) + 1, zCoord);
            if (!world.isRemote && !world.provider.hasNoSky && (world.getTotalWorldTime() % 80) == 0) {
                if (sunVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, SOLAR_ENERGY_GENERATION_DAY));
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, SOLAR_ENERGY_GENERATION_NIGHT));
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
