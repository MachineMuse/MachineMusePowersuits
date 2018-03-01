package net.machinemuse.numina.api.capability_ports.heat;


/**
 * Same as ForgeEnergy/RF, except for heat
 */
public interface IHeatStorage {
    /**
     * Adds heat to the storage. Returns quantity of heat that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of heat to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of heat that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveHeat(int maxReceive, boolean simulate);

    /**
     * Removes heat from the storage. Returns quantity of heat that was removed.
     *
     * @param maxExtract
     *            Maximum amount of heat to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of heat that was (or would have been, if simulated) extracted from the storage.
     */
    int extractHeat(int maxExtract, boolean simulate);

    /**
     * Returns the amount of heat currently stored.
     */
    int getHeatStored();

    /**
     * Returns the maximum amount of heat that can be stored.
     */
    int getMaxHeatStored();

    /**
     * Sets the maximum amount of heat that can be stored.
     */
    void setMaxHeatStored(int maxHeat);

    /**
     * Sets the current heat value
     */
    void setCurrentHeat(int currentHeat);

    /**
     * Sets the maximum heat transfer value
     */
    void setMaxHeatTransfer(int maxHeatTransfer);

    /**
     * Returns if this storage can have heat extracted.
     * If this is false, then any calls to extractHeat will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive heat.
     * If this is false, then any calls to receiveHeat will return 0.
     */
    boolean canReceive();
}