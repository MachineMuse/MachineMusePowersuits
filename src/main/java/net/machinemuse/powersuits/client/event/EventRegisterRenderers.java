package net.machinemuse.powersuits.client.event;

import net.machinemuse.powersuits.client.models.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.renderers.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.renderers.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.renderers.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.common.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.common.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.common.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.common.event.EventRegisterItems;
import net.machinemuse.powersuits.common.tileentities.TileEntityTinkerTable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//import net.machinemuse.powersuits.client.renderers.TinkerTableRenderer;

@SideOnly(Side.CLIENT)
public class EventRegisterRenderers {
    private static EventRegisterRenderers ourInstance;

    public static EventRegisterRenderers getInstance() {
        if (ourInstance == null)
            ourInstance = new EventRegisterRenderers();
        return ourInstance;
    }

    @SubscribeEvent
    public void registerRenderers(ModelRegistryEvent event) {


//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, new TinkerTableRenderer());

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockTinkerTable.getInstance()), 0, new ModelResourceLocation(BlockTinkerTable.getInstance().getRegistryName(), "normal"));

        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerArmorHead, 0, new ModelResourceLocation(EventRegisterItems.powerArmorHead.getRegistryName().toString()));
        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerArmorTorso, 0, new ModelResourceLocation(EventRegisterItems.powerArmorTorso.getRegistryName().toString()));
        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerArmorLegs, 0, new ModelResourceLocation(EventRegisterItems.powerArmorLegs.getRegistryName().toString()));
        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerArmorFeet, 0, new ModelResourceLocation(EventRegisterItems.powerArmorFeet.getRegistryName().toString()));




//        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));



        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockTinkerTable.getInstance()), 0, TileEntityTinkerTable.class);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.getInstance()), 0, ModelLuxCapacitor.modelResourceLocation);


        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerTool, 0, new ModelResourceLocation(EventRegisterItems.powerTool.getRegistryName().toString()));

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);


//        Item item;
//
//        item = Item.getItemFromBlock(BlockLuxCapacitor.getInstance());
//        for (int i = 0; i <= 6; ++i) {
//            ModelLoader.setCustomModelResourceLocation(item, i, ModelLuxCapacitor.modelResourceLocation);
//        }
//        ModelLoader.setCustomStateMapper(BlockLuxCapacitor.getInstance(), DirectionalBlockStateMapper.getInstance());



    }
}
