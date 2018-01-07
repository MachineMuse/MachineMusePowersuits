package net.machinemuse.powersuits.client.modelspec;

import net.minecraft.client.resources.I18n;

/**
 * This is just a way of mapping a possible texture combinations for a piece of PowerArmor using the default vanilla model
 *
 */

public class TextureSpec extends Spec {
    public TextureSpec(String name, boolean isDefault) {
        super(name, isDefault, EnumSpecType.ARMOR_SKIN);
    }

    @Override
    public String getDisaplayName() {
        return I18n.format(new StringBuilder("textureSpec.")
                .append(this.getName())
                .append(".specName")
                .toString());
    }
}