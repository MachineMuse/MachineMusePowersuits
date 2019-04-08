package net.machinemuse.powersuits.client.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.client.model.item.armor.modelspec.ModelSpecXMLReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ModelHelper {
    static {
        new ModelHelper();
    }




    //-------------------------------------



    //-----------------------------------
//
//
//
//
//







//
//    /*
//     * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
//     * With this we can generate an extended blockstates to get the quads of any group in a model without
//     * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
//     * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
//     *
//     * The biggest issue with this setup is that the code. There is a better way out there
//     */
//    @Nullable
//    public static IExtendedBlockState getStateForPart(String shownIn, OBJModel.OBJBakedModel objBakedModelIn) {
//        List<String> hidden = new ArrayList<>(objBakedModelIn.getIModel().getMatLib().getGroups().keySet());
//        return getStateForPart(shownIn, hidden);
//    }
//
//    public static IExtendedBlockState getStateForPart(String shownIn, List<String> hiddenIn) {
//        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[] {net.minecraftforge.common.property.Properties.AnimationProperty});
//
//        hiddenIn.remove(shownIn);
//
//        try {
//            IModelState state = new IModelState() {
//                private final java.util.Optional<TRSRTransformation> getValue = java.util.Optional.of(TRSRTransformation.identity());
//
//                @Override
//                public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
//                    if (part.isPresent()) {
//                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
//                        if (parts.hasNext()) {
//                            String id = parts.next();
//                            // only interested in the root level
//                            if (!parts.hasNext() && hiddenIn.contains(id)) return getValue;
//                        }
//                    }
//                    return java.util.Optional.empty();
//                }
//            };
//            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    //--

















}