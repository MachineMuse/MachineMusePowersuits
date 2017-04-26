package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.client.render.model.*;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
@SideOnly(Side.CLIENT)
public class ModelBakeEventHandler {
    private static ModelBakeEventHandler ourInstance = new ModelBakeEventHandler();
    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    LuxCapModelHelper luxCapHeler = LuxCapModelHelper.getInstance();
    //FIXME there may only be one run. 2 runs not a guarantee
    private static boolean firstLoad = Boolean.parseBoolean(System.getProperty("fml.skipFirstModelBake", "true"));
    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerTool", "inventory");
    public static ModelPowerFist powerFistModel;

    // Armor icons
    public static final ModelResourceLocation powerArmorHeadModelLocation = new ModelResourceLocation(MPSItems.powerArmorHead.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorChestModelLocation = new ModelResourceLocation(MPSItems.powerArmorTorso.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorLegsModelLocation = new ModelResourceLocation(MPSItems.powerArmorLegs.getRegistryName(), "inventory");
    public static final ModelResourceLocation powerArmorFeetModelLocation = new ModelResourceLocation(MPSItems.powerArmorFeet.getRegistryName(), "inventory");


    public static final ModelResourceLocation  tinkerTable2Location = new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.testBlock", "inventory");



    public static ModelBakeEventHandler getInstance() {
        return ourInstance;
    }

    private ModelBakeEventHandler() {
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        modelRegistry = event.getModelRegistry();

//         Power Fist
        powerFistModel = new ModelPowerFist(modelRegistry.getObject(powerFistIconLocation));
        modelRegistry.putObject(powerFistIconLocation, powerFistModel);

        // Lux Capacitor as Item
        storeLuxCapModel(null);

        // Lux Capacitor as Blocks
        for (EnumFacing facing : EnumFacing.values()) {
            storeLuxCapModel(facing);
        }

        // set up armor icon models for coloring because that's how it used to work
        IBakedModel powerArmorHeadModel = modelRegistry.getObject(powerArmorHeadModelLocation);
        IBakedModel powerArmorChestModel = modelRegistry.getObject(powerArmorChestModelLocation);
        IBakedModel powerArmorLegsModel = modelRegistry.getObject(powerArmorLegsModelLocation);
        IBakedModel powerArmorFeetModel = modelRegistry.getObject(powerArmorFeetModelLocation);

        IBakedModel powerArmorIconModel = new ArmorIcon(powerArmorHeadModel,
                                                        powerArmorChestModel,
                                                        powerArmorLegsModel,
                                                        powerArmorFeetModel);

        modelRegistry.putObject(powerArmorHeadModelLocation, powerArmorIconModel);
        modelRegistry.putObject(powerArmorChestModelLocation, powerArmorIconModel);
        modelRegistry.putObject(powerArmorLegsModelLocation, powerArmorIconModel);
        modelRegistry.putObject(powerArmorFeetModelLocation, powerArmorIconModel);

        IBakedModel tinkerTableItem = modelRegistry.getObject(tinkerTable2Location);
        modelRegistry.putObject(tinkerTable2Location, new ModelTinkerTable2(tinkerTableItem));

        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                ModelResourceLocation tinkerTableLocation =  new ModelResourceLocation(Config.RESOURCE_PREFIX + "tile.testBlock", "facing=" + facing.getName());
                tinkerTableItem = modelRegistry.getObject(tinkerTableLocation);
                modelRegistry.putObject(tinkerTableLocation, new ModelTinkerTable2(tinkerTableItem));
            }
        }









//        setupArmorIcon(MPSItems.powerArmorHead, modelRegistry);
//        setupArmorIcon(MPSItems.powerArmorTorso, modelRegistry);
//        setupArmorIcon(MPSItems.powerArmorLegs, modelRegistry);
//        setupArmorIcon(MPSItems.powerArmorFeet, modelRegistry);

        // put this here because it might be fired late enough to actually work
        if (firstLoad) {
            firstLoad = false;
        } else {
            ModelHelper.loadArmorModels(true);
        }
    }

    private void storeLuxCapModel(EnumFacing facing) {
        ModelResourceLocation luxCapacitorLocation = luxCapHeler.getLocationForFacing(facing);
        IBakedModel modelIn = modelRegistry.getObject(luxCapacitorLocation);
        if (modelIn instanceof OBJModel.OBJBakedModel) {
            LuxCapModelHelper.getInstance().putLuxCapModels(facing, modelIn);
            modelRegistry.putObject(luxCapacitorLocation, new ModelLuxCapacitor(modelIn));
        }
    }

    public void setupArmorIcon(Item itemIn, IRegistry<ModelResourceLocation, IBakedModel> modelRegistryIn) {
//        ModelResourceLocation armorIconLocation = new ModelResourceLocation(itemIn.getRegistryName(), "inventory");
//        IBakedModel iconModel = modelRegistryIn.getObject(armorIconLocation);
//        modelRegistryIn.putObject(armorIconLocation, new ArmorIcon(iconModel));
    }



}
