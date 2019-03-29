package net.machinemuse.numina.client.render.modelspec;

import net.minecraft.client.resources.I18n;

/**
 * This is just a way of mapping a possible texture combinations for a piece of PowerArmor using the default vanilla model
 */
public class TextureSpec extends SpecBase {
    public TextureSpec(final String name, final boolean isDefault) {
        super(name, isDefault, EnumSpecType.ARMOR_SKIN);
    }

    @Override
    public String getDisaplayName() {
        return I18n.format(new StringBuilder("textureSpec.")
                .append(this.getName())
                .append(".specName")
                .toString());
    }

    @Override
    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }
}