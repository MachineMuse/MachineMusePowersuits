package net.machinemuse.numina.capabilities.heat;

import net.machinemuse.numina.api.heat.IHeatStorage;

public class HeatStorage implements IHeatStorage {
    protected double heat;
    protected double capacity;
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
        this.heat = Math.max(0, Math.min(capacity, heat));
    }

    @Override
    public double receiveHeat(double maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        double heatReceived = Math.min(capacity - heat, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            heat += heatReceived;
        return heatReceived;
    }

    @Override
    public double extractHeat(double maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        double heatExtracted = Math.min(heat, Math.min(this.maxExtract, maxExtract));
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
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }
}
