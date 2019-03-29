package net.machinemuse.numina.client.render.modelspec;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
@SideOnly(Side.CLIENT)
public class ModelPartSpec extends PartSpecBase {
    private final boolean defaultglow;
    static final String GLOW = "glow";

    public ModelPartSpec(final ModelSpec modelSpec,
                         final SpecBinding binding,
                         final String partName,
                         final Integer defaultColourIindex,
                         final Boolean defaultGlow) {
        super(modelSpec, binding, partName, defaultColourIindex);
        this.defaultglow = (defaultGlow != null) ? defaultGlow : false;
    }

    @Override
    public String getDisaplayName() {
        return I18n.format(new StringBuilder("model.")
                .append(this.spec.getOwnName())
                .append(".")
                .append(this.partName)
                .append(".partName")
                .toString());
    }

    public boolean getGlow() {
        return this.defaultglow;
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey(GLOW) ? nbt.getBoolean(GLOW) : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) nbt.removeTag(GLOW);
        else nbt.setBoolean(GLOW, g);
    }

    public List<BakedQuad> getQuads() {
        return ((ModelSpec) (this.spec)).getModel().getQuadsforPart(this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex, Boolean glow) {
        super.multiSet(nbt, colourIndex);
        this.setGlow(nbt, (glow != null) ? glow : false);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModelPartSpec that = (ModelPartSpec) o;
        return defaultglow == that.defaultglow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defaultglow);
    }
}

