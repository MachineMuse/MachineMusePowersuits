package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.constants.NuminaModuleConstants;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ModularItemWrapper extends ItemStackHandler implements IModularItemCapability {
    protected ItemStack container;

    int slotCount;

    public ModularItemWrapper(@Nonnull ItemStack container, int slotCount, NBTTagCompound nbt) {
        super(slotCount);
        this.container = container;
        this.slotCount = slotCount;
        if (nbt != null)
            deserializeNBT(nbt);
    }

    @Override
    public boolean isModuleOnline(String unLocalizedName) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty()) {
                if (module.getUnlocalizedName().equals(unLocalizedName)) {
                    NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(module);
                    if (nbt.hasKey(NuminaModuleConstants.ONLINE))
                        return nbt.getBoolean(NuminaModuleConstants.ONLINE);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void toggleModule(String unLocalizedName, boolean online) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty()) {
                if (module.getUnlocalizedName().equals(unLocalizedName)) {
                    NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(module);
                    nbt.setBoolean(NuminaModuleConstants.ONLINE, online);
                }
            }
        }
    }

    @Override
    public void setModuleTweakInteger(String unLocalizedName, String tweakName, int teakVal) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty()) {
                if (module.getUnlocalizedName().equals(unLocalizedName)) {
                    NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(module);
                    nbt.setInteger(tweakName, teakVal);
                }
            }
        }
    }

    @Override
    public void setModuleTweakDouble(String unLocalizedName, String tweakName, double teakVal) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty()) {
                if (module.getUnlocalizedName().equals(unLocalizedName)) {
                    NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(module);
                    nbt.setDouble(tweakName, teakVal);
                }
            }
        }
    }

    @Override
    public List<String> getInstalledModuleNames() {
        List<String> moduleNames = new ArrayList<>();
        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty()) {
                moduleNames.add(module.getUnlocalizedName());
            }
        }
        return moduleNames;
    }

    @Override
    public NonNullList<ItemStack> getInstallledModules() {
        return stacks;
    }

    @Override
    public boolean isModuleInstalled(String unLocalizedName) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && stack.getUnlocalizedName().equals(unLocalizedName))
                return true;
        }
        return false;
    }

    @Override
    public boolean isModuleInstalled(@Nonnull ItemStack module) {
        for (int i= 0; i < slotCount; i ++) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && stack.getUnlocalizedName().equals(module.getUnlocalizedName()))
                return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public ItemStack installModule(@Nonnull ItemStack module) {
        if (isModuleInstalled(module))
            return module;

        for (int i= 0; i < slotCount; i ++) {
            if (getStackInSlot(i).isEmpty())
                return insertItem(i, module, false);
        }
        return module;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> removeModule(String moduleName) {
        NonNullList<ItemStack> retList = NonNullList.create();

        for (int i= 0; i < slotCount; i ++) {
            ItemStack module = getStackInSlot(i);
            if (!module.isEmpty() && moduleName.equals(module.getUnlocalizedName())) {
                retList.add(extractItem(i, getStackInSlot(i).getCount(), false));
            }
        }
        return retList;
    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(container);
        if (nbt != null && nbt.hasKey(NuminaNBTConstants.TAG_MODULES, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) nbt.getTag(NuminaNBTConstants.TAG_MODULES));

            // todo: is this even needed??
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




    // --------------------------------------------------------------------- //
    // IItemHandler

    @Override
    protected int getStackLimit(final int slot, @Nonnull final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        return 1;
    }

    // --------------------------------------------------------------------- //
    // ItemStackHandler

    @Override
    protected void onContentsChanged(final int slot) {
        super.onContentsChanged(slot);
        NBTTagCompound nbt = NuminaNBTUtils.getMuseItemTag(container);
        nbt.setTag(NuminaNBTConstants.TAG_MODULES, serializeNBT());
    }
}
