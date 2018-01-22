package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This is the base class for TexturePartSpec and ModelPartSpec.
 * This class is not meant to be used directly.
 */
public class PartSpec {
    final byte enumColourlourIndex; // default EnumColour index value
    public Byte defaultColourArrayIndex; // index value of NBTByteArray (array of EnumColour indexes. Smaller than tracking duplicate values individually)
    public Spec spec;
    final String partName;
    final Binding binding;

    public PartSpec(Spec spec,
                    Binding binding,
                    String partName,
                    byte enumColourIndex) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        this.enumColourlourIndex = enumColourIndex;
    }

    public Binding getBinding() {
        return binding;
    }

    public String getDisaplayName() {
        return "";
    }

    public int getDefaultColourArrayIndex() {
        return this.defaultColourArrayIndex != null ? Byte.toUnsignedInt(this.defaultColourArrayIndex) : 0 ;
    }

    public void setDefaultColourArrayIndex(byte index) {
        defaultColourArrayIndex = index;
    }

    public byte getEnumColourIndex() {
        return enumColourlourIndex;
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? Byte.toUnsignedInt(nbt.getByte("colourindex")) : getDefaultColourArrayIndex();
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == getDefaultColourArrayIndex()) nbt.removeTag("colourindex");
        else nbt.setByte("colourindex", (byte)c);
    }

    public void setColourIndex(NBTTagCompound nbt, byte c) {
        if (c == getDefaultColourArrayIndex()) nbt.removeTag("colourindex");
        else nbt.setByte("colourindex", c);
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

    public NBTTagCompound multiSet(NBTTagCompound nbt, Byte colourIndex) {
        this.setPart(nbt);
        this.setColourIndex(nbt, (colourIndex != null) ? (byte)colourIndex : (defaultColourArrayIndex != null ? defaultColourArrayIndex : 0));
        this.setModel(nbt, this.spec);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSpec partSpec = (PartSpec) o;
        return enumColourlourIndex == partSpec.enumColourlourIndex &&
                Objects.equal(defaultColourArrayIndex, partSpec.defaultColourArrayIndex) &&
                Objects.equal(spec, partSpec.spec) &&
                Objects.equal(partName, partSpec.partName) &&
                Objects.equal(binding, partSpec.binding);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enumColourlourIndex, defaultColourArrayIndex, spec, partName, binding);
    }
}
