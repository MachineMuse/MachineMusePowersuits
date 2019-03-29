package net.machinemuse.powersuits.client.model.helper;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum ModelLuxCapacitorHelper {
    INSTANCE;

    private static final ResourceLocation baseModelLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_PREFIX + "block/luxcapacitor/luxcapacitor_base.obj");
    private static final ResourceLocation lensModelLocation = new ResourceLocation(MPSResourceConstants.RESOURCE_PREFIX + "block/luxcapacitor/luxcapacitor_lens.obj");

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
                    return MuseModelHelper.getBakedModel(baseModelLocation, TRSRTransformation.from((facing != null) ? facing : EnumFacing.NORTH));
                }

                public IBakedModel getLens(@Nullable EnumFacing facing) {
                    return MuseModelHelper.getBakedModel(lensModelLocation, TRSRTransformation.from((facing != null) ? facing : EnumFacing.NORTH));
                }

                List<BakedQuad> getBaseQuads(@Nullable EnumFacing facing) {
                    facing = (facing != null) ? facing : EnumFacing.NORTH;

                    TRSRTransformation transform = TRSRTransformation.from(facing);
                    IBakedModel bakedModel = MuseModelHelper.getBakedModel(baseModelLocation, transform);
                    return bakedModel.getQuads(MPSItems.INSTANCE.luxCapacitor.getDefaultState().withProperty(BlockDirectional.FACING, facing), null, 0);
                }

                List<BakedQuad> getLensColoredQuads(Colour color, @Nullable EnumFacing facing) {
                    facing = (facing != null) ? facing : EnumFacing.NORTH;
                    TRSRTransformation transform = TRSRTransformation.from(facing);
                    IBakedModel bakedModel = MuseModelHelper.getBakedModel(lensModelLocation, transform);
                    List<BakedQuad> quads = bakedModel.getQuads(MPSItems.INSTANCE.luxCapacitor.getDefaultState().withProperty(BlockDirectional.FACING, facing), null, 0);
                    return MuseModelHelper.getColoredQuadsWithGlow(quads, color, true);
                }

                List<BakedQuad> getQuads(Colour color, @Nullable EnumFacing facing) {
                    List<BakedQuad> frameList = getBaseQuads(facing);
                    List<BakedQuad> lensList = getLensColoredQuads(color, facing);

                    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
                    for (BakedQuad quad : frameList)
                        builder.add(quad);
                    for (BakedQuad quad : lensList)
                        builder.add(quad);
                    return builder.build();
                }
            });
}