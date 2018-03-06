package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.constants.NuminaModuleConstants;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
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
                    NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(module);
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
                    NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(module);
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
                    NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(module);
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
                    NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(module);
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



        // TODO: return module and install costs


        List<ItemStack> retList = new ArrayList<>();
        retList.add(ItemStack.EMPTY);

        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        System.out.println("trying to insert item");
/*
 itemTag: {"Maximum Heat":900000,"Current Heat":0,item.powersuits.module.debugModule:{Active:1b},render:{}}

 */

        if (stack.isEmpty())
            System.out.println("stack is empty!!!");
        else
            System.out.println("stack is NOT empty!!!");

        System.out.println("serialize: " + serializeNBT());

        return super.insertItem(slot, stack, simulate);
    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);
        if (nbt != null && nbt.hasKey(NuminaNBTConstants.TAG_MODULES, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) nbt.getTag(NuminaNBTConstants.TAG_MODULES));

            if (stacks.size() != slotCount) {
                final List<ItemStack> oldStacks = new ArrayList<>(stacks);
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
        NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);

        System.out.println("nbt: " + nbt) ;
        nbt.setTag(NuminaNBTConstants.TAG_MODULES, serializeNBT());
    }
}
