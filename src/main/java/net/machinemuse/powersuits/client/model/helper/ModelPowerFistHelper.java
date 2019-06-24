package net.machinemuse.powersuits.client.model.helper;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.event.ModelBakeEventHandler;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum ModelPowerFistHelper {
    INSTANCE;

    public static final ResourceLocation powerFistLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerfist/powerfist.obj");
    public static final ResourceLocation powerFistFingersNormalLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerfist/powerfist_fingers_normal.obj");
    public static final ResourceLocation powerFistFingersFiringLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerfist/powerfist_fingers_firing.obj");

    public static final ResourceLocation powerFistLeftLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerFist/powerfist_left.obj");
    public static final ResourceLocation powerFistFingersLeftNormalLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerfist/powerfist_fingers_normal_left.obj");
    public static final ResourceLocation powerFistFingersLeftFiringLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, "models/item/powerfist/powerfist_fingers_firing_left.obj");
    public static final IModelState powerfistState = getPowerfistState();
    public static IBakedModel powerFist;
    public static IBakedModel powerFistFingers;
    public static IBakedModel powerFistFingersFiring;
    public static IBakedModel powerFistLeft;
    public static IBakedModel powerFistFingersLeft;
    public static IBakedModel powerFistFingersLeftFiring;
    public static LoadingCache<PowerFistQuadMapKey, List<BakedQuad>> colouredPowerFistQuadMap = CacheBuilder.newBuilder()
            .maximumSize(40)
            .build(new CacheLoader<PowerFistQuadMapKey, List<BakedQuad>>() {
                @Override
                public List<BakedQuad> load(PowerFistQuadMapKey key) throws Exception {
                    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
                    switch (key.transformType) {
                        case THIRD_PERSON_LEFT_HAND:
                        case FIRST_PERSON_LEFT_HAND:
                            powerFistLeft.getQuads(null, null, 0).forEach(quad -> builder.add(
                                    MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            if (key.isFiring())
                                powerFistFingersLeftFiring.getQuads(null, null, 0).forEach(quad -> builder.add(
                                        MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            else
                                powerFistFingersLeft.getQuads(null, null, 0).forEach(quad -> builder.add(
                                        MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            return builder.build();

                        case THIRD_PERSON_RIGHT_HAND:
                        case FIRST_PERSON_RIGHT_HAND:
                        case GROUND:
                            powerFist.getQuads(null, null, 0).forEach(quad -> builder.add(
                                    MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            if (key.isFiring())
                                powerFistFingersFiring.getQuads(null, null, 0).forEach(quad -> builder.add(
                                        MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            else
                                powerFistFingers.getQuads(null, null, 0).forEach(quad -> builder.add(
                                        MuseModelHelper.colorQuad(key.getColour(), quad, quad.shouldApplyDiffuseLighting())));
                            return builder.build();

                        default:
                            return MuseModelHelper.getColoredQuads(ModelBakeEventHandler.powerFistIconModel.getQuads(null, null, 0), key.getColour());
                    }
                }
            });

    public static void loadPowerFistModels(@Nullable TextureMap map) {
        if (map != null) {
            try {
                // FIXME: register textures from XML loader
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistLocation);
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistFingersNormalLocation);
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistFingersFiringLocation);
//
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistLeftLocation);
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistFingersLeftNormalLocation);
//                MuseOBJLoader.INSTANCE.registerModelSprites(powerFistFingersLeftFiringLocation);
            } catch (Exception ignored) {
            }
        } else {
            powerFist = MuseModelHelper.loadBakedModel(powerFistLocation);
            powerFistFingers = MuseModelHelper.loadBakedModel(powerFistFingersNormalLocation);
            powerFistFingersFiring = MuseModelHelper.loadBakedModel(powerFistFingersFiringLocation);

            powerFistLeft = MuseModelHelper.loadBakedModel(powerFistLeftLocation);
            powerFistFingersLeft = MuseModelHelper.loadBakedModel(powerFistFingersLeftNormalLocation);
            powerFistFingersLeftFiring = MuseModelHelper.loadBakedModel(powerFistFingersLeftFiringLocation);
        }
    }

    static IModelState getPowerfistState() {
        ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                MuseModelHelper.get(8, 8.01f, 9, -15, 180, 0, 0.630f, 0.630f, 0.630f));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                MuseModelHelper.get(13.2f/*-2 */, 8.01f, 9, -15, 180, 0, 0.630f, 0.630f, 0.630f));

        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                MuseModelHelper.get(11.8f, 8, 7, -16, -162, 0, 0.5f, 0.5f, 0.5f));

        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                MuseModelHelper.get(14.8f/*11.8f*/ /* 1.8f */, 8, 7, 344, 198, 0, 0.5f, 0.5f, 0.5f));

        builder.put(ItemCameraTransforms.TransformType.GROUND,
                MuseModelHelper.get(0, 5, 0, 0, 0, 0, 0.630f));
        return new SimpleModelState(builder.build());
    }

    public static class PowerFistQuadMapKey {
        private final Colour colour;
        private final ItemCameraTransforms.TransformType transformType;
        private final boolean firing;

        public PowerFistQuadMapKey(Colour colour, ItemCameraTransforms.TransformType transformType, boolean firing) {
            this.colour = colour;
            this.transformType = transformType;
            this.firing = firing;
        }

        public Colour getColour() {
            return colour;
        }

        public ItemCameraTransforms.TransformType getTransformType() {
            return transformType;
        }

        public boolean isFiring() {
            return firing;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PowerFistQuadMapKey that = (PowerFistQuadMapKey) o;
            return firing == that.firing &&
                    Objects.equal(getColour(), that.getColour()) &&
                    getTransformType() == that.getTransformType();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getColour(), getTransformType(), firing);
        }
    }
}
