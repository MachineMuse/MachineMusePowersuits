package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Eximius88 on 1/12/14.
 */
public class AdvancedSolarGenerator extends PowerModuleBase implements IPlayerTickModule {
    public static final String MODULE_ADVANCED_SOLAR_GENERATOR = "Advanced Solar Generator";
    public static final String A_SOLAR_ENERGY_GENERATION_DAY = "Daytime Energy Generation";
    public static final String A_SOLAR_ENERGY_GENERATION_NIGHT = "Nighttime Energy Generation";
    public static final String SOLAR_HEAT_GENERATION_DAY = "Daytime Heat Generation";
    public static final String SOLAR_HEAT_GENERATION_NIGHT = "Nighttime Heat Generation";

    public AdvancedSolarGenerator(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(SOLAR_HEAT_GENERATION_DAY, 15);
        addBaseProperty(SOLAR_HEAT_GENERATION_NIGHT, 5);
        addBaseProperty(A_SOLAR_ENERGY_GENERATION_DAY, 4500);
        addBaseProperty(A_SOLAR_ENERGY_GENERATION_NIGHT, 150);
        addBaseProperty(MuseCommonStrings.WEIGHT, 300);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solarPanel, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.computerChip, 1));
    }

    @Override
    public String getTextureFile() {
        return "advsolarhelmet";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_ADVANCED_SOLAR_GENERATOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "advSolarGenerator";
    }

    @Override
    public String getDescription() {
        return "A solar generator with 3 times the power generation of the standard solar generator";
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
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, A_SOLAR_ENERGY_GENERATION_DAY));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, SOLAR_HEAT_GENERATION_DAY) / 2);
                } else if (moonVisible) {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, A_SOLAR_ENERGY_GENERATION_NIGHT));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, SOLAR_HEAT_GENERATION_NIGHT) / 2);
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
