package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public abstract class PartSpecBase {
    public final SpecBase spec;
    final String partName;
    final SpecBinding binding;
    Integer defaultcolourindex; // index value of NBTIntArray (array of colours as Int's.)

    public PartSpecBase(final SpecBase spec,
                        final SpecBinding binding,
                        final String partName,
                        final Integer defaultcolourindex) {
        this.spec = spec;
        this.partName = partName;
        this.binding = binding;
        if (defaultcolourindex != null && defaultcolourindex >= 0)
            this.defaultcolourindex =  defaultcolourindex;
        else
            this.defaultcolourindex = 0;
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
        String modelString = ModelRegistry.getInstance().getName(model);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString(NuminaNBTConstants.TAG_MODEL, modelname);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString(NuminaNBTConstants.TAG_PART, this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex) {
        this.setPart(nbt);
        this.setModel(nbt, this.spec);
        this.setColourIndex(nbt, (colourIndex != null) ? colourIndex : defaultcolourindex);
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
                Objects.equals(defaultcolourindex, that.defaultcolourindex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spec, partName, binding, defaultcolourindex);
    }
}