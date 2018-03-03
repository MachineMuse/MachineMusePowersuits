package net.machinemuse.utils;

import net.machinemuse.numina.api.energy.adapater.ElectricAdapter;
import net.machinemuse.numina.common.config.NuminaConfig;
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

    /**
     * Equipped ItemStacks with the Energy capability
     */
    public static List<ElectricAdapter> getElectricItemsEquipped(EntityPlayer player) {
        List<ElectricAdapter> electrics = new ArrayList<>();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ElectricAdapter adapter  = ElectricAdapter.wrap(player.getItemStackFromSlot(slot));
            if (adapter != null) {
                electrics.add(0, adapter);
            }
        }
        return electrics;
    }

    /**
     * Sum of all the energy of the equipped items
     */
    public static int getPlayerEnergy(EntityPlayer player) {
        int avail = 0;
        for (ElectricAdapter adapter : getElectricItemsEquipped(player))
            avail += adapter.getEnergyStored();
        return avail;
    }

    /**
     * @param player
     *      Player holding the ItemStacks to be queried
     * @return Total amount of energy of items equipped.
     */
    public static int getMaxPlayerEnergy(EntityPlayer player) {
        int max = 0;
        for (ElectricAdapter adapter : getElectricItemsEquipped(player))
            max += adapter.getMaxEnergyStored();
        return max;
    }

    /**
     * Removes energy from the ItemStack. Returns quantity of energy that was removed.
     *
     * @param player
     *          Player holding the ItemStacks to be drained
     * @param drainAmount
     *          Maximum amount of energy to be extracted.
     * @return Amount of energy that was extracted from all of the ItemStacks.
     */
    public static int drainPlayerEnergy(EntityPlayer player, int drainAmount) {
        int drainLeft = drainAmount;
        for (ElectricAdapter adapter : getElectricItemsEquipped(player))
            if (drainLeft > 0)
                drainLeft = drainLeft - adapter.extractEnergy(drainLeft, false);
            else
                break;
        return drainAmount - drainLeft;
    }

    /**
     * Gives energy to the equipped compatible items. Returns quantity of energy that was added.
     *
     * @param player
     *          Player holding the ItemStacks to be drained
     * @param rfToGive
     *          Maximum amount of energy to be added.
     * @return Amount of energy that was added to all of the ItemStacks.
     */
    public static int givePlayerEnergy(EntityPlayer player, int rfToGive) {
        int rfleft = rfToGive;
        for (ElectricAdapter adapter: getElectricItemsEquipped(player))
            if (rfleft > 0) {
                rfleft = rfleft - adapter.receiveEnergy(rfleft, false);
            } else
                break;
        return rfToGive - rfleft;
    }

    /**
     * Used for getting the tier of an ItemStack. Used for various functions
     *
     * // FIXME... don't reference MPS from Numina
     */
    public static int getTierForItem(@Nonnull ItemStack itemStack) {
        int maxEnergy = ElectricAdapter.wrap(itemStack).getMaxEnergyStored();
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

    @Deprecated
    public static int jouleValueOfComponent(ItemStack stackInCost) {
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
}