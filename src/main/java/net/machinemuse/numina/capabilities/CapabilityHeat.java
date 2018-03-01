package net.machinemuse.numina.capabilities;

import net.machinemuse.numina.api.capability_ports.heat.HeatStorage;
import net.machinemuse.numina.api.capability_ports.heat.IHeatStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

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
                        return new NBTTagInt(instance.getHeatStored());
                    }

                    @Override
                    public void readNBT(Capability<IHeatStorage> capability, IHeatStorage instance, EnumFacing side, NBTBase nbt) {
                        if (!(instance instanceof HeatStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                        ((HeatStorage) instance).setCurrentHeat(((NBTTagInt) nbt).getInt());
                    }
                },
                () -> new HeatStorage(1000));
    }
}