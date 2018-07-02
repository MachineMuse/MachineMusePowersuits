package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public abstract class PartSpecBase {
    public SpecBase spec;
    final String partName;
    final SpecBinding binding;
    Integer defaultcolourindex; // index value of NBTIntArray (array of colours as Int's.)

    public PartSpecBase(SpecBase spec,
                        SpecBinding binding,
                        String partName,
                        Integer defaultcolourindex) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
//        this.displayName = displayName; // use translation file
    }

    public SpecBinding getBinding() {
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

    public void setModel(NBTTagCompound nbt, SpecBase model) {
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
        PartSpecBase partSpec = (PartSpecBase) o;
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