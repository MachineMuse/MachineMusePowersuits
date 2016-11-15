package net.machinemuse.powersuits.client.render.modelspec;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelPartSpec
{
    public ModelSpec modelSpec;
    public MorphTarget morph;
    public String partName;
    public int slot;
    public int defaultcolourindex;
    public boolean defaultglow;
    public String displayName;

    public ModelPartSpec(ModelSpec modelSpec, MorphTarget morph, String partName, int slot, Integer defaultcolourindex, Boolean defaultglow, String displayName) {
        this.modelSpec = modelSpec;
        this.morph = morph;
        this.partName = partName;
        this.slot = slot;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
        this.displayName = displayName;
    }

    public String getTexture(NBTTagCompound nbt) {
        return (nbt.hasKey("texture") ?  nbt.getString("texture") :  modelSpec.textures[0]);
    }

    public void setTexture(NBTTagCompound nbt, String s) {
        if (s.equals("") || s.equalsIgnoreCase(modelSpec.textures[0])) {
            nbt.removeTag("texture");
        }
        else {
            nbt.setString("texture", s);
        }
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : this.defaultcolourindex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultcolourindex) {
            nbt.removeTag("colourindex");
        }
        else {
            nbt.setInteger("colourindex", c);
        }
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey("glow") ? nbt.getBoolean("glow") : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) {
            nbt.removeTag("glow");
        }
        else {
            nbt.setBoolean("glow", g);
        }
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

    public void setPartAndModel(NBTTagCompound nbt) {
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, String tex, Boolean glow, Integer c) {
        this.setPart(nbt);
        this.setModel(nbt, this.modelSpec);
        this.setTexture(nbt, (tex != null) ? tex : "");
        this.setGlow(nbt, (glow != null) ? glow : false);
        this.setColourIndex(nbt, (c != null) ? c : defaultcolourindex);
        return nbt;
    }
}