package net.machinemuse.powersuits.item.armor;

import com.google.common.collect.Multimap;
import net.machinemuse.numina.api.item.IArmorTraits;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.capabilities.MPSCapProvider;
import net.machinemuse.powersuits.client.model.item.armor.ArmorModelInstance;
import net.machinemuse.powersuits.client.model.item.armor.IArmorModel;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

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
        super(ItemArmor.ArmorMaterial.IRON, renderIndex, entityEquipmentSlot);
        this.setMaxStackSize(1);
        this.setCreativeTab(MPSConfig.INSTANCE.getCreativeTab());
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        int priority = 0;
        // Fire damage is just heat based damage like fire or lava
        if (source.isFireDamage() && !(source.equals(MuseHeatUtils.overheatDamage))) {
            return new ISpecialArmor.ArmorProperties(priority, 0.25, (int) (25 * damage));
        }
        if (ModuleManager.INSTANCE.itemHasModule(armor, "Radiation Shielding") && (source.damageType.equals("electricity") || source.damageType.equals("radiation"))) {
            return new ISpecialArmor.ArmorProperties(priority, 0.25, (int) (25 * damage));
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
        return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
    }

    @Override
    public String getArmorTexture(ItemStack armor, Entity entity, EntityEquipmentSlot slot, String type) {
        if (type == "overlay")  // this is to allow a tint to be applied tot the armor
            return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;
        if (armor.getItem() instanceof ItemPowerArmor) {
            if ((slot == EntityEquipmentSlot.CHEST && ModuleManager.INSTANCE.itemHasActiveModule(armor, MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME)) ||
                    (ModuleManager.INSTANCE.itemHasActiveModule(armor, MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME)))
                return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;
            return MPSNBTUtils.getArmorTexture(armor, slot);
        }
        return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;
    }

//    @Override
//    public int getColor(ItemStack stack) {
//        Colour c = this.getColorFromItemStack(stack);
//        return c.getInt();
//    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack armor, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (!armor.isEmpty()) {
            // check if using 2d armor
            if (!MPSNBTUtils.hasHighPolyModel(armor, armorSlot)) {
                return _default;
            }

            ModelBiped model = ArmorModelInstance.getInstance();
            ((IArmorModel) model).setVisibleSection(armorSlot);

            if (ModuleManager.INSTANCE.itemHasActiveModule(armor, MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME) ||
                    (armorSlot == EntityEquipmentSlot.CHEST && ModuleManager.INSTANCE.itemHasActiveModule(armor, MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME))) {
                ((IArmorModel) model).setVisibleSection(null);
            }

            ((IArmorModel) model).setRenderSpec(MPSNBTUtils.getMuseRenderTag(armor, armorSlot));
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

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
//        if (MPSNBTUtils.hasHighPolyModel(stack, ((ItemPowerArmor) stack.getItem()).armorType)) {
//            return false;
//        }
        return true;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) this.getArmorDouble(player, armor);
    }

    public double getHeatResistance(EntityPlayer player, ItemStack stack) {
        return MuseHeatUtils.getMaxHeat(stack);
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        double totalArmor = 0.0;
        NBTTagCompound props = MuseNBTUtils.getMuseItemTag(stack);
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double physArmor = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_VALUE_PHYSICAL);
        double enerArmor = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_VALUE_ENERGY);
        double enerConsum = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION);
        totalArmor += physArmor;
        if (energy > enerConsum) {
            totalArmor += enerArmor;
        }
        totalArmor = Math.min(MPSConfig.INSTANCE.getMaximumArmorPerPiece(), totalArmor);
        return totalArmor;
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound itemProperties = MuseNBTUtils.getMuseItemTag(stack);
        if (entity instanceof EntityPlayer) {
            DamageSource overheatDamage = MuseHeatUtils.overheatDamage;
            if (source == null) {
                if (overheatDamage == null) {
                    return;
                }
            } else if (source.equals(overheatDamage)) {
                return;
            }

            // isFireDamage includes heat related damage sources such as lava
            if (source.isFireDamage()) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!source.equals(DamageSource.ON_FIRE) || MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getMaxHeat(player))
                    MuseHeatUtils.heatPlayer(player, damage);
            } else {
                double enerConsum = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION);
                double drain = enerConsum * damage;
                if (entity instanceof EntityPlayer)
                    ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, (int) drain);
                else {
                    final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
                    if (energyStorage != null) {
                        energyStorage.extractEnergy((int) drain, false);
                    }
                }
            }
        }
    }

    @Optional.Method(modid = "forestry")
    @Override
    public boolean protectEntity(final EntityLivingBase player, final ItemStack armor, final String cause, final boolean doProtect) {
        if (ModuleManager.INSTANCE.itemHasActiveModule(armor, MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(armor, MPSModuleConstants.APIARIST_ARMOR_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

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

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        // NBT provided here is empty or null, so it's useless for this.
        return new MPSCapProvider(stack);
    }
}