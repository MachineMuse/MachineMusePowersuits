package net.machinemuse.powersuits.common.items.old;

import com.google.common.collect.Multimap;
import net.machinemuse.powersuits.api.IArmorTraits;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.client.modelspec.TexturePartSpec;
import net.machinemuse.powersuits.client.renderers.item.HighPolyArmor;
import net.machinemuse.powersuits.client.renderers.item.IArmorModel;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

import static net.machinemuse.powersuits.common.MPSConstants.BLANK_ARMOR_MODEL_PATH;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 *         <p>
 *         Ported to Java by lehjr on 11/4/16.
 */
public abstract class ItemPowerArmor extends ItemElectricArmor implements ISpecialArmor, IArmorTraits {
    public static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()};

    public ItemPowerArmor(int renderIndex, EntityEquipmentSlot entityEquipmentSlot) {
        super(ArmorMaterial.IRON, renderIndex, entityEquipmentSlot);
        this.maxStackSize = 1;
        this.setCreativeTab(MPSSettings.getCreativeTab());
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        int priority = 0;
        thing:{
            if (source.isFireDamage()) {
                DamageSource overheatDamage = MuseHeatUtils.overheatDamage;
                if (source == null) {
                    if (overheatDamage == null) {
                        break thing;
                    }
                } else if (source.equals(overheatDamage)) {
                    break thing;
                }
                return new ArmorProperties(priority, 0.25, (int) (25 * damage));
            }
        }
        if (ModuleManager.itemHasModule(armor, MPSConstants.MODULE_HAZMAT) && (source.damageType.equals("electricity") || source.damageType.equals("radiation"))) {
            return new ArmorProperties(priority, 0.25, (int) (25 * damage));
        }
        double armorDouble2;
        if (player instanceof EntityPlayer) {
            armorDouble2 = this.getArmorDouble((EntityPlayer) player, armor);
        } else {
            armorDouble2 = 2.0;
        }
        double armorDouble = armorDouble2;
        double absorbRatio = 0.04 * armorDouble;
        int absorbMax = (int) armorDouble * 75;
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0.0;
        }
        return new ArmorProperties(priority, absorbRatio, absorbMax);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (type == "overlay")  // this is to allow a tint to be applied ot the armor
            return BLANK_ARMOR_MODEL_PATH;

        ItemStack armor = ((EntityLivingBase) entity).getItemStackFromSlot(slot);
        // fixme: is this check really needed?
        if (armor.getItem() instanceof ItemPowerArmor) {
            if (ModuleManager.itemHasActiveModule(armor, MPSConstants.MODULE_ACTIVE_CAMOUFLAGE))
                return BLANK_ARMOR_MODEL_PATH;

            TexturePartSpec partSpec = MuseItemUtils.getTexturePartSpec(stack, slot);
            if (partSpec != null)
                return partSpec.getTextureLocation();
        }
        return BLANK_ARMOR_MODEL_PATH;
    }

    @Override
    public int getColor(ItemStack stack) {
        return MuseItemUtils.getStackSingleColour(stack).getColour().getInt();
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return hasColor(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStackArmor, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (!itemStackArmor.isEmpty()) {
            if (!(ModuleManager.itemHasActiveModule(itemStackArmor, MPSConstants.HIGH_POLY_ARMOR)) ||
                    MPSSettings.allowHighPollyArmorModels() && MPSSettings.allowCustomHighPollyArmor()) {
                return _default;
            }

            // TODO: find way to mix vanilla model the custom model
            ModelBiped model = HighPolyArmor.getInstance();
            if (ModuleManager.itemHasActiveModule(itemStackArmor, MPSConstants.MODULE_ACTIVE_CAMOUFLAGE))
                ((IArmorModel) model).setVisibleSection(null);
            else
                ((IArmorModel) model).setVisibleSection(armorSlot);

            ((IArmorModel) model).setRenderSpec(MuseItemUtils.getMuseRenderTag(itemStackArmor, armorSlot));
            return model;
        }
        return _default;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), 0.25, 0));
        }

        return multimap;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean hasColor(ItemStack stack) {
        if (!(ModuleManager.itemHasActiveModule(stack, MPSConstants.HIGH_POLY_ARMOR)) ||
                MPSSettings.allowHighPollyArmorModels() && MPSSettings.allowCustomHighPollyArmor()) {
            return true;
        }
        return false;
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) this.getArmorDouble(player, armor);
    }

    public double getHeatResistance(EntityPlayer player, ItemStack stack) {
        return MuseHeatUtils.getMaxHeat(stack);
    }

    @Override
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

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
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
                    this.drainMPSEnergyFrom(stack, drain);
                }
            }
        }
    }
// TODO: capabilities
//    @Optional.Method(modid = "forestry")
//    public boolean protectEntity(EntityLivingBase player, ItemStack armor, String cause, boolean doProtect) {
//        return ApiaristArmor.getInstance().protectEntity(player, armor, cause, doProtect);
//    }
}