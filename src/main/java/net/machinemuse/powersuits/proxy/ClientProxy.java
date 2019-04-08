package net.machinemuse.powersuits.proxy;

import net.machinemuse.numina.client.model.obj.MuseOBJLoader;
import net.machinemuse.powersuits.basemod.ModularPowersuits;
import net.machinemuse.powersuits.client.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.client.event.RenderEventHandler;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void setupClient(FMLClientSetupEvent event) {
//        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.INSTANCE);
//        OBJLoader.INSTANCE.addDomain(ModularPowersuits.MODID.toLowerCase());


        ModelLoaderRegistry.registerLoader(MuseOBJLoader.INSTANCE);
        MuseOBJLoader.INSTANCE.addDomain(ModularPowersuits.MODID.toLowerCase());

//        MinecraftForge.EVENT_BUS.register(ModelRegisterEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.INSTANCE);


        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);

    }
}
