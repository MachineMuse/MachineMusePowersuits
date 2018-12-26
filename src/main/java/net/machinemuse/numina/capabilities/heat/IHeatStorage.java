package net.machinemuse.numina.capabilities.heat;

/**
 * Same as ForgeEnergy/RF, except for heat
 */
public interface IHeatStorage {
    /**
     * Adds heat to the storage. Returns quantity of heat that was accepted.
     *
     * @param maxReceive Maximum amount of heat to be inserted.
     * @param simulate   If TRUE, the insertion will only be simulated.
     * @return Amount of heat that was (or would have been, if simulated) accepted by the storage.
     */
    double receiveHeat(double maxReceive, boolean simulate);

    /**
     * Removes heat from the storage. Returns quantity of heat that was removed.
     *
     * @param maxExtract Maximum amount of heat to be extracted.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of heat that was (or would have been, if simulated) extracted from the storage.
     */
    double extractHeat(double maxExtract, boolean simulate);

    /**
     * Returns the amount of heat currently stored.
     */
    double getHeatStored();

    /**
     * Returns the maximum amount of heat that can be stored.
     */
    double getMaxHeatStored();

    /**
     * Returns if this storage can have heat extracted.
     * If this is false, then any calls to extractheat will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive heat.
     * If this is false, then any calls to receiveHeat will return 0.
     */
    boolean canReceive();
}