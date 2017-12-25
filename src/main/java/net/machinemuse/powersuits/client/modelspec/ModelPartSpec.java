package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
@SideOnly(Side.CLIENT)
public class ModelPartSpec extends PartSpec {
    public boolean defaultglow;

    public ModelPartSpec(ModelSpec modelSpec,
                         Binding binding,
                         String partName,
                         String displayName,
                         Integer defaultcolourindex,
                         Boolean defaultglow) {
        super(modelSpec, binding, partName, displayName, defaultcolourindex);
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
    }

    public List<BakedQuad> getQuads() {
        return ((ModelSpec)(this.spec)).getModel().getQuadsforPart(this.partName);
    }

    public boolean getGlow() {
        return this.defaultglow;
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey("glow") ? nbt.getBoolean("glow") : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) nbt.removeTag("glow");
        else nbt.setBoolean("glow", g);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex, Boolean glow) {
        this.setGlow(nbt, (glow != null) ? glow : false);
        return super.multiSet(nbt, colourIndex);
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
        return Objects.hashCode(super.hashCode(), defaultglow);
    }
}