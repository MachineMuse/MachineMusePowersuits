package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.render.model.ModelPowerFist;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

/**
 * Ported to Java by lehjr on 12/22/16.
 */
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler ourInstance = new ModelBakeEventHandler();

    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }

    private ModelBakeEventHandler() {
    }


    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException
    {
        ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerTool", "inventory");
        ModelResourceLocation luxCapacitorLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.luxCapacitor", "inventory");



        Object obj = event.getModelRegistry().getObject(powerFistIconLocation);




        ModelPowerFist powerFistModel = new ModelPowerFist(event.getModelRegistry().getObject(powerFistIconLocation));
        event.getModelRegistry().putObject(powerFistIconLocation, powerFistModel);

        ModelLuxCapacitor luxCapacitorModel = new ModelLuxCapacitor(event.getModelRegistry().getObject(luxCapacitorLocation), 0);



    }



}
