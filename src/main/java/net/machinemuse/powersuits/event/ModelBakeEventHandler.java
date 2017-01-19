package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.render.model.ModelPowerFist;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

/**
 * Ported to Java by lehjr on 12/22/16.
 */
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler ourInstance = new ModelBakeEventHandler();
    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;

    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }

    private ModelBakeEventHandler() {
    }

    public IRegistry<ModelResourceLocation, IBakedModel> getModelRegistry(){
        return modelRegistry;
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException
    {
        modelRegistry = event.getModelRegistry();

        ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerTool", "inventory");
        ModelPowerFist powerFistModel = new ModelPowerFist(modelRegistry.getObject(powerFistIconLocation));
        modelRegistry.putObject(powerFistIconLocation, powerFistModel);

        ModelResourceLocation luxCapacitorLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.luxCapacitor", "inventory");
        ModelLuxCapacitor luxCapacitorModel = new ModelLuxCapacitor(modelRegistry.getObject(luxCapacitorLocation), 0);
        modelRegistry.putObject(luxCapacitorLocation, luxCapacitorModel);


    }
}
