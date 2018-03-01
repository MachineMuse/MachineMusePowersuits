package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This is the base class for TexturePartSpec and ModelPartSpec.
 * This class is not meant to be used directly.
 */
public abstract class PartSpec {
    public Spec spec;
    final String partName;
    final Binding binding;
    Integer defaultcolourindex; // index value of NBTIntArray (array of colours as Int's.)

    public PartSpec(Spec spec,
                    Binding binding,
                    String partName,
                    Integer defaultcolourindex) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
//        this.displayName = displayName; // use translation file
    }

    public Binding getBinding() {
        return binding;
    }

    public abstract String getDisaplayName();

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : this.defaultcolourindex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultcolourindex) nbt.removeTag("colourindex");
        else nbt.setInteger("colourindex", c);
    }

    public void setModel(NBTTagCompound nbt, Spec model) {
        String modelString = ModelRegistry.getInstance().inverse().get(model);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString("model", modelname);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString("part", this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourInt) {
        this.setPart(nbt);
        this.setModel(nbt, this.spec);
        this.setColourIndex(nbt, (colourInt != null) ? colourInt : defaultcolourindex);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSpec partSpec = (PartSpec) o;
        return Objects.equal(spec, partSpec.spec) &&
                Objects.equal(partName, partSpec.partName) &&
                Objects.equal(getBinding(), partSpec.getBinding()) &&
                Objects.equal(defaultcolourindex, partSpec.defaultcolourindex);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(spec, partName, getBinding(), defaultcolourindex);
    }
}
