package net.machinemuse.numina.capabilities.heat;

import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;

/**
 * Forge Energy, but with Heat
 */
public class CapabilityHeat {
    @CapabilityInject(IHeatStorage.class)
    public static Capability<IHeatStorage> HEAT = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IHeatStorage.class, new Capability.IStorage<IHeatStorage>() {
                    @Override
                    public NBTBase writeNBT(Capability<IHeatStorage> capability, IHeatStorage instance, EnumFacing side) {
                        NBTTagCompound nbtOut = new NBTTagCompound();
                        if (instance.getHeatStored() > 0)
                            nbtOut.setDouble(NuminaNBTConstants.CURRENT_HEAT, ((HeatStorage) instance).heat);
                        nbtOut.setDouble(NuminaNBTConstants.MAXIMUM_HEAT, ((HeatStorage) instance).capacity);
                        return nbtOut;
                    }

                    @Override
                    public void readNBT(Capability<IHeatStorage> capability, IHeatStorage instance, EnumFacing side, NBTBase nbt) {
                        if (!(instance instanceof HeatStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

                        if (((NBTTagCompound) nbt).hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_DOUBLE))
                            ((HeatStorage) instance).heat = ((NBTTagCompound) nbt).getDouble(NuminaNBTConstants.CURRENT_HEAT);
                        else
                            ((HeatStorage) instance).heat = 0;
                        ((HeatStorage) instance).capacity = ((NBTTagCompound) nbt).getDouble(NuminaNBTConstants.MAXIMUM_HEAT);
                    }
                },
                () -> new HeatStorage(1000));
    }
}