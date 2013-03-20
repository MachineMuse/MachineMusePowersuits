package net.machinemuse.api.electricity;

import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class UEElectricAdapter extends ElectricAdapter {
	protected ItemStack stack;
	protected IItemElectric item;

	public UEElectricAdapter(ItemStack stack) {
		this.stack = stack;
		this.item = (IItemElectric) stack.getItem();
	}

	@Override
	public double getCurrentEnergy() {
		double currentEnergy = museEnergyFromJoules(item.getJoules(stack));
		return currentEnergy;
	}

	@Override
	public double getMaxEnergy() {
		double maxEnergy = museEnergyFromJoules(item.getMaxJoules(stack));
		return maxEnergy;
	}

	@Override
	public double drainEnergy(double requested) {
		double voltage = item.getVoltage(stack);
		ElectricityPack requestedPack = museEnergyToElectricityPack(requested, voltage);
		ElectricityPack receivedPack = item.onProvide(requestedPack, stack);
		return museEnergyFromElectricityPack(receivedPack);
	}

	@Override
	public double giveEnergy(double provided) {
		double voltage = item.getVoltage(stack);
		ElectricityPack packToProvide = museEnergyToElectricityPack(provided, voltage);
		ElectricityPack eatenPack = item.onReceive(packToProvide, stack);
		return museEnergyFromElectricityPack(eatenPack);
	}

	public static ElectricityPack museEnergyToElectricityPack(double museEnergy, double voltage) {
		double coulombsPerTick = museEnergyToJoules(museEnergy);
		double amps = coulombsPerTick * 20.0 / voltage;
		return new ElectricityPack(amps, voltage);
	}

	public static double museEnergyFromElectricityPack(ElectricityPack pack) {
		double coulombsPerTick = pack.amperes / 20.0 * pack.voltage;
		double museEnergy = museEnergyFromJoules(coulombsPerTick);
		return museEnergy;
	}

	public static double museEnergyToJoules(double energy) {
		return energy / ModCompatability.getUERatio();
	}

	public static double museEnergyFromJoules(double energy) {
		return energy * ModCompatability.getUERatio();
	}

}
