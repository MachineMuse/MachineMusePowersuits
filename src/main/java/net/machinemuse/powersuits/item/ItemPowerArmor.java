package net.machinemuse.powersuits.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IApiaristArmor;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.client.render.item.ArmorModelInstance;
import net.machinemuse.powersuits.client.render.item.IArmorModel;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.armor.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.InvisibilityModule;
import net.machinemuse.powersuits.powermodule.misc.TintModule;
import net.machinemuse.powersuits.powermodule.misc.TransparentArmorModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import java.util.UUID;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ItemPowerArmor extends ItemElectricArmor implements ISpecialArmor, IApiaristArmor {
    public ItemPowerArmor(int renderIndex, int armorType) {
        super(ItemArmor.ArmorMaterial.IRON, renderIndex, armorType);
        setMaxStackSize(1);
        setCreativeTab(Config.getCreativeTab());
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Config.BLANK_ARMOR_MODEL_PATH;
    }

    @Override
    public int getColor(ItemStack stack) {
        return getColorFromItemStack(stack).getInt();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ModelBiped model = ArmorModelInstance.getInstance();
        ((IArmorModel)model).setVisibleSection(armorSlot);
        if (itemStack != null) {
            if (entityLiving instanceof EntityPlayer) {
                ItemStack chest = ((EntityPlayer) entityLiving).getCurrentArmor(2);
                if (itemStack != null)
                    if (ModuleManager.itemHasActiveModule(chest, InvisibilityModule.MODULE_ACTIVE_CAMOUFLAGE))
                        ((IArmorModel) model).setVisibleSection(99);
            }
        }

        if (ModuleManager.itemHasActiveModule(itemStack, TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
            ((IArmorModel)model).setVisibleSection(99);
        }
        ((IArmorModel)model).setRenderSpec(MuseItemUtils.getMuseRenderTag(itemStack, armorSlot));
        return model;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap parent = super.getAttributeModifiers(stack);//.asInstanceOf[Multimap[String, AttributeModifier]]
        parent.put("generic.knockbackResistance", new AttributeModifier(UUID.fromString("448ef0e9-9b7c-4e56-bf3a-6b52aeabff8d"), "generic.knockbackResistance", 0.25, 0));
        return parent;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    /**
     * Return whether the specified armor has a color.
     */
    @Override
    public boolean hasColor(ItemStack stack) {
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        return ModuleManager.tagHasModule(itemTag, TintModule.RED_TINT) || ModuleManager.tagHasModule(itemTag, TintModule.GREEN_TINT) || ModuleManager.tagHasModule(itemTag, TintModule.BLUE_TINT);
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        double totalArmor = 0;
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double physArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL);
        double enerArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY);
        double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
        totalArmor += physArmor;
        if (energy > enerConsum) {
            totalArmor += enerArmor;
        }
        totalArmor = Math.min(Config.getMaximumArmorPerPiece(), totalArmor);
        return totalArmor;
    }

    /* ISpecialArmor ------------------------------------------------------------------------------*/

    /**
     * Inherited from ISpecialArmor, allows significant customization of damage
     * calculations.
     */
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        int priority = 0;

        if (source.isFireDamage() && !(source == MuseHeatUtils.overheatDamage)) {
            return new ISpecialArmor.ArmorProperties(priority, 0.25, (int)(25 * damage));
        }
        if(ModuleManager.itemHasModule(armor, HazmatModule.MODULE_HAZMAT)) {
            if(source.damageType.equals("electricity") || source.damageType.equals("radiation")) {
                return new ISpecialArmor.ArmorProperties(priority, 0.25, (int)(25 * damage));
            }
        }
        double armorDouble = (player instanceof EntityPlayer) ?  getArmorDouble((EntityPlayer) player, armor) : 2.0;
        double absorbRatio = 0.04 * armorDouble;
        int absorbMax = (int)armorDouble * 75;
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0;
        }
        return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize the calculations for
     * how much armor will display on the player's HUD.
     */
    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) getArmorDouble(player, armor);
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (entity instanceof EntityPlayer) {
            if (source == MuseHeatUtils.overheatDamage) {
            } else if (source.isFireDamage()) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!source.equals(DamageSource.onFire) || MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getMaxHeat(player)) {
                    MuseHeatUtils.heatPlayer(player, damage);
                }
            }
            else {
                double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
                double drain = enerConsum * damage;
                if (entity instanceof EntityPlayer) {
                    ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
                }
                else {
                    drainEnergyFrom(stack, drain);
                }
            }
        }
    }

    /* IApiaristArmor ----------------------------------------------------------------------------- */
    @Optional.Method(modid = "Forestry")
    @Override
    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
//        if (ModuleManager.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
//            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION));
//            return true;
//        }
        return false;
    }

    @Optional.Method(modid = "Forestry")
    @Override
    public boolean protectEntity(EntityLivingBase player, ItemStack armor, String cause, boolean doProtect) {
        if (ModuleManager.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }
}
