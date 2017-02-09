package net.machinemuse.powersuits.event;

//import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;

import net.machinemuse.powersuits.client.render.model.LuxCapModelHelper;
import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.render.model.ModelPowerFist;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler ourInstance = new ModelBakeEventHandler();
    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    LuxCapModelHelper luxCapHeler = LuxCapModelHelper.getInstance();


    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerTool", "inventory");
    public static ModelPowerFist powerFistModel;

    IBakedModel luxCapacitorModel;

    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }

    private ModelBakeEventHandler() {
    }

    public IRegistry<ModelResourceLocation, IBakedModel> getModelRegistry(){
        return modelRegistry;
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        modelRegistry = event.getModelRegistry();
        powerFistModel = new ModelPowerFist(modelRegistry.getObject(powerFistIconLocation));
        modelRegistry.putObject(powerFistIconLocation, powerFistModel);

        luxCapacitorModel = modelRegistry.getObject(LuxCapModelHelper.getInstance().luxCapItemLocation);
        storeModel(luxCapHeler.luxCapItemLocation, luxCapacitorModel);

        ModelResourceLocation luxCapacitorLocation;
        for (EnumFacing facing : EnumFacing.values()) {
            luxCapacitorLocation = luxCapHeler.getLocationForFacing(facing);
            luxCapacitorModel = modelRegistry.getObject(luxCapacitorLocation);
            storeModel(luxCapacitorLocation, luxCapacitorModel);
        }
    }

    private void storeModel(ModelResourceLocation locationIn, IBakedModel modelIn) {
        if (modelIn instanceof OBJModel.OBJBakedModel) {
            LuxCapModelHelper.getInstance().putLuxCapFameModels(locationIn, modelIn);
            modelRegistry.putObject(locationIn, new ModelLuxCapacitor(modelIn));
        }
    }
}