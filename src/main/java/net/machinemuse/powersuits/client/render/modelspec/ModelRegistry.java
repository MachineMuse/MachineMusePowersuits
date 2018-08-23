package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.map.MuseRegistry;
import net.machinemuse.powersuits.client.model.obj.OBJModelPlus;
import net.machinemuse.powersuits.client.model.obj.OBJPlusLoader;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 *
 * Note: make sure to have null checks in place.
 */
@SideOnly(Side.CLIENT)
public class ModelRegistry extends MuseRegistry<SpecBase> {
    private ModelRegistry(){
    }
    private static volatile ModelRegistry INSTANCE;

    public static ModelRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelRegistry();
                }
            }
        }
        return INSTANCE;
    }

    public static OBJModelPlus getIModel(ResourceLocation location, int attempt) {
        String domain = location.getResourceDomain();
        String resourePath = location.getResourcePath().replaceFirst("^models/models", "models");

        location = new ResourceLocation(domain, resourePath);
        IModel model;
        try {
            model = OBJPlusLoader.INSTANCE.loadModel(location);
            model = ((OBJModelPlus) model).process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                getIModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else
                return (OBJModelPlus) model;
            MuseLogger.logError("Failed to load model. " + e);
        }
        return (OBJModelPlus) model;
    }

    /**
     * TextureSpec does not have an IModelState so this is relatively safe
     */
    public OBJModelPlus.OBJBakedModelPus loadBakedModel(ResourceLocation resource, IModelState modelState) {
        String name = MuseStringUtils.extractName(resource);
        SpecBase spec = get(name);
        if (spec == null)
            return wrap(resource, modelState);
        return ((ModelSpec)(spec)).getModel();
    }

    @Nullable
    public static OBJModelPlus.OBJBakedModelPus wrap(ResourceLocation modellocation, IModelState modelState) {
        OBJModelPlus model = getIModel(modellocation, 0);

        try {
            return (OBJModelPlus.OBJBakedModelPus) model.bake(modelState, DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e) {
            MuseLogger.logError("Failed to bake model. " + e);
        }
        return null;
    }

    public Iterable<SpecBase> getSpecs() {
        return this.elems();
    }

    public Iterable<String> getNames() {
        return this.names();
    }

    /**
     * FIXME: texture spec needs a model tag for this to work. Model tag does not have to be a real model, just a unique string for the spec k-v pair
     */
    public SpecBase getModel(NBTTagCompound nbt) {
        return get(nbt.getString(NuminaNBTConstants.TAG_MODEL));
    }

    public PartSpecBase getPart(NBTTagCompound nbt, SpecBase model) {
        return model.get(nbt.getString(NuminaNBTConstants.TAG_PART));
    }

    public PartSpecBase getPart(NBTTagCompound nbt) {
        return getPart(nbt, getModel(nbt));
    }

    public NBTTagCompound getSpecTag(NBTTagCompound museRenderTag, PartSpecBase spec) {
        String name = makeName(spec);
        return (museRenderTag.hasKey(name)) ? (museRenderTag.getCompoundTag(name)) : null;
    }

    public String makeName(PartSpecBase spec) {
        return spec.spec.getOwnName() + "." + spec.partName;
    }
}