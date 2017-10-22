//package net.machinemuse.numina.item;
//
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.storage.MapData;
//import net.minecraftforge.client.IItemRenderer;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 7:48 PM, 6/28/13
// */
//public class ItemRenderDelegator implements IItemRenderer {
//    public final MuseItemRenderer renderer;
//
//    public ItemRenderDelegator(MuseItemRenderer renderer) {
//        this.renderer = renderer;
//    }
//
//    @Override
//    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//        return true;
//    }
//
//    @Override
//    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
//        return true;
//    }
//
//    @Override
//    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
//        switch (type) {
//            case ENTITY:
//                RenderBlocks renderEntity = (RenderBlocks) data[0];
//                EntityItem entityEntity = (EntityItem) data[1];
//                renderer.renderEntity(item, renderEntity, entityEntity);
//                break;
//            case INVENTORY:
//                RenderBlocks renderInventory = (RenderBlocks) data[0];
//                renderer.renderInventory(item, renderInventory);
//                break;
//            case EQUIPPED:
//                RenderBlocks renderEquipped = (RenderBlocks) data[0];
//                EntityLivingBase entityEquipped = (EntityLivingBase) data[1];
//                renderer.renderEquipped(item, renderEquipped, entityEquipped);
//                break;
//            case FIRST_PERSON_MAP:
//                EntityPlayer playerFirstPerson = (EntityPlayer) data[0];
//                TextureManager engineFirstPerson = (TextureManager) data[1];
//                MapData mapDataFirstPerson = (MapData) data[2];
//                renderer.renderFirstPersonMap(item, playerFirstPerson, engineFirstPerson, mapDataFirstPerson);
//                break;
//            case EQUIPPED_FIRST_PERSON:
//                RenderBlocks renderEquFP = (RenderBlocks) data[0];
//                EntityLivingBase entityEquFP = (EntityLivingBase) data[1];
//                renderer.renderFirstPerson(item, renderEquFP, entityEquFP);
//                break;
//            default:
//        }
//    }
//}