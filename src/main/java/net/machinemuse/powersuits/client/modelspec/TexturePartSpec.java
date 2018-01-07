package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

public class TexturePartSpec extends PartSpec {
    String textureLocation;
    public TexturePartSpec(Spec spec,
                           Binding binding,
                           EnumColour enumColour,
                           String partName,
                           String textureLocation) {
        super(spec, binding, partName, enumColour);
        this.textureLocation = textureLocation;
    }

    @Override
    public String getDisaplayName() {
        return I18n.format(new StringBuilder("textureSpec.")
                .append(this.binding.getSlot().getName())
                .append(".partName")
                .toString());
    }

    public String getTextureLocation() {
        return textureLocation;
    }

    public void setTextureLocation(NBTTagCompound nbt, String textureLocation) {
        nbt.setString("texture", textureLocation);
        this.textureLocation = textureLocation;
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourindex, String textureLocationIn) {
        super.multiSet(nbt, colourindex);
        this.setTextureLocation(nbt, (textureLocationIn != null && !textureLocationIn.isEmpty()) ? textureLocationIn : textureLocation);
        return nbt;
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