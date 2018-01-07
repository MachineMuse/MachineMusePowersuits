package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class PartSpec {
    EnumColour enumColourlour;
    public Byte defaultcolourindex; // NOT A COLOR but an index of a list. Used for nbt data
    public Spec spec;
    public String partName;
    public Binding binding;

    public PartSpec(Spec spec,
                    Binding binding,
                    String partName,
                    EnumColour enumColour) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        this.enumColourlour = enumColour;
    }

    public String getDisaplayName() {
        return "";
    }

    public int getDefaultColourIndex() {
        return this.defaultcolourindex != null ? Byte.toUnsignedInt(this.defaultcolourindex) : 0 ;
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? Byte.toUnsignedInt(nbt.getByte("colourindex")) : getDefaultColourIndex();
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == getDefaultColourIndex()) nbt.removeTag("colourindex");
        else nbt.setByte("colourindex", (byte)c);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString("part", this.partName);
    }

    public void setModel(NBTTagCompound nbt, Spec spec) {
        String modelString = ModelRegistry.getInstance().inverse().get(spec);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString("model", modelname);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex) {
        this.setPart(nbt);
        this.setColourIndex(nbt, (colourIndex != null) ? colourIndex : (defaultcolourindex != null ? defaultcolourindex : 0));
        this.setModel(nbt, this.spec);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSpec partSpec = (PartSpec) o;
        return enumColourlour == partSpec.enumColourlour &&
                Objects.equal(defaultcolourindex, partSpec.defaultcolourindex) &&
                Objects.equal(spec, partSpec.spec) &&
                Objects.equal(partName, partSpec.partName) &&
                Objects.equal(binding, partSpec.binding);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enumColourlour, defaultcolourindex, spec, partName, binding);
    }
}
