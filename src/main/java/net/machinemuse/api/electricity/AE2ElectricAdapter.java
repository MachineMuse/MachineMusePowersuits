//package net.machinemuse.api.electricity;
//
//import appeng.api.config.AccessRestriction;
//import appeng.api.implementations.items.IAEItemPowerStorage;
//import net.minecraft.item.ItemStack;
//
///**
// * Ported to Java by lehjr on 11/4/16.
// */
//public class AE2ElectricAdapter extends ElectricAdapter {
//    private final ItemStack stack;
//    private final IAEItemPowerStorage item;
//
//    public AE2ElectricAdapter(final ItemStack stack) {
//        this.stack = stack;
//        this.item = (IAEItemPowerStorage)stack.getItem();
//    }
//
//    public ItemStack stack() {
//        return this.stack;
//    }
//
//    public IAEItemPowerStorage item() {
//        return this.item;
//    }
//
//    @Override
//    public double getCurrentEnergy() {
//        return ElectricConversions.museEnergyFromAE(this.item().getAECurrentPower(this.stack()));
//    }
//
//    @Override
//    public double getMaxEnergy() {
//        return ElectricConversions.museEnergyFromAE(this.item().getAEMaxPower(this.stack()));
//    }
//
//    @Override
//    public double drainEnergy(final double requested) {
//        return ElectricConversions.museEnergyFromAE(this.item().extractAEPower(this.stack(), ElectricConversions.museEnergyToAE(requested)));
//    }
//
//    @Override
//    public double giveEnergy(final double provided) {
//        return ElectricConversions.museEnergyFromAE(this.item().injectAEPower(this.stack(), ElectricConversions.museEnergyToAE(provided)));
//    }
//
//    public AccessRestriction getPowerFlow(final ItemStack stack) {
//        return AccessRestriction.READ_WRITE;
//    }
//}