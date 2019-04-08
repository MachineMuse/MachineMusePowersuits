package net.machinemuse.powersuits.client.event;


import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.basemod.ModularPowersuits;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.model.block.TinkerTableModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public enum ModelBakeEventHandler {
    INSTANCE;

    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(MPSItems.INSTANCE.powerFist.getRegistryName().toString(), "inventory");
    public static IBakedModel powerFistIconModel;

    final IModelState modelState = getModelState();




    private static Map<ModelResourceLocation, IBakedModel> modelRegistry;


    //    ModelResourceLocation tinkerTableLocation = new ModelResourceLocation(new ResourceLocation(ModularPowersuits.MODID, BlockTinkerTable.name).toString());


    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        modelRegistry = event.getModelRegistry();
        IModel tinkertableUnbaked = MuseModelHelper.getModel(new ResourceLocation(ModularPowersuits.MODID,
                "models/block/powerarmor_workbench.obj"));

        // New Lux Capacitor Inventory Model
        modelRegistry.put(ModelLuxCapacitor.modelResourceLocation, new ModelLuxCapacitor());

        // new Tinker Table Inventory Model
        modelRegistry.put(
                new ModelResourceLocation(MPSItems.INSTANCE.tinkerTableRegName, "inventory"),
                new TinkerTableModel(tinkertableUnbaked.bake(ModelLoader.defaultModelGetter(),
                        MuseModelHelper.defaultTextureGetter(),
                        modelState,
                        true, DefaultVertexFormats.ITEM)));

        for (EnumFacing facing : EnumFacing.values()) {
            modelRegistry.put(ModelLuxCapacitor.getModelResourceLocation(facing), new ModelLuxCapacitor());

            if (facing.equals(EnumFacing.DOWN) || facing.equals(EnumFacing.UP))
                continue;

            modelRegistry.put(
                new ModelResourceLocation(
                        MPSItems.INSTANCE.luxCapaRegName, "facing=" + facing.getName()),
                        tinkertableUnbaked.bake(ModelLoader.defaultModelGetter(),
                            MuseModelHelper.defaultTextureGetter(), TRSRTransformation.from(facing), true, DefaultVertexFormats.ITEM));
        }

//        for (ResourceLocation location : modelRegistry.keySet()) {
////            MuseLogger.logInfo("model location namespace: " + location.getNamespace());
//
//
//            if (location.getNamespace().equals(ModularPowersuits.MODID)) {
//                Numina.LOGGER.info("MPS model location: " + location.toString());
//            }
//        }



    }

    public IModelState getModelState() {
        ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();

        // first person and third person models rotated to so that the side away from the player is the same as when it is placed
        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                MuseModelHelper.get(0, 0, 0, 0, 135, 0, 0.4f));

        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                MuseModelHelper.get(0, 0, 0, 0, 135, 0, 0.4f));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                MuseModelHelper.get(0, 2.5f, 0, 75, -135, 0, 0.375f));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                MuseModelHelper.get(0, 2.5f, 0, 75, -135, 0, 0.375f));

        builder.put(ItemCameraTransforms.TransformType.GUI,
                MuseModelHelper.get(-0.0625F, 0.25F, 0, 30, 225, 0, 0.625f));

        builder.put(ItemCameraTransforms.TransformType.GROUND,
                MuseModelHelper.get(0, 3, 0, 0, 0, 0, 0.25f));

        builder.put(ItemCameraTransforms.TransformType.FIXED,
                MuseModelHelper.get(0, 0, 0, 0, 0, 0, 0.5f));

        return new SimpleModelState(builder.build());
    }
}
