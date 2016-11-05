package net.machinemuse.powersuits.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.ApiaristArmor;
import net.machinemuse.api.IArmorTraits;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.item.ArmorModel;
import net.machinemuse.powersuits.client.render.item.ArmorModel$;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.misc.InvisibilityModule;
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
public abstract class ItemPowerArmor extends ItemElectricArmor implements ISpecialArmor, IArmorTraits {
    public ItemPowerArmor(final int renderIndex, final int armorType) {
        super(ItemArmor.ArmorMaterial.IRON, renderIndex, armorType);
        this.setMaxStackSize(1);
        this.setCreativeTab(Config.getCreativeTab());
    }

    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor, final DamageSource source, final double damage, final int slot) {
        final int priority = 0;
        Label_0057: {
            if (source.isFireDamage()) {
                final DamageSource overheatDamage = MuseHeatUtils.overheatDamage;
                if (source == null) {
                    if (overheatDamage == null) {
                        break Label_0057;
                    }
                }
                else if (source.equals(overheatDamage)) {
                    break Label_0057;
                }
                return new ISpecialArmor.ArmorProperties(priority, 0.25, (int)(25 * damage));
            }
        }
        if (ModuleManager.itemHasModule(armor, "Radiation Shielding") && (source.damageType.equals("electricity") || source.damageType.equals("radiation"))) {
            return new ISpecialArmor.ArmorProperties(priority, 0.25, (int)(25 * damage));
        }
        double armorDouble2;
        if (player instanceof EntityPlayer) {
            armorDouble2 = this.getArmorDouble((EntityPlayer)player, armor);
        }
        else {
            armorDouble2 = 2.0;
        }
        final double armorDouble = armorDouble2;
        double absorbRatio = 0.04 * armorDouble;
        int absorbMax = (int)armorDouble * 75;
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0.0;
        }
        return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
    }

    public String getArmorTexture(final ItemStack itemstack, final Entity entity, final int slot, final int layer) {
        return Config.BLANK_ARMOR_MODEL_PATH();
    }

    public int getColor(final ItemStack stack) {
        final Colour c = this.getColorFromItemStack(stack);
        return c.getInt();
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(final EntityLivingBase entity, final ItemStack itemstack, final int armorSlot) {
        final ArmorModel model = ArmorModel$.MODULE$.instance();
        model.visibleSection_$eq(armorSlot);
        if (itemstack != null) {
            if (entity instanceof EntityPlayer) {
                ItemStack armorChest = ((EntityPlayer)entity).getCurrentArmor(2);

                if (!armorChest.equals(null) && armorChest.getItem() instanceof ItemPowerArmor)
                    if (ModuleManager.itemHasActiveModule(armorChest, InvisibilityModule.MODULE_ACTIVE_CAMOUFLAGE)) model.visibleSection_$eq(99);

            }

            if (ModuleManager.itemHasActiveModule(itemstack, "Transparent Armor")) {
                model.visibleSection_$eq(99);
            }
            model.renderSpec_$eq(MuseItemUtils.getMuseRenderTag(itemstack, armorSlot));
        }
        return (ModelBiped)model;
    }

    public Multimap<?, ?> getAttributeModifiers(final ItemStack stack) {
        final Multimap parent = super.getAttributeModifiers(stack);
        parent.put((Object)"generic.knockbackResistance", (Object)new AttributeModifier(UUID.fromString("448ef0e9-9b7c-4e56-bf3a-6b52aeabff8d"), "generic.knockbackResistance", 0.25, 0));
        return (Multimap<?, ?>)parent;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean hasColor(final ItemStack stack) {
        final NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        return ModuleManager.tagHasModule(itemTag, "Red Tint") || ModuleManager.tagHasModule(itemTag, "Green Tint") || ModuleManager.tagHasModule(itemTag, "Blue Tint");
    }

    public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot) {
        return (int)this.getArmorDouble(player, armor);
    }

    public double getHeatResistance(final EntityPlayer player, final ItemStack stack) {
        return MuseHeatUtils.getMaxHeat(stack);
    }

    @Override
    public double getArmorDouble(final EntityPlayer player, final ItemStack stack) {
        double totalArmor = 0.0;
        final NBTTagCompound props = MuseItemUtils.getMuseItemTag(stack);
        final double energy = ElectricItemUtils.getPlayerEnergy(player);
        final double physArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL);
        final double enerArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY);
        final double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
        totalArmor += physArmor;
        if (energy > enerConsum) {
            totalArmor += enerArmor;
        }
        totalArmor = Math.min(Config.getMaximumArmorPerPiece(), totalArmor);
        return totalArmor;
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int damage, final int slot) {
        final NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
        if (entity instanceof EntityPlayer) {
            final DamageSource overheatDamage = MuseHeatUtils.overheatDamage;
            if (source == null) {
                if (overheatDamage == null) {
                    return;
                }
            }
            else if (source.equals(overheatDamage)) {
                return;
            }
            if (source.isFireDamage()) {
                final EntityPlayer player = (EntityPlayer)entity;
                if (!source.equals(DamageSource.onFire) || MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getMaxHeat(player)) {
                    MuseHeatUtils.heatPlayer(player, damage);
                }
            }
            else {
                final double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
                final double drain = enerConsum * damage;
                if (entity instanceof EntityPlayer) {
                    ElectricItemUtils.drainPlayerEnergy((EntityPlayer)entity, drain);
                }
                else {
                    this.drainEnergyFrom(stack, drain);
                }
            }
        }
    }

    @Optional.Method(modid = "Forestry")
    public boolean protectPlayer(final EntityPlayer player, final ItemStack armor, final String cause, final boolean doProtect) {
        return ApiaristArmor.getInstance().protectPlayer(player, armor, cause, doProtect);
    }

    @Optional.Method(modid = "Forestry")
    public boolean protectEntity(final EntityLivingBase player, final ItemStack armor, final String cause, final boolean doProtect) {
        return ApiaristArmor.getInstance().protectEntity(player, armor, cause, doProtect);
    }
}