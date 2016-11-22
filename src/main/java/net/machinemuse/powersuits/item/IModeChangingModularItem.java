//package net.machinemuse.powersuits.item;
//
//import net.machinemuse.numina.item.IModeChangingItem;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//
//
//import javax.annotation.Nullable;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 4:52 PM, 9/5/13
// *
// * Ported to Java by lehjr on 11/1/16.
// */
//public interface IModeChangingModularItem extends IModeChangingItem {
//    @Nullable
//    IIcon getModeIcon(String mode, ItemStack stack, EntityPlayer player);
//
//    List<String> getValidModes(ItemStack stack);
//
//    String getActiveMode(ItemStack stack);
//
//    void setActiveMode(ItemStack stack, String newMode) ;
//
//    void cycleMode(ItemStack stack, EntityPlayer player, int dMode);
//
//    String nextMode(ItemStack stack, EntityPlayer player);
//
//    String prevMode(ItemStack stack, EntityPlayer player);
//}