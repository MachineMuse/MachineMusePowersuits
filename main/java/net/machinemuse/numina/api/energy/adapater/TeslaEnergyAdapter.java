package net.machinemuse.numina.api.energy.adapater;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;

public class TeslaEnergyAdapter extends ElectricAdapter {
    private final ItemStack itemStack;
    private final ITeslaHolder holder;
    private final ITeslaConsumer consumer;
    private final ITeslaProducer producer;

    public TeslaEnergyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.consumer = this.itemStack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
        this.holder = itemStack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        this.producer = itemStack.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null);
    }

    @Override
    public int getCurrentMPSEnergy() {
        return holder != null ? (int) holder.getStoredPower() : 0;
    }

    @Override
    public int getMaxMPSEnergy() {
        return holder != null ? (int) holder.getCapacity() : 0;
    }

    @Override
    public int drainMPSEnergy(int requested) {
        return producer != null ? (int) producer.takePower(requested, false) : 0;
    }

    @Override
    public int giveMPSEnergy(int provided) {
        return consumer != null ? (int) consumer.givePower(provided, false) : 0;
    }
}