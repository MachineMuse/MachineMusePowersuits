package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.common.MPSConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TexturePartSpec extends PartSpec {
    String textureLocation;
    public TexturePartSpec(Spec spec, Binding binding, String partName, String displayName, String textureLocation, Integer defaultcolourindex) {
        super(spec, binding, partName, displayName, defaultcolourindex);
        this.textureLocation = textureLocation;
    }

    public String getTextureLocation() {
        return textureLocation;
    }

    public void setTextureLocation(NBTTagCompound nbt, String textureLocation) {
        nbt.setString("texture", textureLocation);
        this.textureLocation = textureLocation;
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, String textureLocationIn, Integer colourindex) {
        this.setTextureLocation(nbt, textureLocationIn != null ? textureLocationIn : textureLocation);
        return super.multiSet(nbt, colourindex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TexturePartSpec that = (TexturePartSpec) o;
        return Objects.equal(getTextureLocation(), that.getTextureLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getTextureLocation());
    }
}