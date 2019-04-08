package net.machinemuse.powersuits.item.module.special;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleInvisibility extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {
//    private final Potion invisibility = Potion.getPotionFromResourceLocation("invisibility");

    public ItemModuleInvisibility(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_SPECIAL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 4));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
//        PotionEffect invis = null;
//        if (player.isPotionActive(invisibility)) {
//            invis = player.getActivePotionEffect(invisibility);
//        }
//        if (50 < totalEnergy) {
//            if (invis == null || invis.getDuration() < 210) {
//                player.addPotionEffect(new PotionEffect(invisibility, 500, -3, false, false));
//                ElectricItemUtils.drainPlayerEnergy(player, 50);
//            }
//        } else {
//            onPlayerTickInactive(player, item);
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        PotionEffect invis = null;
//        if (player.isPotionActive(invisibility)) {
//            invis = player.getActivePotionEffect(invisibility);
//        }
//        if (invis != null && invis.getAmplifier() == -3) {
//            if (player.world.isRemote) {
//                player.removeActivePotionEffect(invisibility);
//            } else {
//                player.removePotionEffect(invisibility);
//            }
//        }
    }
}