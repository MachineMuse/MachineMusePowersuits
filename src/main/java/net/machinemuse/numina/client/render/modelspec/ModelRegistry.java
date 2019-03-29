package net.machinemuse.numina.client.render.modelspec;

import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.numina.client.model.obj.MuseOBJModel;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.map.MuseRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nullable;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 * <p>
 * Note: make sure to have null checks in place.
 */
public class ModelRegistry extends MuseRegistry<SpecBase> {
    private static volatile ModelRegistry INSTANCE;

    private ModelRegistry() {
    }

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

    @Nullable
    public static MuseOBJModel.MuseOBJBakedModel wrap(ResourceLocation modelLocation, IModelState modelState) {
        IBakedModel bakedModel = MuseModelHelper.getBakedOBJModel(modelLocation, modelState);
        return bakedModel instanceof MuseOBJModel.MuseOBJBakedModel ? (MuseOBJModel.MuseOBJBakedModel) bakedModel : null;
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
        return get(nbt.getString(ModelSpecTags.TAG_MODEL));
    }

    public PartSpecBase getPart(NBTTagCompound nbt, SpecBase model) {
        return model.get(nbt.getString(ModelSpecTags.TAG_PART));
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