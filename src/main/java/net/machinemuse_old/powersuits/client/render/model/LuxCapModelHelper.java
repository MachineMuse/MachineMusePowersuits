package net.machinemuse_old.powersuits.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.machinemuse_old.numina.geometry.Colour;
import net.machinemuse_old.powersuits.block.BlockLuxCapacitor;
import net.machinemuse_old.powersuits.common.Config;
import net.machinemuse_old.powersuits.common.MPSItems;
import net.machinemuse_old.powersuits.common.ModularPowersuits;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.block.BlockDirectional.FACING;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/5/17.
 *
 * Yes, this is a lot of code to color the lens of the model. But it still allows us to
 * have complete control over the lens color without using a TESR
 */
@SideOnly(Side.CLIENT)
public class LuxCapModelHelper {
    private static LuxCapModelHelper ourInstance = new LuxCapModelHelper();
    public static LuxCapModelHelper getInstance() {
        return ourInstance;
    }
    private LuxCapModelHelper() {
    }
    // item model variant
    public static final ModelResourceLocation luxCapItemLocation = new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "inventory");
    private static final ResourceLocation lensModelLocation = new ResourceLocation(ModularPowersuits.MODID.toLowerCase(), "models/block/luxCapacitor/lightCore.obj");
    private static List<BakedQuad> itemQuads = new ArrayList<>();


    // Store the Untouched models
    private static Map<EnumFacing, IBakedModel> luxCapCleanFrameModelMap = new HashMap<>();
    private static Map<EnumFacing, IBakedModel> luxCapCleanLensModelMap = new HashMap<>();

    // The actual quad map
    private static Map<Map<Colour, EnumFacing>, List<BakedQuad>> luxCapColoredQuadMap = new HashMap<>();

// todo use Guava chache
//    static LoadingCache<Map<Colour, EnumFacing>, List<BakedQuad>> luxCapColoredQuadMapII = CacheBuilder.newBuilder()
//            .maximumSize(40)
//            .build(new CacheLoader<Map<Colour, EnumFacing>, List<BakedQuad>>() {
//                @Override
//                public List<BakedQuad> load(Map<Colour, EnumFacing> key) throws Exception {
//                    return null;
//                }
//
//                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
//                    return ModelHelper.getColoredQuads(ModelHelper.powerFistFiring.getQuads(null, null,0), colour);
//                }
//            });



    static IModel getLensModel() {
        IModel model = null;
        try {
            model = OBJLoader.INSTANCE.loadModel(lensModelLocation);
            model = ((OBJModel) model).process(ImmutableMap.copyOf(ImmutableMap.of("flip-v", "true")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static IBakedModel getBakedLensModelForFacing(EnumFacing facing) {
        IBakedModel lensModel = luxCapCleanLensModelMap.get(facing);

        if (lensModel == null) {
            IBakedModel frameModel = luxCapCleanFrameModelMap.get(facing);
            if (frameModel != null && frameModel instanceof OBJModel.OBJBakedModel) {
                IModel model = getLensModel();
                if (model instanceof OBJModel) {
                    lensModel = model.bake(((OBJModel.OBJBakedModel) frameModel).getState(), DefaultVertexFormats.ITEM,
                            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
                }
            }
        }
        return lensModel;
    }

    public static IBakedModel getFrameForFacing(EnumFacing facing) {
        return luxCapCleanFrameModelMap.get(facing);
    }

    // used in the ModelBakeEvent handler to load the untouched models into the cache
    public static void putLuxCapModels(EnumFacing facing, IBakedModel model) {
        if (model instanceof OBJModel.OBJBakedModel && luxCapCleanFrameModelMap.get(facing) == null) {
            luxCapCleanFrameModelMap.put(facing, model);

            // bake and load the lens models too
            IBakedModel lensModel = getBakedLensModelForFacing(facing);
            if (lensModel instanceof OBJModel.OBJBakedModel && luxCapCleanLensModelMap.get(facing) == null) {
                luxCapCleanLensModelMap.put(facing, lensModel);
            }
        }
    }

    // get the frame model resource location for the given rotation variant
    public ModelResourceLocation getLocationForFacing(EnumFacing facing) {
        if (facing == null)
            return luxCapItemLocation;
        return new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "facing=" + facing.getName());
    }

    // translate the blockstate into the color value
    public static Colour getColorFromState(IExtendedBlockState state) {
        Colour color;
        if (state == null) color = BlockLuxCapacitor.defaultColor;
        else color = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOR);
        color = color != null ? color : BlockLuxCapacitor.defaultColor;
        return color;
    }

    // get the facing value from state
    public static EnumFacing getFacingFromState(IBlockState state) {
        if (state == null)
            return null;
        return state.getValue(FACING);
    }

    public static List<BakedQuad> getQuadsForColorAndFacing(Colour color, EnumFacing facing) {
        List<BakedQuad> quadList;
        if (facing == null) {
            quadList = itemQuads;
            if (quadList.isEmpty()) {
                IBakedModel lensModel;
                IBakedModel frameModel;
                IBlockState state;
                List<BakedQuad> lensList;
                List<BakedQuad> frameList;
                ImmutableList.Builder<BakedQuad> builder;


                lensModel = getBakedLensModelForFacing(null);
                frameModel = getFrameForFacing(null);
                state = MPSItems.luxCapacitor.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
                lensList = lensModel.getQuads(state, null, 0);
                lensList= ModelHelper.getColoredQuads(lensList, color);
                frameList = frameModel.getQuads(state, null, 0);

                builder = ImmutableList.builder();
                for (BakedQuad quad : frameList) {
                    builder.add(quad);
                }
                for (BakedQuad quad : lensList) {
                    builder.add(quad);
                }
                List<BakedQuad> quads = builder.build();
                itemQuads = quads;
            }
            return itemQuads;
        } else {
            quadList = luxCapColoredQuadMap.get(ImmutableMap.of(color, facing));

            if (quadList == null) {
                IBakedModel lensModel;
                IBakedModel frameModel;
                IBlockState state;
                List<BakedQuad> lensList;
                List<BakedQuad> frameList;
                ImmutableList.Builder<BakedQuad> builder;

                for (EnumFacing side: EnumFacing.values()) {
                    lensModel = getBakedLensModelForFacing(side);
                    frameModel = getFrameForFacing(side);
                    state = MPSItems.luxCapacitor.getDefaultState().withProperty(FACING, side);
                    lensList = lensModel.getQuads(state, null, 0);
                    lensList= ModelHelper.getColoredQuads(lensList, color);
                    frameList = frameModel.getQuads(state, null, 0);

                    builder = ImmutableList.builder();
                    for (BakedQuad quad : frameList) {
                        builder.add(quad);
                    }
                    for (BakedQuad quad : lensList) {
                        builder.add(quad);
                    }
                    List<BakedQuad> quads = builder.build();
                    if (facing == side)
                        quadList = quads;
                    luxCapColoredQuadMap.put(ImmutableMap.of(color, side), quads);
                }

            }
            return quadList;
        }
    }

    public static List<BakedQuad> getQuads(IExtendedBlockState state) {
        EnumFacing facing = getFacingFromState(state);
        Colour color = getColorFromState(state);

        return getQuadsForColorAndFacing(color, facing);
    }

    public static TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(new ResourceLocation(Config.RESOURCE_DOMAIN, "blocks/LuxCap"));
    }
}