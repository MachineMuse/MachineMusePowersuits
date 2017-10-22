package net.machinemuse.powersuits.client.events;

import net.machinemuse.powersuits.client.models.ModelLuxCapacitor;
//import net.machinemuse.powersuits.client.renderers.TinkerTableRenderer;
import net.machinemuse.powersuits.client.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.blocks.BlockTinkerTable;
import net.machinemuse.powersuits.common.events.EventRegisterItems;
import net.machinemuse.powersuits.common.items.old.ItemPowerFist;
import net.machinemuse.powersuits.common.tileentities.TileEntityTinkerTable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URL;

import static com.google.common.io.Resources.getResource;

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

//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuxCapacitor.class, new TileEntityLuxCapacitorRenderer());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.getInstance()), 0, ModelLuxCapacitor.modelResourceLocation);


        ModelLoader.setCustomModelResourceLocation(EventRegisterItems.powerTool, 0, new ModelResourceLocation(EventRegisterItems.powerTool.getRegistryName().toString()));




//        Item item;
//
//        item = Item.getItemFromBlock(BlockLuxCapacitor.getInstance());
//        for (int i = 0; i <= 6; ++i) {
//            ModelLoader.setCustomModelResourceLocation(item, i, ModelLuxCapacitor.modelResourceLocation);
//        }
//        ModelLoader.setCustomStateMapper(BlockLuxCapacitor.getInstance(), DirectionalBlockStateMapper.getInstance());



    }
}
