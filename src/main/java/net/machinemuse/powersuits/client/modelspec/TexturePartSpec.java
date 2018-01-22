package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.minecraft.client.resources.I18n;

public class TexturePartSpec extends PartSpec {
    String textureLocation;
    public TexturePartSpec(Spec spec,
                           Binding binding,
                           byte enumColourIndex,
                           String partName,
                           String textureLocation) {
        super(spec, binding, partName, enumColourIndex);
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