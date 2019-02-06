package net.machinemuse.numina.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityPlayerValues implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(IPlayerValues.class)
    public static Capability<IPlayerValues> PLAYER_VALUES = null;
    private IPlayerValues instance = PLAYER_VALUES.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerValues.class, new Capability.IStorage<IPlayerValues>() {

                    // TODO: maybe store these in a byte. However, in future versions there might be more values to store.
                    @Override
                    public NBTBase writeNBT(Capability<IPlayerValues> capability, IPlayerValues instance, EnumFacing side) {
                        NBTTagCompound nbtOut = new NBTTagCompound();
                        nbtOut.setBoolean("jumping", instance.getJumpKeyState());
                        nbtOut.setBoolean("downKey", instance.getDownKeyState());
                        return nbtOut;
                    }

                    @Override
                    public void readNBT(Capability<IPlayerValues> capability, IPlayerValues instance, EnumFacing side, NBTBase nbt) {
                        instance.setJumpKeyState(((NBTTagCompound) nbt).getBoolean("jumping"));
                        instance.setJumpKeyState(((NBTTagCompound) nbt).getBoolean("downKey"));
                    }
                },
                () -> new PlayerValueStorage());
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_VALUES;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_VALUES ? PLAYER_VALUES.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return PLAYER_VALUES.getStorage().writeNBT(PLAYER_VALUES, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        PLAYER_VALUES.getStorage().readNBT(PLAYER_VALUES, this.instance, null, nbt);
    }
}