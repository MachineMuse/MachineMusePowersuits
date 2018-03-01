package net.machinemuse.numina.api.capability_ports.heat;

/**
 * Reference implementation of {@link IHeatStorage}. Use/extend this or implement your own.
 * <p>
 * Derived from Forge Energy which is Derived from the Redstone Flux power system designed by King Lemming and originally utilized in Thermal Expansion and related mods.
 * Created with consent and permission of King Lemming and Team CoFH. Released with permission under LGPL 2.1 when bundled with Forge.
 */
public class HeatStorage implements IHeatStorage {
    protected int heat;
    protected int capacity;
    protected int maxTransfer;

    public HeatStorage(int capacity) {
        this(capacity, capacity,  0);
    }

    public HeatStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, 0);
    }

    public HeatStorage(int capacity, int maxTransfer, int heat) {
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.heat = Math.max(0, Math.min(capacity, heat));
    }

    @Override
    public int receiveHeat(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int heatReceived = Math.min(capacity - heat, Math.min(this.maxTransfer, maxReceive));
        if (!simulate)
            heat += heatReceived;
        return heatReceived;
    }

    @Override
    public int extractHeat(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int heatExtracted = Math.min(heat, Math.min(this.maxTransfer, maxExtract));
        if (!simulate)
            heat -= heatExtracted;
        return heatExtracted;
    }

    @Override
    public int getHeatStored() {
        return heat;
    }

    @Override
    public int getMaxHeatStored() {
        return capacity;
    }

    @Override
    public void setMaxHeatStored(int maxHeat) {
        this.capacity = maxHeat;
    }

    @Override
    public void setCurrentHeat(int currentHeat) {
        this.heat = currentHeat;
    }

    @Override
    public void setMaxHeatTransfer(int maxHeatTransfer) {
        this.maxTransfer = maxHeatTransfer;
    }

    @Override
    public boolean canExtract() {
        return this.maxTransfer > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxTransfer > 0;
    }
}