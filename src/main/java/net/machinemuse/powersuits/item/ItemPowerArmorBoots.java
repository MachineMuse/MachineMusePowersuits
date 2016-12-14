package net.machinemuse.powersuits.item;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IMetalArmor;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList ({
        @Optional.Interface (iface = "ic2.api.item.IMetalArmor", modid = "IC2", striprefs = true)
})
public class ItemPowerArmorBoots extends ItemPowerArmor implements IMetalArmor{
    public final String iconpath = MuseRenderer.ICON_PREFIX + "armorfeet";

    public ItemPowerArmorBoots() {
        super(0, 3);
        setUnlocalizedName("powerArmorBoots");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(iconpath);
    }

    @Override
    public boolean isMetalArmor(ItemStack itemStack, EntityPlayer entityPlayer) {
        return true;
    }
}
