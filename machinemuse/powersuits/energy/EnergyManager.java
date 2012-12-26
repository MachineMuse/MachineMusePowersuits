/**
 * 
 */
package machinemuse.powersuits.energy;

import machinemuse.powersuits.powermodule.PowerModule;
import machinemuse.powersuits.powermodule.PowerModuleBattery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Energy Manager will provide a simplified and scalable interface for keeping
 * track of all the individual modules in an item which might store, consume, or
 * produce power.
 * 
 * @author MachineMuse
 * 
 */
public class EnergyManager {
	public float getStoredEnergy(EntityPlayer player) {
		float energy = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tags = PowerModule.getItemModules(stack);
				// TODO: Make this scalable
				if (tags.hasKey("Electric Battery")) {
					NBTTagCompound batteryTag = tags
							.getCompoundTag("Electric Battery");
					PowerModuleBattery bat = (PowerModuleBattery) PowerModule
							.getModuleFromNBT(batteryTag);
					energy += bat.getStoredEnergy(player, batteryTag);
				}
			}
		}
		return energy;
	}

	public float getMaxEnergy(EntityPlayer player) {
		float energy = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tags = PowerModule.getItemModules(stack);
				// TODO: Make this scalable
				if (tags.hasKey("Electric Battery")) {
					NBTTagCompound batteryTag = tags
							.getCompoundTag("Electric Battery");
					PowerModuleBattery bat = (PowerModuleBattery) PowerModule
							.getModuleFromNBT(batteryTag);
					energy += bat.getMaxEnergy(player, batteryTag);
				}
			}
		}
		return energy;
	}
}
