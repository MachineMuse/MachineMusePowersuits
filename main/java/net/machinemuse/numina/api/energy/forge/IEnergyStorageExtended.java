package net.machinemuse.numina.api.energy.forge;

import net.minecraftforge.energy.IEnergyStorage;

/**
 * Extends Forge Energy interface with setters and getters.
 */
public interface IEnergyStorageExtended extends IEnergyStorage {
    void setEnergy(int currentEnergy);

    void setCapacity(int maxEnergy);

    void setMaxReceive(int maxReceive);

    void setMaxExtract(int maxExtract);
}
