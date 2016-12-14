package net.machinemuse.powersuits.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorChestplate extends ItemPowerArmor {
    final String iconpath = MuseRenderer.ICON_PREFIX + "armortorso";

    public ItemPowerArmorChestplate() {
        super(0, 1);
        setUnlocalizedName("powerArmorChestplate");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(iconpath);
    }
}
