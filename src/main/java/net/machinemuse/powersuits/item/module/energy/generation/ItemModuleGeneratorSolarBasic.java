package net.machinemuse.powersuits.item.module.energy.generation;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleGeneratorSolarBasic extends ItemAbstractPowerModule implements IPlayerTickModule {
    public ItemModuleGeneratorSolarBasic(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_ENERGY_GENERATION);
//        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY, 15000);
//        addBasePropertyDouble(MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT, 1500);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemPowerArmorHelmet) {
//            World world = player.world;
//            boolean isRaining, canRain = true;
//            if (world.getTotalWorldTime() % 20 == 0) {
//                canRain = world.getBiome(player.getPosition()).canRain();
//            }
//
//            isRaining = canRain && (world.isRaining() || world.isThundering());
//            boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0, 1, 0));
//            boolean moonVisible = !world.isDaytime() && !isRaining && world.canBlockSeeSky(player.getPosition().add(0, 1, 0));
//            if (!world.isRemote && world.provider.hasSkyLight() && (world.getTotalWorldTime() % 80) == 0) {
//                double lightLevelScaled = (world.getLightFor(EnumSkyBlock.SKY, player.getPosition().up()) - world.getSkylightSubtracted())/15D;
//                if (sunVisible) {
//                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_DAY) * lightLevelScaled));
//                } else if (moonVisible) {
//                    ElectricItemUtils.givePlayerEnergy(player, (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SOLAR_ENERGY_GENERATION_NIGHT) * lightLevelScaled));
//                }
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}