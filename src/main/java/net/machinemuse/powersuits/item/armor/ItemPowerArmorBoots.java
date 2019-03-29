package net.machinemuse.powersuits.item.armor;

import ic2.api.item.IMetalArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.item.IMetalArmor", modid = "IC2", striprefs = true)})
public class ItemPowerArmorBoots extends ItemPowerArmor implements IMetalArmor {
    public final EntityEquipmentSlot armorType;

    public ItemPowerArmorBoots(String regName) {
        super(regName, "powerArmorBoots",0, EntityEquipmentSlot.FEET);
        this.armorType = EntityEquipmentSlot.FEET;
    }

    public boolean isMetalArmor(final ItemStack itemStack, final EntityPlayer entityPlayer) {
        return true;
    }
}