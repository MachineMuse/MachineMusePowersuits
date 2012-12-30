/**
 * 
 */
package net.machinemuse.powersuits.energy;


/**
 * Energy Manager will provide a simplified and scalable interface for keeping
 * track of all the individual modules in an item which might store, consume, or
 * produce power.
 * 
 * @author MachineMuse
 * 
 */
public class EnergyManager {
	// public float getPlayerEnergy(EntityPlayer player) {
	// float energy = 0;
	// for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
	// ItemStack stack = player.inventory.getStackInSlot(i);
	// if (stack != null) {
	// NBTTagCompound tags = ModuleUtils.getItemModules(stack);
	// for (NBTTagCompound moduleTag : (Collection<NBTTagCompound>) tags
	// .getTags()) {
	// PowerModule module = ModuleUtils
	// .getModuleFromNBT(moduleTag);
	// if (module instanceof IPowerModuleEnergyStorage) {
	// IPowerModuleEnergyStorage psmodule = (IPowerModuleEnergyStorage) module;
	// energy += psmodule.getMaxEnergy(player, moduleTag);
	// }
	// }
	// }
	// }
	// return energy;
	// }
	//
	// public float getPlayerMaxEnergy(EntityPlayer player) {
	// float energy = 0;
	// for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
	// ItemStack stack = player.inventory.getStackInSlot(i);
	// if (stack != null) {
	// NBTTagCompound tags = ModuleUtils.getItemModules(stack);
	// // TODO: Make this scalable
	// if (tags.hasKey("Electric Battery")) {
	// NBTTagCompound batteryTag = tags
	// .getCompoundTag("Electric Battery");
	// PowerModuleBattery bat = (PowerModuleBattery) ModuleUtils
	// .getModuleFromNBT(batteryTag);
	// energy += bat.getMaxEnergy(player, batteryTag);
	// }
	// }
	// }
	// return energy;
	// }
}
