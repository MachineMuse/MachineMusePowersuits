package net.machinemuse.powersuits.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorLeggings extends ItemPowerArmor {
    private final String iconpath = MuseRenderer.ICON_PREFIX + "armorlegs";

    public ItemPowerArmorLeggings() {
        super(0, 2);
        this.setUnlocalizedName("powerArmorLeggings");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(this.iconpath);
    }
}