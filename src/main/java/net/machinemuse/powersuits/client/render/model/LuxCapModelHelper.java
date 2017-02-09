package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/5/17.
 */
public class LuxCapModelHelper {
    private static final ResourceLocation lensModelLocation = new ResourceLocation(ModularPowersuits.MODID.toLowerCase(), "models/block/luxCapacitor/lightCore.obj");
    private static Map<String, String> customData = new HashMap<String, String>();
    private static IModel lensModel;
    private static final String materialname = "LensMaterial";

    private static LuxCapModelHelper ourInstance = new LuxCapModelHelper();
    public static LuxCapModelHelper getInstance() {
        return ourInstance;
    }

    private LuxCapModelHelper() {
    }

    // Store the frame models
    public static Map<ModelResourceLocation, IBakedModel> frameModelMap = new HashMap<>();
    public static final ModelResourceLocation luxCapItemLocation = new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "inventory");
    public static Map<Colour, Map<IModelState, IBakedModel>> lensChache = new HashMap();
    public static Map<IExtendedBlockState, List<BakedQuad>> quadMap = new HashMap<>();

    public ModelResourceLocation getLocationForFacing(EnumFacing facing) {
        if (facing == null)
            return luxCapItemLocation;
        return new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "facing=" + facing.getName());
    }

    public ModelResourceLocation getLocationForState(IBlockState state) {
        EnumFacing facing = state != null ? state.getValue(BlockLuxCapacitor.FACING) : null;
        return getLocationForFacing(facing);
    }

    public static void putLuxCapFameModels(ModelResourceLocation resourceLocation, IBakedModel model) {
        if ((model == null && !frameModelMap.containsKey(resourceLocation)) || // new entry from state mapper to make an iterable list from keyset
                (model instanceof OBJModel.OBJBakedModel // can't check for "MissingModel" since model ModelLoaderRegistry is null
                        && frameModelMap.get(resourceLocation) == null) ) { // replace the value with an actual model
            frameModelMap.put(resourceLocation, model);
        }
    }

    public IBakedModel getFrameModelforState(IBlockState state) {
        ModelResourceLocation location = getLocationForState(state);
        return  frameModelMap.get(location);
    }

    public static IModel getLensModel() {
        if (!customData.containsKey("flip-v"))
            customData.put("flip-v", "true");
        if (lensModel == null) {
            try {
                lensModel = OBJLoader.INSTANCE.loadModel(lensModelLocation);
                if (lensModel instanceof OBJModel) {
                    lensModel = ((OBJModel) lensModel).process(ImmutableMap.copyOf(customData));
                }
            } catch (Exception e) {
                e.printStackTrace();
                lensModel = ModelLoaderRegistry.getMissingModel();
            }
        }
        return lensModel;
    }

    IBakedModel getColoredLens(IModelState imodelState, Colour color) {
        IBakedModel coloredLens = null;
        if (lensChache.get(color) != null) {
            coloredLens = lensChache.get(color).get(imodelState);
        }

        if (coloredLens == null) {
            Map<IModelState, IBakedModel> tempMap = new HashMap<>();

            IModel lensModel = getLensModel();
            if (lensModel instanceof OBJModel)
                ((OBJModel)lensModel).getMatLib().getMaterial(materialname).setColor(color.toVector4f());
            coloredLens = lensModel.bake(imodelState, DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));

            tempMap.put(imodelState, coloredLens);
            lensChache.put(color, tempMap);
        }
        return coloredLens;
    }

    public static List<ModelResourceLocation> locationList = new ArrayList<>();
    public static Map<Colour, IBakedModel> luxCapMap = new HashMap<>();

    public List<BakedQuad> getQuads(@Nullable IBakedModel bakedFrame, @Nullable IBakedModel bakedlens, IBlockState state, EnumFacing side, long rand) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : bakedFrame.getQuads(state, side, rand)) {
            builder.add(quad);
        }

        for (BakedQuad quad : bakedlens.getQuads(state, side, rand)) {
            builder.add(quad);
        }
        return builder.build();
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quadlist = quadMap.get(state);
        if ( quadlist == null) {
            IBakedModel lensModel = null;
            Colour color = null;
            ModelResourceLocation location = getLocationForState(state);
            if (state != null) color = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOR);
            color = color != null ? color : BlockLuxCapacitor.defaultColor;
            IBakedModel baseBakedModel = frameModelMap.get(location);
            IModelState modelState = baseBakedModel != null ? ((OBJModel.OBJBakedModel) baseBakedModel).getState() : getLuxCapacitorBlockTransform(
                    state != null ? state.getValue(BlockLuxCapacitor.FACING) : (side != null ? side : EnumFacing.DOWN));

            lensModel = getColoredLens(modelState, color);
            quadlist = getQuads(baseBakedModel, lensModel, state, side, rand);
            quadMap.put((IExtendedBlockState) state, quadlist);
        }
        return quadlist;
    }

    /**
     * We need our own because the default set is based on the default=facing north
     * Our model is default facing up
     */
    public static TRSRTransformation getLuxCapacitorBlockTransform(EnumFacing side) {
        Matrix4f matrix;

        switch(side) {
            case DOWN:
                matrix = TRSRTransformation.identity().getMatrix();
                matrix.setTranslation(new Vector3f(0.0f + 1, -0.5f + 1, 0.0f + 1));
                break;
            case UP:
                matrix = ModelRotation.X180_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f + 1, 0.5f + 1, 0.0f + 1));
                break;

            case NORTH:
                matrix = ModelRotation.X270_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f + 1, 0.0f + 1, -0.5f + 1));
                break;
            case SOUTH:
                matrix = ModelRotation.X90_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f + 1, 0.0f + 1, 0.5f + 1));
                break;

            case WEST:
                matrix = ModelRotation.X90_Y90.getMatrix();
                matrix.setTranslation(new Vector3f(-0.5f + 1, 0.0f + 1, -0.0f + 1));
                break;
            case EAST:
                matrix = ModelRotation.X90_Y270.getMatrix();
                matrix.setTranslation(new Vector3f(0.5f + 1, 0.0f + 1, -0.0f + 1));
                break;
            default:
                matrix = new Matrix4f();
                break;
        }

        matrix.setScale(0.0625f);
        return TRSRTransformation.blockCornerToCenter(new TRSRTransformation(matrix));
    }
}