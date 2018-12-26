package net.machinemuse.numina.capabilities.heat;

/**
 * Based on Forge Energy and CoHF RF, but using doubles and max heat value is only a safety threashold, not a cap
 */
public class HeatStorage implements IHeatStorage {
    protected double heat;
    protected double capacity; // this is just a safety boundary, not an absolute cap
    protected double maxReceive;
    protected double maxExtract;

    public HeatStorage(double capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public HeatStorage(double capacity, double maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public HeatStorage(double capacity, double maxReceive, double maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public HeatStorage(double capacity, double maxReceive, double maxExtract, double heat) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.heat = heat;
    }

    @Override
    public double receiveHeat(double maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        double heatReceived = maxReceive;
        if (!simulate)
            heat += heatReceived;
        return heatReceived;
    }

    @Override
    public double extractHeat(double maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        double heatExtracted = Math.min(heat, maxExtract);
        if (!simulate)
            heat -= heatExtracted;
        return heatExtracted;
    }

    @Override
    public double getHeatStored() {
        return heat;
    }

    @Override
    public double getMaxHeatStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.heat > 0;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
