package net.machinemuse.powersuits.utils;

import net.machinemuse.powersuits.api.electricity.adapter.ElectricAdapter;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Ported to Java by lehjr on 10/18/16.
 */
public class ElectricItemUtils {
    public static List<ElectricAdapter> electricItemsEquipped(EntityPlayer player) {
        List<ElectricAdapter> electrics = new ArrayList<>();
        // Only check the entity equipment slots for items. This is all armor slots and both hand slots.
        for (EntityEquipmentSlot slot: EntityEquipmentSlot.values()) {
            ElectricAdapter adapter  = ElectricAdapter.wrap(player.getItemStackFromSlot(slot));
            if (adapter != null) {
                electrics.add(adapter);
            }
        }
        return electrics;
    }

    public static void chargeEmulatedToolFromPlayerEnergy(EntityPlayer player, @Nonnull ItemStack emulatedTool) {
        ElectricAdapter adapter = ElectricAdapter.wrap(emulatedTool);
        if (adapter == null)
            return;

        int maxCharge = adapter.getMaxEnergyStored();
        int charge = adapter.getEnergyStored();

        if (maxCharge == charge)
            return;

        int playerEnergy = getMaxPlayerEnergy(player);
        if (playerEnergy > (maxCharge - charge)) {
            adapter.receiveEnergy(maxCharge - charge, false);
            drainPlayerEnergy(player, maxCharge - charge);
        } else {
            adapter.receiveEnergy(playerEnergy, false);
            drainPlayerEnergy(player, playerEnergy);
        }
    }

    public static int getItemEnergy(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null)
            return adapter.getEnergyStored();
        return 0;
    }

    public static int getMaxItemEnergy(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null)
            return adapter.getEnergyStored();
        return 0;
    }

    public static int getMaxPlayerEnergy(EntityPlayer player) {
        int avail = 0;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            avail += adapter.getEnergyStored();
        return avail;
    }

    public static int getMaxEnergy(EntityPlayer player) {
        int avail = 0;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            avail += adapter.getMaxEnergyStored();
        return avail;
    }

    public static void drainPlayerEnergy(EntityPlayer player, int drainAmount) {
        int drainleft = drainAmount;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            if (drainleft > 0)
                drainleft = drainleft - adapter.extractEnergy(drainleft, false);
            else
                break;
    }

    public static void givePlayerEnergy(EntityPlayer player, int rfToGive) {
        int rfLeft = rfToGive;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            if (rfLeft > 0) {
                rfLeft = rfLeft - adapter.receiveEnergy(rfLeft, false);
            } else
                break;
    }

    public static int rfValueOfComponent(ItemStack stackInCost) {
        if (stackInCost.getItem() instanceof ItemComponent) {
            switch(stackInCost.getItemDamage() - ItemComponent.lvcapacitor.getItemDamage()) {
                case 0:
                    return 200000 * stackInCost.getCount();
                case 1:
                    return 1000000 * stackInCost.getCount();
                case 2:
                    return 7500000 * stackInCost.getCount();
                case 3:
                    return 10000000 * stackInCost.getCount();
                default:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Used for getting the tier of an ItemStack. Used for various functions
     *
     */
    public static int getTierForItem(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null) {
            int maxEnergy = adapter.getMaxEnergyStored();
            if (maxEnergy <= MPSConfig.INSTANCE.getTier1MaxRF())
                return 1;
            else if (maxEnergy <= MPSConfig.INSTANCE.getTier2MaxRF())
                return 2;
            else if (maxEnergy <= MPSConfig.INSTANCE.getTier3MaxRF())
                return 3;
            else if (maxEnergy <= MPSConfig.INSTANCE.getTier4MaxRF())
                return 4;
            return 5;
        }
        return 0;
    }
}