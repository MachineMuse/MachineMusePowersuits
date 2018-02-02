//package net.machinemuse.api;
//
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.common.Optional;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 6:54 AM, 4/28/13
// *
// * Ported to Java by lehjr on 11/4/16.
// */
//@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "forestry", striprefs = true)
//public class ApiaristArmor implements IApiaristArmor {
//    private static ApiaristArmor INSTANCE;
//
//    public static ApiaristArmor getInstance() {
//        if (INSTANCE == null)
//            INSTANCE = new ApiaristArmor();
//        return INSTANCE;
//    }
//
//    @Optional.Method(modid = "forestry")
//    @Override
//    public boolean protectEntity(EntityLivingBase player, ItemStack armor, String cause, boolean doProtect) {
//        return false;
//    }
//}
