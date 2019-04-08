package net.machinemuse.powersuits.client.event;

import net.machinemuse.powersuits.client.helper.ModelHelper;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.model.item.ModelPowerFist;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
@SideOnly(Side.CLIENT)
public enum ModelBakeEventHandler {
    INSTANCE;

    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerFist.getRegistryName().toString(), "inventory");
    public static IBakedModel powerFistIconModel;
    private static IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
//
//    // Armor icons
//    public static final ModelResourceLocation powerArmorHeadModelLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerArmorHead.getRegistryName(), "inventory");
//    public static final ModelResourceLocation powerArmorChestModelLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerArmorTorso.getRegistryName(), "inventory");
//    public static final ModelResourceLocation powerArmorLegsModelLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerArmorLegs.getRegistryName(), "inventory");
//    public static final ModelResourceLocation powerArmorFeetModelLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerArmorFeet.getRegistryName(), "inventory");

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        modelRegistry = event.getModelRegistry();

        // New Lux Capacitor Model
        modelRegistry.putObject(ModelLuxCapacitor.modelResourceLocation, new ModelLuxCapacitor());

        for (EnumFacing facing : EnumFacing.VALUES) {
            modelRegistry.putObject(ModelLuxCapacitor.getModelResourceLocation(facing), new ModelLuxCapacitor());
        }

        // Power Fist
        powerFistIconModel = modelRegistry.getObject(powerFistIconLocation);
        modelRegistry.putObject(powerFistIconLocation, new ModelPowerFist(powerFistIconModel));

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

        ModelHelper.loadArmorModels(null);
    }
}
