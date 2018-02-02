package net.machinemuse.numina.capabilties;

import net.machinemuse.numina.api.item.IModeChangingItem;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ModeChangingCapability implements Capability.IStorage<IModeChangingItem> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IModeChangingItem> capability, IModeChangingItem instance, EnumFacing side) {
        //FIXME!!!

        return null;
    }

    @Override
    public void readNBT(Capability<IModeChangingItem> capability, IModeChangingItem instance, EnumFacing side, NBTBase nbt) {

    }
}
