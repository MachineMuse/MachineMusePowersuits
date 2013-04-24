package net.machinemuse.powersuits.item;

import atomicscience.api.IAntiPoisonArmor;
import atomicscience.api.poison.Poison;
import forestry.api.apiculture.IArmorApiarist;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.misc.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.TintModule;
import net.machinemuse.powersuits.powermodule.tool.ApiaristArmorModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemElectricArmor
        implements
        ISpecialArmor,
        IAntiPoisonArmor,
        IModularItem,
        IArmorApiarist {

    /**
     * @param id
     * @param renderIndex
     * @param armorType   0 = head; 1 = torso; 2 = legs; 3 = feet
     */
    public ItemPowerArmor(int id, int renderIndex, int armorType) {
        super(id, EnumArmorMaterial.IRON, renderIndex, armorType);
        setMaxStackSize(1);
        setCreativeTab(Config.getCreativeTab());
    }

    /**
     * Inherited from ISpecialArmor, allows significant customization of damage
     * calculations.
     */
    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLiving player, ItemStack armor, DamageSource source, double damage, int slot) {
        // Order in which this armor is assessed for damage. Higher(?) priority
        // items take damage first, and if none spills over, the other items
        // take no damage.
        int priority = 1;
        if (source.isFireDamage() && !source.equals(MuseHeatUtils.overheatDamage)) {
            return new ArmorProperties(priority, 0.25, (int) (25 * damage));
        }
        double armorDouble;

        if (player instanceof EntityPlayer) {
            armorDouble = getArmorDouble((EntityPlayer) player, armor);
        } else {
            armorDouble = 2;
        }

        // How much of incoming damage is absorbed by this armor piece.
        // 1.0 = absorbs all damage
        // 0.5 = 50% damage to item, 50% damage carried over
        double absorbRatio = 0.04 * armorDouble;

        // Maximum damage absorbed by this piece. Actual damage to this item
        // will be clamped between (damage * absorbRatio) and (absorbMax). Note
        // that a player has 20 hp (1hp = 1 half-heart)
        int absorbMax = (int) armorDouble * 75; // Not sure why this is
        // necessary but oh well
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0;
        }
        return new ArmorProperties(priority, absorbRatio, absorbMax);
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }


    /**
     * Return whether the specified armor ItemStack has a color.
     */
    @Override
    public boolean hasColor(ItemStack stack) {
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        return MuseItemUtils.tagHasModule(itemTag, TintModule.RED_TINT) || MuseItemUtils.tagHasModule(itemTag, TintModule.GREEN_TINT)
                || MuseItemUtils.tagHasModule(itemTag, TintModule.BLUE_TINT);
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize the calculations for
     * how much armor will display on the player's HUD.
     */
    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) getArmorDouble(player, armor);
    }

    public double getHeatResistance(EntityPlayer player, ItemStack stack) {
        return MuseHeatUtils.getMaxHeat(stack);
    }

    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        double totalArmor = 0;
        NBTTagCompound props = MuseItemUtils.getMuseItemTag(stack);

        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double physArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL);
        double enerArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY);
        double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);

        totalArmor += physArmor;

        if (energy > enerConsum) {
            totalArmor += enerArmor;
        }
        // Make it so each armor piece can only contribute reduction up to the
        // configured amount.
        // Defaults to 6 armor points, or 24% reduction.
        totalArmor = Math.min(Config.getMaximumArmorPerPiece(), totalArmor);
        return totalArmor;
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    @Override
    public void damageArmor(EntityLiving entity, ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
        if (entity instanceof EntityPlayer) {
            if (source.equals(MuseHeatUtils.overheatDamage)) {
            } else if (source.isFireDamage()) {
                EntityPlayer player = (EntityPlayer) entity;
                MuseHeatUtils.heatPlayer(player, damage);
            } else {
                double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
                double drain = enerConsum * damage;
                if (entity instanceof EntityPlayer) {
                    ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
                } else {
                    drainEnergyFrom(stack, drain);
                }
            }
        }
    }


    // AtomicScience
    @Override
    public boolean isProtectedFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
        return MuseItemUtils.itemHasActiveModule(itemStack, HazmatModule.MODULE_HAZMAT);
    }

    // AtomicScience
    @Override
    public void onProtectFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
    }

    // Forestry
    @Override
    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
        if (MuseItemUtils.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

}
