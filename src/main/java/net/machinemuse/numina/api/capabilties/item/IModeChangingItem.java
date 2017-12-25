package net.machinemuse.numina.api.capabilties.item;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:11 PM, 9/3/13
 *
 * Ported to Java by lehjr on 11/1/16.
 */
public interface IModeChangingItem {
    @SideOnly(Side.CLIENT)
    @Nullable
    TextureAtlasSprite getModeIcon(String mode, ItemStack stack, EntityPlayer player);

    List<String> getValidModes(ItemStack stack);

    String getActiveMode(ItemStack stack);

    void setActiveMode(ItemStack stack, String newMode);

    void cycleMode(ItemStack stack, EntityPlayer player, int dMode);

    String nextMode(ItemStack stack, EntityPlayer player);

    String prevMode(ItemStack stack, EntityPlayer player);
}
