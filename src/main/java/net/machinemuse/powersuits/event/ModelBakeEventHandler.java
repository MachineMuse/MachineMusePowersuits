package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.client.render.model.LuxCapModelHelper;
import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.render.model.ModelPowerFist;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.proxy.ClientProxy;
import net.machinemuse.powersuits.item.DummyItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.net.URL;


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

//         temporary setup for loading the armor models until I can get them to load correctly manually
        Item dummies = MPSItems.dummies;
        IBakedModel armorModel;
        boolean success = true;
        if (dummies != null) {
            for (Integer  meta : ((DummyItem)dummies).modelLocations.keySet()) {
                ModelResourceLocation location = ((DummyItem)dummies).modelLocations.get(meta);
                armorModel= modelRegistry.getObject(location);
                if (armorModel instanceof OBJModel.OBJBakedModel) {
                    ((DummyItem)dummies).setModel(armorModel, location.getResourcePath());
                    System.out.println("model IS OBJBakedModel " + location.getResourcePath());
                } else {
                    success = false;
                    System.out.println("model NOT OBJBakedModel " + location.getVariant());
                    if (armorModel == null) System.out.println("model is NULL!!!: " + location.getResourcePath());
                }
            }
            if (success)
                loadArmorModels();
        }

        loadArmorModels();
    }

    private void storeLuxCapModel(EnumFacing facing) {
        ModelResourceLocation luxCapacitorLocation = luxCapHeler.getLocationForFacing(facing);
        IBakedModel modelIn = modelRegistry.getObject(luxCapacitorLocation);
        if (modelIn instanceof OBJModel.OBJBakedModel) {
            LuxCapModelHelper.getInstance().putLuxCapModels(luxCapacitorLocation, modelIn);
            modelRegistry.putObject(luxCapacitorLocation, new ModelLuxCapacitor(modelIn));
        }
    }

    private void loadArmorModels() {
        URL resource = ClientProxy.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(resource);
        URL otherResource = ClientProxy.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource);
    }
}