package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagCompound;

public class PartSpec {
    public int defaultcolourindex; // NOT A COLOR but an index of a list
    public Spec spec;
    public String partName;
    public String displayName;
    public Binding binding;

    public PartSpec(Spec spec,
                    Binding binding,
                    String partName,
                    String displayName,
                    Integer defaultcolourindex) {
        this.spec = spec;
        this.partName = partName;
        this.displayName = displayName; // TODO: actual translations
        this.binding = binding;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
    }

    public void setModel(NBTTagCompound nbt, Spec spec) {
        String modelString = ModelRegistry.getInstance().inverse().get(spec);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString("model", modelname);
    }

    public int getDefaultColourIndex() {
        return this.defaultcolourindex;
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : this.defaultcolourindex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultcolourindex) nbt.removeTag("colourindex");
        else nbt.setInteger("colourindex", c);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString("part", this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex) {
        this.setPart(nbt);
        this.setColourIndex(nbt, (colourIndex != null) ? colourIndex : defaultcolourindex);
        this.setModel(nbt, this.spec);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSpec partSpec = (PartSpec) o;
        return defaultcolourindex == partSpec.defaultcolourindex &&
                Objects.equal(spec, partSpec.spec) &&
                Objects.equal(partName, partSpec.partName) &&
                Objects.equal(displayName, partSpec.displayName) &&
                Objects.equal(binding, partSpec.binding);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(defaultcolourindex, spec, partName, displayName, binding);
    }
}
