package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleWaterElectrolyzer extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {
    public ItemModuleWaterElectrolyzer(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
//        addBasePropertyDouble(MPSModuleConstants.WATERBREATHING_ENERGY_CONSUMPTION, 10000, "RF");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }


    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        int energy = ElectricItemUtils.getPlayerEnergy(player);
//        int energyConsumption = (int) Math.round(ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.WATERBREATHING_ENERGY_CONSUMPTION));
//        if (energy > energyConsumption && player.getAir() < 10) {
//
//            if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
//                Musique.playClientSound(SoundDictionary.SOUND_EVENT_ELECTROLYZER, SoundCategory.PLAYERS, 1.0f, player.getPosition());
//            }
//            ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
//            player.setAir(300);
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}