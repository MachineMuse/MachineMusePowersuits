//package net.machinemuse.api;
//
//import cpw.mods.fml.common.Optional;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 6:54 AM, 4/28/13
// *
// * Ported to Java by lehjr on 11/4/16.
// */
//@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "Forestry", striprefs = true)
//public class ApiaristArmor implements IApiaristArmor {
//    private static ApiaristArmor INSTANCE;
//
//    public static ApiaristArmor getInstance() {
//        if (INSTANCE == null)
//            INSTANCE = new ApiaristArmor();
//        return INSTANCE;
//    }
//
//
//    @Optional.Method(modid = "Forestry")
//    @Override
//    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
//        return false;
//    }
//
//    @Optional.Method(modid = "Forestry")
//    @Override
//    public boolean protectEntity(EntityLivingBase player, ItemStack armor, String cause, boolean doProtect) {
//        return false;
//    }
//}
