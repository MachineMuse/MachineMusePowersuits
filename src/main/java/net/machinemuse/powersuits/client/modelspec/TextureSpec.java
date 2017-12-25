package net.machinemuse.powersuits.client.modelspec;

/**
 * This is just a way of mapping a possible texture combinations for a piece of PowerArmor using the default vanilla model
 *
 */

public class TextureSpec extends Spec {
    public TextureSpec(String name, boolean isDefault) {
        super(name, isDefault, EnumSpecType.ARMOR_SKIN);
    }
}