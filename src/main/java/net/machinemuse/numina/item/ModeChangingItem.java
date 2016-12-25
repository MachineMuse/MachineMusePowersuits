package net.machinemuse.numina.item;

import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:11 PM, 9/3/13
 *
 * Ported to Java by lehjr on 11/1/16.
 */
public class ModeChangingItem implements IModeChangingItem {
    private static Item mcItem;

    public ModeChangingItem(ItemStack mcitemStack) {
        ItemStack mcitemStack1 = mcitemStack;
        this.mcItem = (mcitemStack != null && mcitemStack.getItem() instanceof IModeChangingItem) ? mcitemStack.getItem() : null;
    }

    public ModeChangingItem(Item mcItem) {
        this.mcItem = (mcItem != null && mcItem instanceof IModeChangingItem) ? mcItem : null;
    }

    @Nullable
    @Override
    public TextureAtlasSprite getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
        if (this.mcItem != null)
            return ((IModeChangingItem) mcItem).getModeIcon(mode, stack, player);
        return null;
    }

    @Override
    public List<String> getValidModes(ItemStack stack) {
        List<String> modes = new ArrayList<>();
        if (this.mcItem != null)
            return ((IModeChangingItem) mcItem).getValidModes(stack);
        return modes;
    }

    @Override
    public String getActiveMode(ItemStack stack) {
        String modeFromNBT = NuminaItemUtils.getTagCompound(stack).getString("mode");
        if (modeFromNBT.isEmpty()) {
            List<String> validModes = getValidModes(stack);
            return (validModes!=null && (validModes.size() > 0) ? validModes.get(0) : "");
        }
        else {
            return modeFromNBT;
        }
    }

    @Override
    public void setActiveMode(ItemStack stack, String newMode) {
        NuminaItemUtils.getTagCompound(stack).setString("mode", newMode);
    }

    @Override
    public void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(this.getActiveMode(stack)) + dMode, modes.size());
            String newmode = (String)modes.get(newindex);
            this.setActiveMode(stack, newmode);
            PacketSender.sendToServer(new MusePacketModeChangeRequest(player, newmode, player.inventory.currentItem));
        }
    }

    private static int clampMode(int selection, int modesSize) {
        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
    }


    /* nextMode and prevMode are used for getting the icons to display in the mode selection */
    @Override
    public String nextMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) + 1, modes.size());
            return (String)modes.get(newindex);
        }
        else return "";
    }

    @Override
    public String prevMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) - 1, modes.size());
            return (String)modes.get(newindex);
        }
        else return "";
    }
}