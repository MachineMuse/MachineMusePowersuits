package net.machinemuse.utils;

import net.machinemuse.numina.api.energy.adapter.ElectricAdapter;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Ported to Java by lehjr on 10/18/16.
 */
public class ElectricItemUtils {
    public static final String MAXIMUM_ENERGY = "Maximum Energy";
    public static final String CURRENT_ENERGY = "Current Energy";

    public static List<ElectricAdapter> electricItemsEquipped(EntityPlayer player) {
        List<ElectricAdapter> electrics = new ArrayList<>();
        for (int i  = 0; i < player.inventory.getSizeInventory(); i++) {
            ElectricAdapter adapter  = ElectricAdapter.wrap(player.inventory.getStackInSlot(i));
            if (adapter != null) {
                electrics.add(0, adapter);
            }
        }
        return electrics;
    }

    public static int getPlayerEnergy(EntityPlayer player) {
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

    public static int drainPlayerEnergy(EntityPlayer player, int drainAmount) {
        int drainleft = drainAmount;
        for (ElectricAdapter adapter: electricItemsEquipped(player)) {
            if (drainleft > 0)
                drainleft = drainleft - adapter.extractEnergy(drainleft, false);
            else
                break;
        }
        return drainAmount - drainleft;
    }

    public static int givePlayerEnergy(EntityPlayer player, int rfToGive) {
        int rfleft = rfToGive;
        for (ElectricAdapter adapter: electricItemsEquipped(player)) {
            if (rfleft > 0) {
                rfleft = rfleft - adapter.receiveEnergy(rfleft, false);
            } else
                break;
        }
        return (rfleft - rfToGive) * -1;
    }

    public static double jouleValueOfComponent(ItemStack stackInCost) {
        if (stackInCost.getItem() instanceof ItemComponent) {
            switch(stackInCost.getItemDamage() - ItemComponent.lvcapacitor.getItemDamage()) {
                case 0:
                    return 20000 * stackInCost.getCount();
                case 1:
                    return 100000 * stackInCost.getCount();
                case 2:
                    return 750000 * stackInCost.getCount();
                case 3:
                    return 1000000 * stackInCost.getCount();
                default:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Used for getting the tier of an ItemStack. Used for various functions
     *
     * // FIXME... don't reference MPS from Numina
     */
    public static int getTierForPowerStorageItem(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null) {
            int maxEnergy = adapter.getMaxEnergyStored();
            if (maxEnergy <= NuminaConfig.getTier1MaxRF())
                return 1;
            else if (maxEnergy <= NuminaConfig.getTier2MaxRF())
                return 2;
            else if (maxEnergy <= NuminaConfig.getTier3MaxRF())
                return 3;
            else if (maxEnergy <= NuminaConfig.getTier4MaxRF())
                return 4;
            return 5;
        }
        return 0;
    }
}