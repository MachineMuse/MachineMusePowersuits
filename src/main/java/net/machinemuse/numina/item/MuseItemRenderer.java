//package net.machinemuse.numina.item;
//
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.entities.EntityLivingBase;
//import net.minecraft.entities.item.EntityItem;
//import net.minecraft.entities.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.storage.MapData;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 8:03 PM, 6/28/13
// *
// * Ported to Java by lehjr on 11/2/16.
// */
//public interface MuseItemRenderer {
//    // Implicit delegate; allows use of a MuseItemRenderer in the place of an IItemRenderer
//    ItemRenderDelegator delegator();
//
//    // Item alone as an entities e.g. dropped
//    void renderEntity(ItemStack item, RenderBlocks renderBlocks, EntityItem entities);
//
//    // Inventory screen / toolbar
//    void renderInventory(ItemStack item, RenderBlocks renderBlocks);
//
//    // Maps only e.g. thaumometer
//    void renderFirstPersonMap(ItemStack item, EntityPlayer entities, TextureManager engine, MapData data);
//
//    // First person fist
//    void renderFirstPerson(ItemStack item, RenderBlocks renderBlocks, EntityLivingBase entities);
//
//    // Entity equipped in the world
//    void renderEquipped(ItemStack item, RenderBlocks renderBlocks, EntityLivingBase entities);
//}
