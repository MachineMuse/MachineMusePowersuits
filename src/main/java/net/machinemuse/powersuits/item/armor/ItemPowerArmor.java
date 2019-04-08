package net.machinemuse.powersuits.item.armor;

import com.google.common.collect.Multimap;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.constants.MPSResourceConstants;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemPowerArmor extends ItemElectricArmor {
    public ItemPowerArmor(EntityEquipmentSlot slots) {
        super(slots, new Item.Properties().group(MPSItems.INSTANCE.creativeTab).maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public IArmorMaterial getArmorMaterial() {
        return super.getArmorMaterial();
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack armor, Entity entity, EntityEquipmentSlot slot, String type) {
        if (type == "overlay")  // this is to allow a tint to be applied tot the armor
            return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;

        return slot == EntityEquipmentSlot.LEGS ? MPSResourceConstants.SEBK_AMROR_PANTS : MPSResourceConstants.SEBK_AMROR;
    }

    @Nullable
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
//        // check if using 2d armor
//        if (!MPSNBTUtils.hasHighPolyModel(armor, armorSlot))
            return _default;
//
//        ModelBiped model = ArmorModelInstance.getInstance();
//        ((IArmorModel) model).setVisibleSection(armorSlot);
//
//        ItemStack chestPlate = armorSlot == EntityEquipmentSlot.CHEST ? armor : entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
//        if (chestPlate.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.INSTANCE.itemHasActiveModule(chestPlate, MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME) ||
//                (armorSlot == EntityEquipmentSlot.CHEST && ModuleManager.INSTANCE.itemHasActiveModule(chestPlate, MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME))) {
//            ((IArmorModel) model).setVisibleSection(null);
//        } else
//            ((IArmorModel) model).setRenderSpec(MPSNBTUtils.getMuseRenderTag(armor, armorSlot));
//        return model;
    }
}
