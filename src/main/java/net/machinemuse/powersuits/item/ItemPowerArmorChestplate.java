package net.machinemuse.powersuits.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorChestplate extends ItemPowerArmor {

    public ItemPowerArmorChestplate() {
        super(0, 1);
        this.setUnlocalizedName("powerArmorChestplate");
    }

    public String iconpath() {
        String iconpath = MuseRenderer.ICON_PREFIX + "armortorso";
        return iconpath;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(this.iconpath());
    }
}