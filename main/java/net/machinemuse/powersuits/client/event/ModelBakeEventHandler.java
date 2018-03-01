package net.machinemuse.powersuits.client.event;

import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.model.ModelPowerFist;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
@SideOnly(Side.CLIENT)
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler INSTANCE;
    public static ModelBakeEventHandler getInstance() {
        if (INSTANCE == null) synchronized (ModelBakeEventHandler.class) {
            if (INSTANCE == null) INSTANCE = new ModelBakeEventHandler();
        }
        return INSTANCE;
    }
    private ModelBakeEventHandler() {
    }

    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    //FIXME there may only be one run. 2 runs not a guarantee
    private static boolean firstLoad = Boolean.parseBoolean(System.getProperty("fml.skipFirstModelBake", "true"));
    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(MPSResourceConstants.RESOURCE_PREFIX + "power_fist", "inventory");
    public static IBakedModel powerFistIconModel;

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        modelRegistry = event.getModelRegistry();

        // New Lux Capacitor Model
        event.getModelRegistry().putObject(ModelLuxCapacitor.modelResourceLocation, new ModelLuxCapacitor());

        for (EnumFacing facing : EnumFacing.VALUES) {
            modelRegistry.putObject(ModelLuxCapacitor.getModelResourceLocation(facing), new ModelLuxCapacitor());
        }

        // Power Fist
        powerFistIconModel = modelRegistry.getObject(powerFistIconLocation);
        modelRegistry.putObject(powerFistIconLocation, new ModelPowerFist(powerFistIconModel));


        // put this here because it might be fired late enough to actually work
        if (firstLoad) {
            firstLoad = false;
        } else {
//            ModelHelper.loadArmorModels(true);
        }

        MPSConfig.loadModelSpecs(null);
    }
}