package net.machinemuse.utils;

import net.machinemuse.api.electricity.ElectricAdapter;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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

    public static double getPlayerEnergy(EntityPlayer player) {
        double avail = 0.0;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            avail += adapter.getCurrentEnergy();
        return avail;
    }

    public static double getMaxEnergy(EntityPlayer player) {
        double avail = 0.0;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            avail += adapter.getMaxEnergy();
        return avail;
    }

    public static void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
        if (player.capabilities.isCreativeMode)
            return;
        double drainleft = drainAmount;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            if (drainleft > 0)
                drainleft = drainleft - adapter.drainEnergy(drainleft);
            else
                break;
    }

    public static void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        double joulesleft = joulesToGive;
        for (ElectricAdapter adapter: electricItemsEquipped(player))
            if (joulesleft > 0)
                joulesleft = joulesleft - adapter.giveEnergy(joulesleft);
            else
                break;
    }

    public static double jouleValueOfComponent(ItemStack stackInCost) {
        if (stackInCost.getItem() instanceof ItemComponent) {
            switch(stackInCost.getItemDamage() - ItemComponent.lvcapacitor.getItemDamage()) {
                case 0:
                    return 20000 * stackInCost.stackSize;
                case 1:
                    return 100000 * stackInCost.stackSize;
                case 2:
                    return 750000 * stackInCost.stackSize;
                default:
                    return 0;
            }
        }
        return 0;
    }
}