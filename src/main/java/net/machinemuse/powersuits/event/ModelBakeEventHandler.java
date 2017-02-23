package net.machinemuse.powersuits.event;

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

    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }
    private ModelBakeEventHandler() {}

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        modelRegistry = event.getModelRegistry();

        // Power Fist
        powerFistModel = new ModelPowerFist(modelRegistry.getObject(powerFistIconLocation));
        modelRegistry.putObject(powerFistIconLocation, powerFistModel);

        // Lux Capacitor as Item
        storeLuxCapModel(null);

        // Lux Capacitor as Blocks
        for (EnumFacing facing : EnumFacing.values()) {
            storeLuxCapModel(facing);
        }
    }

    private void storeLuxCapModel(EnumFacing facing) {
        ModelResourceLocation luxCapacitorLocation = luxCapHeler.getLocationForFacing(facing);
        IBakedModel modelIn = modelRegistry.getObject(luxCapacitorLocation);
        if (modelIn instanceof OBJModel.OBJBakedModel) {
            LuxCapModelHelper.getInstance().putLuxCapModels(luxCapacitorLocation, modelIn);
            modelRegistry.putObject(luxCapacitorLocation, new ModelLuxCapacitor(modelIn));
        }
    }
}