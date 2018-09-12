package net.machinemuse.numina.utils.heat;

import net.machinemuse.numina.api.constants.NuminaConstants;
import net.machinemuse.numina.api.heat.IHeatStorage;
import net.machinemuse.numina.capabilities.heat.CapabilityHeat;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;
import java.util.List;

public class MuseHeatUtils {
    public static double getPlayerHeat(EntityPlayer player) {
        double heat = 0;

        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            heat += getItemHeat(stack);



        }
        return heat;
    }

    public static double getPlayerMaxHeat(EntityPlayer player) {
        double maxHeat = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            maxHeat += getItemMaxHeat(stack);
        }
        return maxHeat;
    }

    public static double coolPlayer(EntityPlayer player, double coolJoules) {
        List<ItemStack> items = MuseItemUtils.getModularItemsEquipped(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }
        double heatLeft = coolJoules;

        for (ItemStack stack : items) {
            if (heatLeft > 0)
                heatLeft -= coolItem(stack, heatLeft);
        }
        player.extinguish();
        return coolJoules - heatLeft;
    }

    public static double heatPlayer(EntityPlayer player, double heatJoules) {
        System.out.println("heating player " + heatJoules);

        List<ItemStack> items = MuseItemUtils.getModularItemsEquipped(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }
        double heatLeft = heatJoules;

        for (ItemStack stack : items) {
            if (heatLeft > 0)
                heatLeft -= heatItem(stack, heatLeft);
        }
        if (heatLeft > 0) {
            System.out.println("applying overheat damage: " + (float) (Math.sqrt(heatLeft)/ 4));

            player.attackEntityFrom(MuseHeatUtils.overheatDamage, (float) (Math.sqrt(heatLeft)/ 4));
            player.setFire(1);
        } else
            player.extinguish();

        System.out.println("actual heat applied " + (heatJoules - heatLeft));

        return heatJoules - heatLeft;
    }

    public static double getItemMaxHeat(@Nonnull ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.getMaxHeatStored();
        return 0;
    }

    public static double getItemHeat(@Nonnull ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.getHeatStored();
        return 0;
    }

    public static double heatItem(@Nonnull ItemStack stack, double value) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.receiveHeat(value, false);
        return 0;
    }

    public static double coolItem(@Nonnull ItemStack stack, double value) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.extractHeat(value, false);
        return 0;
    }

    public static final DamageSource overheatDamage = new OverheatDamage();
    protected static final class OverheatDamage extends DamageSource {
        public OverheatDamage() {
            super(NuminaConstants.OVERHEAT_DAMAGE);
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }

        public boolean equals(DamageSource other) {
            return other.damageType.equals(this.damageType);
        }
    }
}
