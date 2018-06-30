package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.powersuits.client.helper.ModelHelper;
import net.machinemuse.powersuits.client.model.obj.OBJModelPlus;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelPartSpec {
    public ModelSpec modelSpec;
    public MorphTarget morph;
    public String partName;
    public EntityEquipmentSlot slot;
    public int defaultcolourindex; // NOT A COLOR but an index of a list
    public boolean defaultglow;
    public String displayName;
//    IExtendedBlockState extendedState;
    List<BakedQuad> quadcache = new ArrayList<>();

    public ModelPartSpec(ModelSpec modelSpec, MorphTarget morph, String partName, EntityEquipmentSlot slot, Integer defaultcolourindex, Boolean defaultglow, String displayName) {
        this.modelSpec = modelSpec;
        this.morph = morph;
        this.partName = partName;
        this.slot = slot;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
        this.displayName = displayName;
        // Extended state is used to isolate the quads for the model "group"(part) associated with this
//        this.extendedState = ModelHelper.getStateForPart(partName, (OBJModelPlus.OBJBakedModelPus) modelSpec.getModel(), TRSRTransformation.identity());
    }

    public List<BakedQuad> getQuads() {
        if (quadcache.isEmpty()) {
            quadcache = modelSpec.getModel().getQuadsforPart(this.partName);
        }
        return quadcache;
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : this.defaultcolourindex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultcolourindex) nbt.removeTag("colourindex");
        else nbt.setInteger("colourindex", c);
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey("glow") ? nbt.getBoolean("glow") : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) nbt.removeTag("glow");
        else nbt.setBoolean("glow", g);
    }

    public void setModel(NBTTagCompound nbt, ModelSpec model) {
        String modelString = ModelRegistry.getInstance().inverse().get(model);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString("model", modelname);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString("part", this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, String tex, Boolean glow, Integer c) {
        this.setPart(nbt);
        this.setModel(nbt, this.modelSpec);
        this.setGlow(nbt, (glow != null) ? glow : false);
        this.setColourIndex(nbt, (c != null) ? c : defaultcolourindex);
        return nbt;
    }
}