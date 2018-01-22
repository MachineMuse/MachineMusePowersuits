package net.machinemuse.powersuits.common.item.armor;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;

@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux")
public class ItemPowerArmorNew extends ItemArmor implements ISpecialArmor, IEnergyContainerItem {
    public ItemPowerArmorNew(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(ArmorMaterial.IRON, renderIndexIn, equipmentSlotIn);
    }

    // FIXME: move this to capabilities

//    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        double totalArmor = 0.0;
        NBTTagCompound props = MuseItemUtils.getMuseItemTag(stack);
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double physArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL);
        double enerArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY);
        double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
        totalArmor += physArmor;
        if (energy > enerConsum) {
            totalArmor += enerArmor;
        }
        totalArmor = Math.min(MPSSettings.general.getMaximumArmorPerPiece, totalArmor);
        return totalArmor;
    }

    /** ISpecialArmor ----------------------------------------------------------------------------- */
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        int priority = 0;

        if (source.isFireDamage() && !(source == MuseHeatUtils.overheatDamage)) {
            return new ArmorProperties(priority, 0.25, (int) (25 * damage));
        }

        if(ModuleManager.itemHasModule(armor, MPSConstants.MODULE_HAZMAT) &&
                source.damageType.equals("electricity") || source.damageType.equals("radiation")) {
            return new ArmorProperties(priority, 0.25, (int) (25 * damage));
        }

        double armorDouble = (player instanceof EntityPlayer) ? this.getArmorDouble((EntityPlayer) player, armor) : 2.0;

        double absorbRatio = 0.04 * armorDouble;
        int absorbMax = (int) armorDouble * 75;
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0.0;
        }
        return new ArmorProperties(priority, absorbRatio, absorbMax);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return (int) this.getArmorDouble(player, armor);
    }

    /**
     * Allows us to customize how the armor handles being damaged.
     */
    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);

        // TODO: shouldn't this work on all entities that can wear the armor?
        if (entity instanceof EntityPlayer) {
            DamageSource overheatDamage = MuseHeatUtils.overheatDamage;
            if (source == null) {
                if (overheatDamage == null) {
                    return;
                }
            } else if (source.equals(overheatDamage)) {
                return;
            }
            if (source.isFireDamage()) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!source.equals(DamageSource.ON_FIRE) || MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getMaxHeat(player)) {
                    MuseHeatUtils.heatPlayer(player, damage);
                }
            } else {
                double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
                double drain = enerConsum * damage;
                if (entity instanceof EntityPlayer) {
                    ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
                } else {
//                    this.extractEnergy(stack, drain); // FIXME to use capabilities reference
                }
            }
        }
    }

    /** Item -------------------------------------------------------------------------------------- */
    @Override
    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null) {
            return 1;
        }

        return 1 - energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored();
    }

    /** IEnergyContainerItem ---------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.getMaxEnergyStored() : 0;
    }
}