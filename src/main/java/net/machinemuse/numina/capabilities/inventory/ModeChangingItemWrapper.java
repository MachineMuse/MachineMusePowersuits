package net.machinemuse.numina.capabilities.inventory;

import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModeChangingItemWrapper extends ModularItemWrapper implements IModeChangingItemCapability {
    public static final String TAG_MODE = "mode";
    int activeMode = -1;

    public ModeChangingItemWrapper(ItemStack container, int slotCount, NBTTagCompound nbt) {
        super(container, slotCount, nbt);
    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = MuseNBTUtils.getMuseItemTag(container);
        if (nbt != null && nbt.hasKey(NuminaNBTConstants.TAG_MODULES, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) nbt.getTag(NuminaNBTConstants.TAG_MODULES));

            // TODO: is this even needed???
            if (stacks.size() != slotCount) {
                final NonNullList<ItemStack> oldStacks = stacks;
                setSize(slotCount); // FIXME : use config reference?
                final int count = Math.min(slotCount, oldStacks.size());
                for (int slot = 0; slot < count; slot++) {
                    stacks.set(slot, oldStacks.get(slot));
                }
            }
        }
    }

    @Nullable
    @Override
    public TextureAtlasSprite getModeIcon(int modeIndex) {
        if (modeIndex == -1)
            return null;
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(getStackInSlot(modeIndex)).getParticleTexture();
    }

    @Override
    public List<Integer> getValidModes() {
        List<Integer>moduleIndexes = new ArrayList<>();
        for(int i=0; i < getSlots();  i++) {
            if (getStackInSlot(i).getItem() instanceof IRightClickModule)
                moduleIndexes.add(i);
        }
        return moduleIndexes;
    }

    @Override
    public boolean isValidMode(String mode) {
        for(int i=0; i < getSlots();  i++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty() && module.getItem() instanceof IRightClickModule && module.getTranslationKey().equals(mode))
                return true;
        }
        return false;
    }

    @Override
    public ItemStack getActiveModule() {
        int activeModeIndex = getActiveMode();
        return activeModeIndex != -1 ? getStackInSlot(activeModeIndex) : ItemStack.EMPTY;
    }

    @Override
    public int getActiveMode() {
        return activeMode;
    }

    @Override
    public void setActiveMode(String unLocalizedName) {
        for(int i=0; i < getSlots();  i++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty() && module.getTranslationKey().equals(unLocalizedName))
                setActiveMode(i);
        }
    }

    @Override
    public void setActiveMode(int newMode) {
        final NBTTagCompound nbt = MuseNBTUtils.getMuseItemTag(container);
        if (nbt != null && nbt.hasKey(NuminaNBTConstants.TAG_MODULES, Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound moduleNBT = (NBTTagCompound) nbt.getTag(NuminaNBTConstants.TAG_MODULES);
            moduleNBT.setInteger(TAG_MODE, newMode);
            deserializeNBT(moduleNBT);
        }
    }

    @Override
    public void cycleMode(EntityPlayer player, int dMode) {
        List<Integer> modes = this.getValidModes();
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(this.getActiveMode()) + dMode, modes.size());
            int newmode = modes.get(newindex);
            this.setActiveMode(newmode);
            System.out.println("this is where the packet would normally update");

            // fixme
//            PacketSender.sendToServer(new MusePacketModeChangeRequest(player, newmode, player.inventory.currentItem));
        }
    }

    @Override
    public int nextMode() {
        List<Integer> modes = this.getValidModes();
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode()) + 1, modes.size());
            return modes.get(newindex);
        }
        else return -1;
    }

    @Override
    public int prevMode() {
        List<Integer> modes = this.getValidModes();
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode()) - 1, modes.size());
            return modes.get(newindex);
        }
        else return -1;
    }

    private static int clampMode(int selection, int modesSize) {
        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        nbt.setInteger(TAG_MODE, this.activeMode);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(TAG_MODE))
            this.activeMode = nbt.getInteger(TAG_MODE);
        else
            this.activeMode = -1;
        super.deserializeNBT(nbt);
    }
}