package net.machinemuse.powersuits.item.armor;

import com.google.common.collect.Multimap;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.IArmorTraits;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.capabilities.MPSCapProvider;
import net.machinemuse.powersuits.client.model.item.armor.ArmorModelInstance;
import net.machinemuse.powersuits.client.model.item.armor.IArmorModel;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
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

import javax.annotation.Nonnull;
import java.util.UUID;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ItemPowerArmor extends ItemElectricArmor implements ISpecialArmor, IArmorTraits {
    public static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()};

    public ItemPowerArmor(String regName, String unlocalizedName, int renderIndex, EntityEquipmentSlot entityEquipmentSlot) {
        super(ItemArmor.ArmorMaterial.IRON, renderIndex, entityEquipmentSlot);
        this.setRegistryName(regName);
        this.setTranslationKey(new StringBuilder(MODID).append(".").append(unlocalizedName).toString());
        this.setMaxStackSize(1);
        this.setCreativeTab(MPSConfig.INSTANCE.mpsCreativeTab);
        this.setMaxDamage(0);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    /**
     * This just a method that determines whether or not otherwise unhandled damage sources are handled by the armor
     * <p>
     * Note, slot is equivilant to the EntityEquipmentSlot index (not slotIndex)
     */
    @Override
    public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
//            System.out.println("damage source: " + source.damageType);
//            System.out.println("slot: " + slot);

        if (source == null || source == MuseHeatUtils.overheatDamage)
            return false;

        if (source.damageType.equals("electricity") || source.damageType.equals("radiation") || source.damageType.equals("sulphuric_acid"))
            return ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_HAZMAT__DATANAME);

        // Fixme: needs to check for Oxygen... needs to check for

//        // Galacticraft
//        if (slot == 3 && source.getDamageType().equals("oxygen_suffocation"))
//            return ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_AIRTIGHT_SEAL__DATANAME);









        // this still needs tweaking (extra planets)
        if (source.getDamageType().equals("pressure")) {
            if (slot == 3)
                return ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_AIRTIGHT_SEAL__DATANAME) && ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_HAZMAT__DATANAME);
            else
                return ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_HAZMAT__DATANAME);

//            for (ItemStack armorStack : entity.getArmorInventoryList()) {
//                if (armorStack.getItem() instanceof this)
//                    return false;
//
//
//
//
////                if (ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_AIRTIGHT_SEAL__DATANAME))
//
//
//
//
//            }
        }

        if (source.getDamageType().equals("cryotheum"))
            return MuseHeatUtils.getPlayerHeat((EntityPlayer) entity) > 0;


        // TODO: Galacticraft "thermal", "sulphuric_acid", "pressure"
        // TODO: Advanced Rocketry: CapabilitySpaceArmor.PROTECTIVEARMOR;


//        System.out.println("damage source: " + source.getDamageType() + " not protected. Damage ammount:" + damage );
        return false;
    }

    /**
     * Inherited from ISpecialArmor, allows us to customize how the armor
     * handles being damaged.
     */
    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            DamageSource overheatDamage = MuseHeatUtils.overheatDamage;

//            if (source == null || source.equals(overheatDamage)) {
//                if (source != null)
//                    System.out.println("overheat damage: " + damage);
//
//
//                return;
//            }

            // cool the player instead of applying damage. Only fires if player has heat
            if (source.getDamageType().equals("cryotheum") && entity.world.isRemote)
                MuseHeatUtils.coolPlayer(player, damage * 10);


            // FIXME: heat needs to either be applied here or in the player uodate handler through environment
            // isFireDamage includes heat related damage sources such as lava
            if (source.isFireDamage()) {

//                System.out.println("heat damage: " + damage);

//                if (!source.equals(DamageSource.ON_FIRE) ||
//                        MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getPlayerMaxHeat(player))
//                MuseHeatUtils.heatPlayer(player, damage);

                MuseHeatUtils.heatPlayer(player, damage * 5); // FIXME: this value needs tweaking. 10 too high, 1 too low
            } else {
                double enerConsum = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION);
                double drain = enerConsum * damage;
                ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, (int) drain);
            }
        }
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        double absorbRatio = 0.25; // 25% for each armor piece;
        int absorbMax = (int) (25 * damage);
        int priority = 0;

        // Fire damage is just heat based damage like fire or lava
        if (source.isFireDamage() && !(source.equals(MuseHeatUtils.overheatDamage))) { //  heat damage is only 1 point ?
//            System.out.println("heat damage: " + damage);

            return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
        }

        // hazmat handled hazards
        if (ModuleManager.INSTANCE.itemHasModule(armor, MPSModuleConstants.MODULE_HAZMAT__DATANAME) &&
                (source.damageType.equals("electricity") ||
                        source.damageType.equals("radiation") ||
                        source.damageType.equals("sulphuric_acid"))) {
            return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
        }

        double armorDouble;
        if (player instanceof EntityPlayer) {
            armorDouble = this.getArmorDouble((EntityPlayer) player, armor);
        } else {
            armorDouble = 2.0;
        }

        absorbMax = (int) armorDouble * 75;
        if (source.isUnblockable()) {
            absorbMax = 0;
            absorbRatio = 0.0;
        } else {
            absorbRatio = 0.04 * armorDouble;
        }

        return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == this.armorType) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), 0.25, 0));
            if (ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_DIAMOND_PLATING__DATANAME) || ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_ENERGY_SHIELD__DATANAME)) {
                multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", 2.5, 0));
            }
        }
        return multimap;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) this.getArmorDouble(player, armor);
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        double totalArmor = 0.0;
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
        int capacity = 0;
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage != null)
            capacity = energyStorage.getMaxEnergyStored();

        if (capacity > 0)
            return true;
        return false;
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        return 1 - (energyStorage != null ? energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored() : 0);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        // NBT provided here is empty or null, so it's useless for this.
        return new MPSCapProvider(stack);
    }

    // Cosmetics ----------------------------------------------------------------------------------

    @Override
    public boolean hasColor(ItemStack stack) {
        return true;
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

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack armor, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        // check if using 2d armor
        if (!MPSNBTUtils.hasHighPolyModel(armor, armorSlot))
            return _default;

        ModelBiped model = ArmorModelInstance.getInstance();
        ((IArmorModel) model).setVisibleSection(armorSlot);

        ItemStack chestPlate = armorSlot == EntityEquipmentSlot.CHEST ? armor : entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chestPlate.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.INSTANCE.itemHasActiveModule(chestPlate, MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME) ||
                (armorSlot == EntityEquipmentSlot.CHEST && ModuleManager.INSTANCE.itemHasActiveModule(chestPlate, MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME))) {
            ((IArmorModel) model).setVisibleSection(null);
        } else
            ((IArmorModel) model).setRenderSpec(MPSNBTUtils.getMuseRenderTag(armor, armorSlot));
        return model;
    }
}