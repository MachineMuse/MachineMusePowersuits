package net.machinemuse.powersuits.client.render.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;


/**
 * Created by lehjr on 12/19/16.
 */
public class ModelPowerFist implements IBakedModel, IPerspectiveAwareModel {
    static ItemCameraTransforms.TransformType cameraTransformType;
    static ItemStack itemStack;
    static Item item;
    static Colour colour;
    static World world;
    static EntityLivingBase entity;
    static boolean isFiring = false;

    static IBakedModel iconModel;
    public ModelPowerFist(IBakedModel bakedModelIn) {
        if (bakedModelIn instanceof ModelPowerFist) {
            iconModel = ((ModelPowerFist)bakedModelIn).iconModel;
        } else {
            iconModel = bakedModelIn;
        }
    }

    static LoadingCache<Colour, List<BakedQuad>> powerFistIconCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(iconModel.getQuads(null, null,0), colour);
                }
            });

    static LoadingCache<Colour, List<BakedQuad>> powerFistCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(ModelHelper.powerFist.getQuads(null, null,0), colour);
                }
            });


    static LoadingCache<Colour, List<BakedQuad>> powerFistFiringCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(ModelHelper.powerFistFiring.getQuads(null, null,0), colour);
                }
            });


    static LoadingCache<Colour, List<BakedQuad>> powerFistLeftCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(ModelHelper.powerFistLeft.getQuads(null, null,0), colour);
                }
            });


    static LoadingCache<Colour, List<BakedQuad>> powerFistLeftFiringCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(ModelHelper.powerFistLeftFiring.getQuads(null, null,0), colour);
                }
            });

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        cameraTransformType = cameraTransformTypeIn;
        Matrix4f matrix;
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                matrix = ModelHelper.get(-13, -1f, -16.2f, -14, -181, 0, 0.5f).getMatrix();
                break;
            case THIRD_PERSON_RIGHT_HAND:
                matrix = ModelHelper.get(-18, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case FIRST_PERSON_LEFT_HAND:
                matrix = ModelHelper.get(-4.8f, -1f, -16.2f, -14, -181, 0, 0.5f).getMatrix();
                break;
            case THIRD_PERSON_LEFT_HAND:
                matrix = ModelHelper.get(-8, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case GROUND:
                matrix = ModelHelper.get(0, 5, 0, 0, 0, 0, 0.630f).getMatrix();
                break;
            default:
//                ModelHelper.transformCalibration();
//                matrix = ModelHelper.get(ModelHelper.xOffest, ModelHelper.yOffest, ModelHelper.zOffest, ModelHelper.xtap, ModelHelper.ytap, ModelHelper.ztap, 0.630f).getMatrix();

                if (iconModel != null && iconModel instanceof IPerspectiveAwareModel) {
                    matrix = ((IPerspectiveAwareModel) iconModel).handlePerspective(cameraTransformTypeIn).getValue();
                } else {
                    matrix = TRSRTransformation.identity().getMatrix();
                }
                break;
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side != null) return Collections.EMPTY_LIST;

        List<BakedQuad> quadList;
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
            case GROUND:
                if(isFiring)
                    try {
                        quadList = powerFistFiringCache.get(colour);
                    } catch (Exception e) {
                        e.printStackTrace();
                        quadList = Collections.EMPTY_LIST;
                    }
                else
                    try {
                        quadList = powerFistCache.get(colour);
                    } catch (Exception e) {
                        e.printStackTrace();
                        quadList = Collections.EMPTY_LIST;
                    }
                break;

            case FIRST_PERSON_LEFT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                if(isFiring)
                    try {
                        quadList = powerFistFiringCache.get(colour);
                    } catch (Exception e) {
                        e.printStackTrace();
                        quadList = Collections.EMPTY_LIST;
                    }
                else
                    try {
                        quadList = powerFistLeftCache.get(colour);
                    } catch (Exception e) {
                        e.printStackTrace();
                        quadList = Collections.EMPTY_LIST;
                    }
                break;
            default:
                try {
                    quadList = powerFistIconCache.get(colour);
                } catch (Exception e) {
                    e.printStackTrace();
                    quadList = Collections.EMPTY_LIST;
                }
        }
        return quadList;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return iconModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return iconModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return iconModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return iconModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new PowerFistItemOverrideList();
    }

    public class PowerFistItemOverrideList  extends ItemOverrideList {
        public PowerFistItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stackIn, World worldIn, EntityLivingBase entityIn) {
            itemStack = stackIn;
            world = worldIn;
            entity = entityIn;
            item = itemStack.getItem();
            colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
            if (entityIn instanceof EntityPlayer) {
                if (itemStack != null && itemStack == entityIn.getHeldItemMainhand() && entityIn.isHandActive()
                        && ModuleManager.itemHasActiveModule(itemStack, PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
                    isFiring = true;
                }
                else isFiring = false;
            }
            else isFiring = false;

            return originalModel;
        }
    }
}