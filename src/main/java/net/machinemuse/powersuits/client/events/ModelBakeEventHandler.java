package net.machinemuse.powersuits.client.events;

import net.machinemuse.powersuits.client.models.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.models.ModelPowerFist;
import net.machinemuse.powersuits.common.events.EventRegisterItems;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

import static net.machinemuse.powersuits.common.MPSConstants.RESOURCE_PREFIX;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
@SideOnly(Side.CLIENT)
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler ourInstance = new ModelBakeEventHandler();
    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    //    LuxCapModelHelper luxCapHeler = LuxCapModelHelper.getInstance();
    //FIXME there may only be one run. 2 runs not a guarantee
    private static boolean firstLoad = Boolean.parseBoolean(System.getProperty("fml.skipFirstModelBake", "true"));
    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(RESOURCE_PREFIX + "powerTool", "inventory");
//    public static ModelPowerFist powerFistModel;

    // Armor icons
    public static final ModelResourceLocation powerArmorHeadModelLocation = new ModelResourceLocation(EventRegisterItems.getInstance().powerArmorHead.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorChestModelLocation = new ModelResourceLocation(EventRegisterItems.getInstance().powerArmorTorso.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorLegsModelLocation = new ModelResourceLocation(EventRegisterItems.getInstance().powerArmorLegs.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorFeetModelLocation = new ModelResourceLocation(EventRegisterItems.getInstance().powerArmorFeet.getRegistryName(), "inventory");


//    public static final ModelResourceLocation  tinkerTable2Location = new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.testBlock", "inventory");



    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }

    private ModelBakeEventHandler() {
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {



        event.getModelRegistry().putObject(ModelLuxCapacitor.modelResourceLocation, new ModelLuxCapacitor());

        for (EnumFacing facing : EnumFacing.VALUES) {
            event.getModelRegistry().putObject(ModelLuxCapacitor.getModelResourceLocation(facing), new ModelLuxCapacitor());
        }

        for(ModelResourceLocation resLoc : event.getModelRegistry().getKeys()) {
            if (resLoc.getResourcePath().contains("powersuits"))
                System.out.println(resLoc.toString());
        }

        IBakedModel powerFistIcon = event.getModelRegistry().getObject(ModelPowerFist.modelResourceLocation);
        event.getModelRegistry().putObject(ModelPowerFist.modelResourceLocation, new ModelPowerFist(powerFistIcon));




//        modelRegistry = event.getModelRegistry();
//
////         Power Fist
//        powerFistModel = new ModelPowerFist(modelRegistry.getObject(powerFistIconLocation));
//        modelRegistry.putObject(powerFistIconLocation, powerFistModel);
//
//        // Lux Capacitor as Item
//        storeLuxCapModel(null);
//
//        // Lux Capacitor as Blocks
//        for (EnumFacing facing : EnumFacing.values()) {
//            storeLuxCapModel(facing);
//        }
//
//        // set up armor icon models for coloring because that's how it used to work
//        IBakedModel powerArmorHeadModel = modelRegistry.getObject(powerArmorHeadModelLocation);
//        IBakedModel powerArmorChestModel = modelRegistry.getObject(powerArmorChestModelLocation);
//        IBakedModel powerArmorLegsModel = modelRegistry.getObject(powerArmorLegsModelLocation);
//        IBakedModel powerArmorFeetModel = modelRegistry.getObject(powerArmorFeetModelLocation);
//
//        IBakedModel powerArmorIconModel = new ArmorIcon(powerArmorHeadModel,
//                                                        powerArmorChestModel,
//                                                        powerArmorLegsModel,
//                                                        powerArmorFeetModel);
//
//        modelRegistry.putObject(powerArmorHeadModelLocation, powerArmorIconModel);
//        modelRegistry.putObject(powerArmorChestModelLocation, powerArmorIconModel);
//        modelRegistry.putObject(powerArmorLegsModelLocation, powerArmorIconModel);
//        modelRegistry.putObject(powerArmorFeetModelLocation, powerArmorIconModel);
//
////        IBakedModel tinkerTableItem = modelRegistry.getObject(tinkerTable2Location);
////        modelRegistry.putObject(tinkerTable2Location, new ModelTinkerTable2(tinkerTableItem));
////
////        for (EnumFacing facing : EnumFacing.values()) {
////            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
////                ModelResourceLocation tinkerTableLocation =  new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.testBlock", "facing=" + facing.getName());
////                tinkerTableItem = modelRegistry.getObject(tinkerTableLocation);
////                modelRegistry.putObject(tinkerTableLocation, new ModelTinkerTable2(tinkerTableItem));
////            }
////        }
//
//
//        // put this here because it might be fired late enough to actually work
//        if (firstLoad) {
//            firstLoad = false;
//        } else {
//            ModelHelper.loadArmorModels(true);
//        }
//    }
//
//    private void storeLuxCapModel(EnumFacing facing) {
//        ModelResourceLocation luxCapacitorLocation = luxCapHeler.getLocationForFacing(facing);
//        IBakedModel modelIn = modelRegistry.getObject(luxCapacitorLocation);
//        if (modelIn instanceof OBJModelPlus.OBJBakedModelPus) {
//            LuxCapModelHelper.getInstance().putLuxCapModels(facing, modelIn);
//            modelRegistry.putObject(luxCapacitorLocation, new ModelLuxCapacitorOLD(modelIn));
//        }
    }
}
