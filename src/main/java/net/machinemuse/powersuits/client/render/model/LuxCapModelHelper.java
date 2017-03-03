package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableList;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/5/17.
 *
 * Yes, this is a lot of code to color the lens of the model. But it still allows us to
 * have complete control over the lens color without using a TESR
 */
public class LuxCapModelHelper {
    private static LuxCapModelHelper ourInstance = new LuxCapModelHelper();
    public static LuxCapModelHelper getInstance() {
        return ourInstance;
    }
    private LuxCapModelHelper() {
    }

    private static final ResourceLocation lensModelLocation = new ResourceLocation(ModularPowersuits.MODID.toLowerCase(), "models/block/luxCapacitor/lightCore.obj");
    public static final ModelResourceLocation luxCapItemLocation = new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "inventory");

    // Store the Untouched models
    public static Map<ModelResourceLocation, IBakedModel> luxCapCleanModelMap = new HashMap<>();


    // get the frame model resource location for the given rotation variant
    public ModelResourceLocation getLocationForFacing(EnumFacing facing) {
        if (facing == null)
            return luxCapItemLocation;
        return new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), "facing=" + facing.getName());
    }

    // translate the blockstate into a facing value and get frame rotation variant location
    public ModelResourceLocation getLocationForState(IBlockState state) {
        EnumFacing facing = state != null ? state.getValue(BlockLuxCapacitor.FACING) : null;
        return getLocationForFacing(facing);
    }

    List<BakedQuad> getQuadsForFacing(IBakedModel baseBakedModelIn, Colour colorIn, EnumFacing facingIn, IExtendedBlockState lensStateIn, IExtendedBlockState frameStateIn) {
        List<BakedQuad> coloredLensQuads = ModelHelper.getColoredQuads(baseBakedModelIn.getQuads(lensStateIn, facingIn, 0), colorIn);
        List<BakedQuad> frameQuads = baseBakedModelIn.getQuads(frameStateIn, facingIn, 0);
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : frameQuads) {
            builder.add(quad);
        }
        for (BakedQuad quad : coloredLensQuads) {
            builder.add(quad);
        }
        return builder.build();
    }

    Map<IExtendedBlockState,  Map<EnumFacing, List<BakedQuad>>> coloredQuadMap = new HashMap<>();
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        Map<EnumFacing, List<BakedQuad>> quadMap = coloredQuadMap.get(((IExtendedBlockState) state));
        if (quadMap == null) {
            quadMap = new HashMap<>();
            Colour color = null;
            ModelResourceLocation location = getLocationForState(state);
            if (state != null) color = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOR);
            color = color != null ? color : BlockLuxCapacitor.defaultColor;
            IBakedModel baseBakedModel = luxCapCleanModelMap.get(location);
            IExtendedBlockState lensState = ModelHelper.getStateForPart("poplight001", (OBJModel.OBJBakedModel)baseBakedModel);
            IExtendedBlockState frameState = ModelHelper.getStateForPart("poplight", (OBJModel.OBJBakedModel)baseBakedModel);
            List<BakedQuad> quadList;
            for (EnumFacing facing : EnumFacing.values()) {
                quadMap.put(facing, getQuadsForFacing(baseBakedModel,color, facing, lensState, frameState));
            }
            quadMap.put(null, getQuadsForFacing(baseBakedModel,color, null, lensState, frameState));
            coloredQuadMap.put((IExtendedBlockState) state, quadMap);
        }
        return quadMap.get(side);
    }

    public static void putLuxCapModels(ModelResourceLocation resourceLocation, IBakedModel model) {
        if (model instanceof OBJModel.OBJBakedModel && luxCapCleanModelMap.get(resourceLocation) == null) {
            luxCapCleanModelMap.put(resourceLocation, model);
        }
    }

    // get the actual frame model for the given state
    public IBakedModel getFrameModelforState(IBlockState state) {
        ModelResourceLocation location = getLocationForState(state);
        return luxCapCleanModelMap.get(location);
    }
}