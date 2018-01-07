package net.machinemuse.powersuits.client.models;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import net.machinemuse.powersuits.client.helpers.ColoredQuadHelperThingie;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.common.block.BlockLuxCapacitor;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.RESOURCE_PREFIX;
import static net.machinemuse.powersuits.common.block.BlockLuxCapacitor.FACING;

@SideOnly(Side.CLIENT)
public class ModelLuxCapacitorHelper {
    private static final ResourceLocation baseModelLocation = new ResourceLocation(RESOURCE_PREFIX + "block/luxcapacitor/luxcapacitor_base.obj");
    private static final ResourceLocation lensModelLocation = new ResourceLocation(RESOURCE_PREFIX + "block/luxcapacitor/luxcapacitor_lens.obj");
    /*
     * Guava chache for the list of baked quads.
     * The "ColoredQuadHelperThingie" is just easier and cleaner than using multi level maps.
     */
    public static LoadingCache<ColoredQuadHelperThingie, List<BakedQuad>> luxCapColoredQuadMap = CacheBuilder.newBuilder()
            .maximumSize(40)
            .build(new CacheLoader<ColoredQuadHelperThingie, List<BakedQuad>>() {
                @Override
                public List<BakedQuad> load(ColoredQuadHelperThingie key) throws Exception {
                    return getQuads(key.getColour(), key.getFacing());
                }

                public IBakedModel getBase(@Nullable EnumFacing facing) {
                    return ModelHelper.getBakedModel(baseModelLocation, new TRSRTransformation((facing != null) ? facing : EnumFacing.NORTH));
                }

                public IBakedModel getLens(@Nullable EnumFacing facing) {
                    return ModelHelper.getBakedModel(lensModelLocation, new TRSRTransformation((facing != null) ? facing : EnumFacing.NORTH));
                }

                List<BakedQuad> getBaseQuads(@Nullable EnumFacing facing) {
                    facing = (facing != null) ? facing : EnumFacing.NORTH;

                    TRSRTransformation transform = new TRSRTransformation(facing);
                    IBakedModel bakedModel = ModelHelper.getBakedModel(baseModelLocation, transform);
                    return bakedModel.getQuads(BlockLuxCapacitor.getInstance().getDefaultState().withProperty(FACING, facing), null, 0);
                }

                List<BakedQuad> getLensColoredQuads(EnumColour color, @Nullable EnumFacing facing) {
                    facing = (facing != null) ? facing : EnumFacing.NORTH;
                    TRSRTransformation transform = new TRSRTransformation(facing);
                    IBakedModel bakedModel = ModelHelper.getBakedModel(lensModelLocation, transform);
                    List<BakedQuad> quads = bakedModel.getQuads(BlockLuxCapacitor.getInstance().getDefaultState().withProperty(FACING, facing), null, 0);
//                    return ModelHelper.getColoredQuads(quads, color.getColour());
                    return ModelHelper.getColoredQuadsWithGlow(quads, color.getColour(), true);
                }

                List<BakedQuad> getQuads(EnumColour color, @Nullable EnumFacing facing) {
                    List<BakedQuad> frameList = getBaseQuads(facing);
                    List<BakedQuad> lensList = getLensColoredQuads(color, facing);

                    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
                    for (BakedQuad quad : frameList) {
                        builder.add(quad);
                    }
                    for (BakedQuad quad : lensList) {
                        builder.add(quad);
                    }
                    return builder.build();
                }
            });
    private static ModelLuxCapacitorHelper INSTANCE;
    private ModelLuxCapacitorHelper() {
    }

    public static ModelLuxCapacitorHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelLuxCapacitorHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelLuxCapacitorHelper();
                }
            }
        }
        return INSTANCE;
    }
}
