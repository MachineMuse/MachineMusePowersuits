package net.machinemuse.powersuits.item.armor;

import forestry.api.core.IArmorNaturalist;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "forestry.api.core.IArmorNaturalist", modid = "forestry", striprefs = true),
        @Optional.Interface(iface = "thaumcraft.api.items.IGoggles", modid = "thaumcraft", striprefs = true),
        @Optional.Interface(iface = "thaumcraft.api.items.IRevealer", modid = "thaumcraft", striprefs = true),
        @Optional.Interface(iface = "thaumcraft.api.items.IVisDiscountGear", modid = "thaumcraft", striprefs = true)
})
public class ItemPowerArmorHelmet extends ItemPowerArmor implements
        IArmorNaturalist,
        IVisDiscountGear,
        IRevealer,
        IGoggles {
    public final EntityEquipmentSlot armorType;

    public ItemPowerArmorHelmet() {
        super(0, EntityEquipmentSlot.HEAD);
        this.setTranslationKey("powerArmorHelmet");
        this.armorType = EntityEquipmentSlot.HEAD;
    }

    @Optional.Method(modid = "forestry")
    public boolean canSeePollination(final EntityPlayer player, final ItemStack helm, final boolean doSee) {
        return ModuleManager.INSTANCE.itemHasActiveModule(helm, MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME);
    }

    @Optional.Method(modid = "thaumcraft")
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return ModuleManager.INSTANCE.itemHasModule(itemStack, MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME);
    }

    @Optional.Method(modid = "thaumcraft")
    public boolean showNodes(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return ModuleManager.INSTANCE.itemHasModule(itemStack, MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME);
    }

    @Optional.Method(modid = "thaumcraft")
    public int getVisDiscount(ItemStack itemStack, EntityPlayer entityPlayer) {
        return ModuleManager.INSTANCE.itemHasModule(itemStack, MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME) ? 5 : 0;
    }
}