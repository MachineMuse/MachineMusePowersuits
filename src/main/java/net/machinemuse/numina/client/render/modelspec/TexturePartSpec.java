package net.machinemuse.numina.client.render.modelspec;

import com.google.common.base.Objects;
import net.minecraft.client.resources.I18n;

/**
 * This just provides a way to tie the armor skin for vanilla armor
 */
public class TexturePartSpec extends PartSpecBase {
    final String textureLocation;

    public TexturePartSpec(final SpecBase spec,
                           final SpecBinding binding,
                           final Integer enumColourIndex,
                           final String partName,
                           final String textureLocation) {
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