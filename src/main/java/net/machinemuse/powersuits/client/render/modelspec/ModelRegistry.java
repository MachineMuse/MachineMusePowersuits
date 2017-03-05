package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseRegistry;
import net.machinemuse.powersuits.client.render.model.ModelHelper;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.item.DummyItem;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 *
 * Note: make sure to have null checks in place.
 */
public class ModelRegistry extends MuseRegistry<ModelSpec> {
    private static Map<String, String> customData = new HashMap<String, String>();
    private ModelRegistry(){
    }

    private static ModelRegistry INSTANCE;

    public static ModelRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModelRegistry();
        return INSTANCE;
    }

    public IBakedModel loadModel(ResourceLocation resource) {
        String name = MuseStringUtils.extractName(resource);
        ModelSpec spec = get(name);
        if (spec == null)
            return wrap(resource);
        return spec.getModel();
    }

    public IBakedModel wrap(ResourceLocation resource) {
        /*
            VertexFormat is DefaultVertexFormats.ITEM for both manually loaded and system loaded
            TextureAtlasSprite info is same for both



         */






        MuseLogger.logDebug("Loading " + resource + " as " + MuseStringUtils.extractName(resource));

        System.out.println("Loading " + resource + " as " + MuseStringUtils.extractName(resource));


        IModel testModel = null;
        try {
            testModel = ModelLoaderRegistry.getModel(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!customData.containsKey("flip-v"))
            customData.put("flip-v", "true");
        OBJModel model = null;
        try {
            if (!(testModel instanceof OBJModel)) {
                model = (OBJModel) OBJLoader.INSTANCE.loadModel(resource);
                System.out.println("loaded model through OBJLoader");
            } else {
                System.out.println("loaded model through model loader registry");
                model = (OBJModel) testModel;
            }

            model = (OBJModel) model.process(ImmutableMap.copyOf(customData));
        } catch (Exception e) {
            e.printStackTrace();
            MuseLogger.logError("Model loading failed :( " + resource);
            return null;
        }

        IBakedModel bakedTestModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                new Function<ResourceLocation, TextureAtlasSprite>() {
                    public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                        System.out.println("resource location: " + resourceLocation.toString());

                        if (resourceLocation.getResourceDomain().equalsIgnoreCase("powersuits"))
                            System.out.println("texture is: " + Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation).toString());
                        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                    }
                });

        // minecraft:missingno
        // powersuits:models/diffuse
        // TextureAtlasSprite{name='powersuits:models/diffuse', frameCount=1, rotated=false, x=1024, y=512, height=512, width=512, u0=0.25, u1=0.375, v0=0.25, v1=0.5}
        // resource location: minecraft:builtin/white
        // resource location: minecraft:builtin/white

// manual bake
// TextureAtlasSprite{name='powersuits:models/diffuse', frameCount=1, rotated=false, x=1024, y=512, height=512, width=512, u0=0.25, u1=0.375, v0=0.25, v1=0.5}



//                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));




        IBakedModel bakedModel = ((DummyItem)MPSItems.dummies).getModel(resource);
        System.out.println("MODEL resource path: " + resource.getResourcePath());
        if (resource.getResourcePath().contains("mps_boots.obj")) {
            IExtendedBlockState extendedBlockState = ModelHelper.getStateForPart("boots1", (OBJModel.OBJBakedModel) bakedTestModel );

            System.out.println("boot quad info: ======================");
            System.out.println("manually baked model:");
            List<BakedQuad> quadList;

            quadList = bakedTestModel.getQuads(extendedBlockState, null, 0);

            for (BakedQuad quad : quadList) {
                System.out.println("boot quad texture: " + quad.getSprite().toString());
                String formatString;
                VertexFormat vertFormat = quad.getFormat();

                if (vertFormat == DefaultVertexFormats.ITEM)
                    formatString = "ITEM";
                else if (vertFormat == DefaultVertexFormats.BLOCK)
                    formatString = "BLOCK";
                else
                    formatString = "OTHER";
                System.out.print("Vertex format: " + formatString);
            }

            extendedBlockState = ModelHelper.getStateForPart("boots1", (OBJModel.OBJBakedModel) bakedModel );
            System.out.println("System baked model:");

            quadList = bakedModel.getQuads(extendedBlockState, null, 0);

            for (BakedQuad quad : quadList) {
                System.out.println("boot quad texture: " + quad.getSprite().toString());
                String formatString;
                VertexFormat vertFormat = quad.getFormat();

                if (vertFormat == DefaultVertexFormats.ITEM)
                    formatString = "ITEM";
                else if (vertFormat == DefaultVertexFormats.BLOCK)
                    formatString = "BLOCK";
                else
                    formatString = "OTHER";
                System.out.print("Vertex format: " + formatString);

            }

        }

        if (bakedModel instanceof OBJModel.OBJBakedModel)
            return bakedModel;
        MuseLogger.logError("Model loading failed :( " + resource);
        return null;
    }

    public ModelSpec getModel(NBTTagCompound nbt) {
        return get(nbt.getString("model"));
    }

    public ModelPartSpec getPart(NBTTagCompound nbt, ModelSpec model) {
        return model.get(nbt.getString("part"));
    }

    public ModelPartSpec getPart(NBTTagCompound nbt) {
        return getPart(nbt, getModel(nbt));
    }

    public NBTTagCompound getSpecTag(NBTTagCompound museRenderTag, ModelPartSpec spec) {
        String name = makeName(spec);
        return (museRenderTag.hasKey(name)) ? (museRenderTag.getCompoundTag(name)) : null;
    }

    public String makeName(ModelPartSpec spec) {
        return spec.modelSpec.getOwnName() + "." + spec.partName;
    }
}