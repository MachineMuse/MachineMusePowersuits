package net.machinemuse.powersuits.client.models;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ModelHelper {
    static {
        new ModelHelper();
    }

    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scaleX, float scaleY, float scaleZ) {
        return new TRSRTransformation(
                // Transform
                new Vector3f(transformX / 16, transformY / 16, transformZ / 16),
                // Angles
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
                // Scale
                new Vector3f(scaleX, scaleY, scaleZ),
                null);
    }

    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
        return get(transformX, transformY, transformZ, angleX, angleY, angleZ, scale, scale, scale);
    }


    public static IModel getIModel(ResourceLocation location, int attempt) {
        // powersuits:models/item/armor/mps_helm.obj
        // powersuits:models/item/armor/mps_helm.obj

        IModel model;
        try {
            model = ModelLoaderRegistry.getModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                model = getIModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            }
            else {
                MuseLogger.logError("Failed to load model. " + e);

                return getOBJModel(location, 0);
            }
        }
        return model;
    }

    public static IModel getOBJModel(ResourceLocation location, int attempt) {

        IModel model;
        try {
            model = OBJLoader.INSTANCE.loadModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                getIModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            }
            else
                return model;
            MuseLogger.logError("Failed to load model. " + e);
        }
        return model;

    }

    public static IBakedModel getBakedModel(ResourceLocation modellocation, IModelState modelState){
        IModel model = getIModel(modellocation, 0);

        try {
            return model.bake(modelState, DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e ) {
            MuseLogger.logError("Failed to bake model. " + e);
        }
        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
    }


    /*
      * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
      * With this we can generate an extended blockstate to get the quads of any group in a model without
      * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
      * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
      *
      * The biggest issue with this setup is that the code. There is a better way out there
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(List<String> shownIn, OBJModel.OBJBakedModel objBakedModelIn, @Nullable TRSRTransformation transformation) {
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        if (transformation == null)
            transformation = TRSRTransformation.identity();
        return getStateForPart(shownIn, hidden, transformation);
    }

    @Nullable
    public static List<String> getPartNames(IBakedModel bakedModel) {
        if (bakedModel != null && bakedModel instanceof OBJModel.OBJBakedModel)
            return new ArrayList<String>(((OBJModel.OBJBakedModel) bakedModel).getModel().getMatLib().getGroups().keySet());
        return null;
    }

    public static IExtendedBlockState getStateForPart(List<String> shownIn, List<String> hiddenIn, final TRSRTransformation transformation) {
        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[] {net.minecraftforge.common.property.Properties.AnimationProperty});
        for (String shown: shownIn)
            hiddenIn.remove(shown);

        try {
            IModelState state = new IModelState() {
                private final java.util.Optional<TRSRTransformation> value = java.util.Optional.of(transformation);

                @Override
                public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
                        if (parts.hasNext()) {
                            String name = parts.next();
                            // only interested in the root level
                            if (!parts.hasNext() && hiddenIn.contains(name)) return value;
                        }
                    }
                    return java.util.Optional.empty();
                }
            };
            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static final ResourceLocation powerFistLocation = new ResourceLocation(RESOURCE_PREFIX, "models/item/powerFist/powerFist.obj");
//    public static final ResourceLocation powerFistFiringLocation = new ResourceLocation(RESOURCE_PREFIX, "models/item/powerFist/powerFistFiring.obj");
//    public static final ResourceLocation powerFistLeftLocation = new ResourceLocation(RESOURCE_PREFIX, "models/item/powerFist/powerFistLeft.obj");
//    public static final ResourceLocation powerFistLeftFiringLocation = new ResourceLocation(RESOURCE_PREFIX, "models/item/powerFist/powerFistFiringLeft.obj");
//
//    static IBakedModel powerFist;
//    static IBakedModel powerFistFiring;
//    static IBakedModel powerFistLeft;
//    static IBakedModel powerFistLeftFiring;
//
//    // One pass just to register the textures called from texture stitch event
//    // another to register the models called from model bake event (second run)
//    public static void loadArmorModels(boolean loadModels) {
//        URL resource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
//        ModelSpecXMLReader.getINSTANCE().parseFile(resource, loadModels);
//        URL otherResource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
//        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource, loadModels);
//
//        if (!loadModels) {
//            try {
//                MPSOBJLoader.INSTANCE.registerModelSprites(powerFistLocation);
//                MPSOBJLoader.INSTANCE.registerModelSprites(powerFistFiringLocation);
//                MPSOBJLoader.INSTANCE.registerModelSprites(powerFistLeftLocation);
//                MPSOBJLoader.INSTANCE.registerModelSprites(powerFistLeftFiringLocation);
//            } catch (Exception ignored) {
//            }
//        } else {
//            powerFist = loadBakedModel(powerFistLocation);
//            powerFistFiring = loadBakedModel(powerFistFiringLocation);
//            powerFistLeft = loadBakedModel(powerFistLeftLocation);
//            powerFistLeftFiring = loadBakedModel(powerFistLeftFiringLocation);
//        }
//    }
//
//    public static IModel getModel(ResourceLocation resource){
//        IModel model = null;
//        try {
//            model = (OBJModel) MPSOBJLoader.INSTANCE.loadModel(resource);
//            model = ((OBJModel) model).process(ImmutableMap.copyOf(ImmutableMap.of("flip-v", "true")));
//        } catch (Exception e) {
//            e.printStackTrace();
//            MuseLogger.logError("Model loading failed :( " + resource);
//        }
//        return model;
//    }
//
//    public static IBakedModel loadBakedModel(ResourceLocation resource) {
//        IModel model = getModel(resource);
//        if (model != null) {
//
//            IBakedModel bakedModel = model.bake(model.getDefaultState(),
//                    DefaultVertexFormats.ITEM,
//                    new Function<ResourceLocation, TextureAtlasSprite>() {
//                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
//                            return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
//                        }
//                    });
//            return bakedModel;
//        }
//        return null;
//    }
//
    //-----------------------
    public static int xtap = 0;
    public static int ytap = 0;
    public static int ztap = 0;
    public static float xOffest = 0;
    public static float yOffest = 0;
    public static float zOffest = 0;
    public static float scalemodifier = 0.625f;
    public static boolean tap;
    //----------------------------------
    /*
     * Only used for setting up scale, rotation, and relative placement coordinates
     */
    public static void transformCalibration() {
        int numsegments = 16;
        if (!tap) {

            if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
                xOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                xOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
                yOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
                yOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
                zOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
                zOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
                xtap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                ytap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
                ztap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
                xtap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
                ytap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                ztap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
                xtap = 0;
                ytap = 0;
                ztap = 0;

                xOffest = 0;
                yOffest = 0;
                zOffest = 0;

                scalemodifier = 1;

                tap = true;
            }
            // this probably needs a bit more work, int's are too big.
            if (Keyboard.isKeyDown(Keyboard.KEY_SCROLL)) {
                scalemodifier -= 0.01f;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_PAUSE)) {
                scalemodifier += 0.01f;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
                System.out.println("xrot: " + xtap + ", yrot: " + ytap + ", zrot: " + ztap);

                System.out.println("xOffest: " + xOffest + ", yOffest: " + yOffest + ", zOffest: " + zOffest);

                System.out.println("scaleModifier: " + scalemodifier);

                tap = true;
            }
        }
        else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)
                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)
                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                tap = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tap = false;
            }
        }
    }



    /*
 * Here we can color the quads using the setup below. This is better than changing material colors
 * for Wavefront models because it means that you can use a single material for the entire model
 * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
 */
    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : quadList) {
            builder.add(colorQuad(color, quad));
        }
        return builder.build();
    }

    public static BakedQuad colorQuad(Colour color, BakedQuad quad) {
        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat());
        quad.pipe(transformer);
        return transformer.build();
    }

    private static class ColorTransformer extends VertexTransformer {
        private final float r,g,b,a;

        public ColorTransformer(Colour color, VertexFormat format) {
            super(new UnpackedBakedQuad.Builder(format));
            this.r = (float)color.r;
            this.g = (float)color.g;
            this.b = (float)color.b;
            this.a = (float)color.a;
//            System.out.println("=======================================================");
//            System.out.println("r: " + this.r);
//            System.out.println("g: " + this.g);
//            System.out.println("b: " + this.b);
//            System.out.println("a: " + this.a);
//            System.out.println("=======================================================");
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
            // transform normals and position
            if(usage == VertexFormatElement.EnumUsage.COLOR && data.length >= 4) {
                data[0] = r;
                data[1] = g;
                data[2] = b;
                data[3] = a;
            }
            super.put(element, data);
        }

        public UnpackedBakedQuad build() {
            return ((UnpackedBakedQuad.Builder) parent).build();
        }
    }
}