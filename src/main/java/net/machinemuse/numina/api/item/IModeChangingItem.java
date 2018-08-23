package net.machinemuse.numina.api.item;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
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

    default String getActiveMode(ItemStack stack) {
        String modeFromNBT = MuseNBTUtils.getMuseItemTag(stack).getString(NuminaNBTConstants.TAG_MODE);
        if (modeFromNBT.isEmpty()) {
            List<String> validModes = getValidModes(stack);
            return (validModes!=null && (validModes.size() > 0) ? validModes.get(0) : "");
        }
        else
            return modeFromNBT;
    }

    default void setActiveMode(ItemStack stack, String newMode) {
        MuseNBTUtils.getMuseItemTag(stack).setString(NuminaNBTConstants.TAG_MODE, newMode);
    }

    default void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(this.getActiveMode(stack)) + dMode, modes.size());
            String newmode = (String)modes.get(newindex);
            this.setActiveMode(stack, newmode);
            PacketSender.sendToServer(new MusePacketModeChangeRequest(player, newmode, player.inventory.currentItem));
        }
    }

    /* nextMode and prevMode are used for getting the icons to display in the mode selection */
    default String nextMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) + 1, modes.size());
            return (String)modes.get(newindex);
        }
        else return "";
    }

    default String prevMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) - 1, modes.size());
            return (String)modes.get(newindex);
        }
        else return "";
    }

    default int clampMode(int selection, int modesSize) {
        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
    }
}
