package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseRegistry;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.HashMap;
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
        if (!customData.containsKey("flip-v"))
            customData.put("flip-v", "true");
        MuseLogger.logDebug("Loading " + resource + " as " + MuseStringUtils.extractName(resource));
        IModel model = null;
        try {
            model = OBJLoader.INSTANCE.loadModel(resource);
            model = ((OBJModel) model).process(ImmutableMap.copyOf(customData));
        } catch (Exception e) {
            e.printStackTrace();
            MuseLogger.logError("Model loading failed :( " + resource);
            model = ModelLoaderRegistry.getMissingModel();
        }
        if (model == null)
            model = ModelLoaderRegistry.getMissingModel();

        IBakedModel bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
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