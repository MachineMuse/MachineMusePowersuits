package net.machinemuse.powersuits.client.event;

import net.machinemuse.numina.utils.MuseLogger;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum ModelRegisterEventHandler {
    INSTANCE;


    @SubscribeEvent
    public void registerRenderers(ModelRegistryEvent event) {


        MuseLogger.logInfo("HELLO FROM MODEL REGISTRY EVENT");



    }

//    private void regRenderer(Item item) {
//        ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
//        ModelLoader..setCustomModelResourceLocation(item, 0, location);
//    }

}
