package net.machinemuse.numina.client.render.modelspec;

import net.machinemuse.numina.constants.ModelSpecTags;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public abstract class PartSpecBase {
    static final String COLOUR_INDEX = "colourindex";
    public final SpecBase spec;
    final String partName;
    final SpecBinding binding;
    Integer defaultColourIndex; // index getValue of NBTIntArray (array of colours as Int's.)

    public PartSpecBase(final SpecBase spec,
                        final SpecBinding binding,
                        final String partName,
                        final Integer defaultColourIndex) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        if (defaultColourIndex != null && defaultColourIndex >= 0)
            this.defaultColourIndex = defaultColourIndex;
        else
            this.defaultColourIndex = 0;
    }

    public int getDefaultColourIndex() {
        return defaultColourIndex;
    }

    public SpecBinding getBinding() {
        return binding;
    }

    public abstract String getDisaplayName();

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey(COLOUR_INDEX) ? nbt.getInteger(COLOUR_INDEX) : this.defaultColourIndex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultColourIndex) nbt.removeTag(COLOUR_INDEX);
        else nbt.setInteger(COLOUR_INDEX, c);
    }

    public void setModel(NBTTagCompound nbt, SpecBase model) {
        String modelString = ModelRegistry.getInstance().getName(model);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString(ModelSpecTags.TAG_MODEL, modelname);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString(ModelSpecTags.TAG_PART, this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex) {
        this.setPart(nbt);
        this.setModel(nbt, this.spec);
        this.setColourIndex(nbt, (colourIndex != null) ? colourIndex : defaultColourIndex);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSpecBase that = (PartSpecBase) o;
        return Objects.equals(spec, that.spec) &&
                Objects.equals(partName, that.partName) &&
                Objects.equals(binding, that.binding) &&
                Objects.equals(defaultColourIndex, that.defaultColourIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spec, partName, binding, defaultColourIndex);
    }
}