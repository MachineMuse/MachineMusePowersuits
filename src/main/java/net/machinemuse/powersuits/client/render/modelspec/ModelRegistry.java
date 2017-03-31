package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseRegistry;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 *
 * Note: make sure to have null checks in place.
 */
@SideOnly(Side.CLIENT)
public class ModelRegistry extends MuseRegistry<ModelSpec> {
    private ModelRegistry(){
    }
    private static ModelRegistry INSTANCE;

    public static ModelRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModelRegistry();
        return INSTANCE;
    }

    private IModel getModel(ResourceLocation resource){
        IModel model = null;
        try {
            model = (OBJModel) OBJLoader.INSTANCE.loadModel(resource);
            model = ((OBJModel) model).process(ImmutableMap.copyOf(ImmutableMap.of("flip-v", "true")));
        } catch (Exception e) {
            e.printStackTrace();
            MuseLogger.logError("Model loading failed :( " + resource);
        }
        return model;
    }

    public IBakedModel loadBakedModel(ResourceLocation resource) {
        String name = MuseStringUtils.extractName(resource);
        ModelSpec spec = get(name);
        if (spec == null)
            return wrap(resource);
        return spec.getModel();
    }

    public IBakedModel wrap(ResourceLocation resource) {
        IModel model = getModel(resource);
        model.getTextures();
        IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
                new Function<ResourceLocation, TextureAtlasSprite>() {
                    public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                    }
                });
        return bakedModel;
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