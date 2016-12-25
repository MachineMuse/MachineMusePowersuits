//package net.machinemuse.numina.item;
//
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.player.EntityPlayer;
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
//    // Item alone as an entity e.g. dropped
//    void renderEntity(ItemStack item, RenderBlocks renderBlocks, EntityItem entity);
//
//    // Inventory screen / toolbar
//    void renderInventory(ItemStack item, RenderBlocks renderBlocks);
//
//    // Maps only e.g. thaumometer
//    void renderFirstPersonMap(ItemStack item, EntityPlayer entity, TextureManager engine, MapData data);
//
//    // First person fist
//    void renderFirstPerson(ItemStack item, RenderBlocks renderBlocks, EntityLivingBase entity);
//
//    // Entity equipped in the world
//    void renderEquipped(ItemStack item, RenderBlocks renderBlocks, EntityLivingBase entity);
//}