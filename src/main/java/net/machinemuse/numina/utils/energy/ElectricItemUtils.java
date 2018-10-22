package net.machinemuse.numina.utils.energy;

import net.machinemuse.numina.api.energy.adapter.ElectricAdapter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ElectricItemUtils {
    /**
     * Returns a list of EQUIPPED items that can share power
     */
    public static List<ElectricAdapter> electricItemsEquipped(EntityPlayer player) {
        List<ElectricAdapter> electrics = new ArrayList<>();
        // Only check the entity equipment slots for items. This is all armor slots and both hand slots.
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ElectricAdapter adapter = ElectricAdapter.wrap(player.getItemStackFromSlot(slot));
            if (adapter != null) {
                electrics.add(adapter);
            }
        }
        return electrics;
    }

    /**
     * applies a given charge to the emulated tool. Used for simulating a used charge from the tool as it would normally be used
     */
    public static int chargeEmulatedToolFromPlayerEnergy(EntityPlayer player, @Nonnull ItemStack emulatedTool) {
        if (player.world.isRemote)
            return 0;

        ElectricAdapter adapter = ElectricAdapter.wrap(emulatedTool);
        if (adapter == null)
            return 0;

        int maxCharge = adapter.getMaxEnergyStored();
        int charge = adapter.getEnergyStored();

        if (maxCharge == charge)
            return 0;

        int chargeAmount;
        int playerEnergy = getPlayerEnergy(player);
        if (playerEnergy > (maxCharge - charge)) {
            adapter.receiveEnergy(maxCharge - charge, false);
            chargeAmount = drainPlayerEnergy(player, maxCharge - charge);
        } else {
            adapter.receiveEnergy(playerEnergy, false);
            chargeAmount = drainPlayerEnergy(player, playerEnergy);
        }
        return chargeAmount;
    }

    /**
     * returns the sum of the energy of the player's equipped items
     */
    public static int getPlayerEnergy(EntityPlayer player) {
        int avail = 0;
        for (ElectricAdapter adapter : electricItemsEquipped(player))
            avail += adapter.getEnergyStored();
        return avail;
    }

    /**
     * returns the total possible amount of energy the player's equipped items can hold
     */
    public static int getMaxPlayerEnergy(EntityPlayer player) {
        int avail = 0;
        for (ElectricAdapter adapter : electricItemsEquipped(player))
            avail += adapter.getMaxEnergyStored();
        return avail;
    }

    /**
     * returns the total amount of energy drained from the player's equipped items
     */
    public static int drainPlayerEnergy(EntityPlayer player, int drainAmount) {
        if (player.world.isRemote)
            return 0;

        int drainleft = drainAmount;
        for (ElectricAdapter adapter : electricItemsEquipped(player)) {
            if (drainleft > 0)
                drainleft = drainleft - adapter.extractEnergy(drainleft, false);
            else
                break;
        }
        return drainAmount - drainleft;
    }

    /**
     * returns the total amount of energy given to a player's equipped items
     */
    public static int givePlayerEnergy(EntityPlayer player, int rfToGive) {
        int rfLeft = rfToGive;
        for (ElectricAdapter adapter : electricItemsEquipped(player)) {
            if (rfLeft > 0) {
                rfLeft = rfLeft - adapter.receiveEnergy(rfLeft, false);
            } else
                break;
        }
        return rfToGive - rfLeft;
    }

    /**
     * returns the energy an item has
     */
    public static int getItemEnergy(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null)
            return adapter.getEnergyStored();
        return 0;
    }

    /**
     * returns total possible energy an item can hold
     */
    public static int getMaxItemEnergy(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null)
            return adapter.getEnergyStored();
        return 0;
    }


}
