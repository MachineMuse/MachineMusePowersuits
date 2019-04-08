package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Power handling base class for armor
 */
public class ItemElectricArmor extends ItemArmor {
    public ItemElectricArmor(EntityEquipmentSlot slots, Properties builder) {
        super(MPSArmorMaterial.EMPTY_ARMOR, slots, builder);
    }

//    @Nullable
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
//        return new MPSCapProvider(stack);
//    }
}

