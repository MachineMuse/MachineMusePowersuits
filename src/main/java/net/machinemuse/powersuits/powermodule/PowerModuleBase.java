package net.machinemuse.powersuits.powermodule;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.machinemuse.numina.api.constants.NuminaNBTConstants.TAG_ONLINE;

public abstract class PowerModuleBase implements IPowerModule {
    EnumModuleTarget moduleTarget;
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected static Map<String, String> units = new HashMap<>();
    protected NBTTagCompound defaultTag;
    protected boolean isAllowed;

    public PowerModuleBase(String dataNameIn, EnumModuleTarget moduleTargetIn) {
        this.moduleTarget = moduleTargetIn;
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(TAG_ONLINE, true);
        this.isAllowed = MPSConfig.INSTANCE.getModuleAllowedorDefault(dataNameIn, true);
    }

    public PowerModuleBase(EnumModuleTarget moduleTargetIn) {
        this.moduleTarget = moduleTargetIn;
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(TAG_ONLINE, true);
        this.isAllowed = this.isAllowed = MPSConfig.INSTANCE.getModuleAllowedorDefault(getDataName(), true);
    }

    public static String getUnit(String propertyName) {
        String unit = units.get(propertyName);
        return unit == null ? "" : unit;
    }

    @Override
    public EnumModuleTarget getTarget() {
        return this.moduleTarget;
    }

    @Override
    public abstract TextureAtlasSprite getIcon(ItemStack item);

    @Override
    public boolean isValidForItem(@Nonnull ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IModularItem))
            return false;
        Item item = stack.getItem();
        switch (getTarget()) {
            case ALLITEMS:
                return true;
            case TOOLONLY:
                return item instanceof ItemPowerFist;
            case ARMORONLY:
                return item instanceof ItemPowerArmor;
            case HEADONLY:
                return item instanceof ItemPowerArmorHelmet;
            case TORSOONLY:
                return item instanceof ItemPowerArmorChestplate;
            case LEGSONLY:
                return item instanceof ItemPowerArmorLeggings;
            case FEETONLY:
                return item instanceof ItemPowerArmorBoots;
        }
        return false;
    }

    @Override
    public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
        return propertyModifiers;
    }

    @Override
    public double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue) {
        Iterable<IPropertyModifier> propertyModifiersIterable = propertyModifiers.get(propertyName);
        if (propertyModifiersIterable != null && itemTag.hasKey(this.getDataName())) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(this.getDataName());
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                propertyValue = modifier.applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public NBTTagCompound getNewTag() {
        return (NBTTagCompound) defaultTag.copy();
    }

    @Override
    public boolean isAllowed() {
        return this.isAllowed;
    }

    public void setIsAllowed(boolean allowed) {
        this.isAllowed = allowed;
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier) {
        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
        units.put(propertyName, unit);
        return addTradeoffProperty(tradeoffName, propertyName, multiplier);
        }

    public PowerModuleBase addPropertyModifier(String propertyName, IPropertyModifier modifier) {
        List<IPropertyModifier> modifiers = propertyModifiers.get(propertyName);
        if (modifiers == null) {
            modifiers = new LinkedList();
            propertyModifiers.put(propertyName, modifiers);
        }
        modifiers.add(modifier);
        return this;
    }

    public PowerModuleBase addSimpleTradeoff(IPowerModule module, String tradeoffName, String firstPropertyName, String firstUnits,
                                             double firstPropertyBase, double firstPropertyMultiplier, String secondPropertyName,
                                             String secondUnits, double secondPropertyBase,
                                             double secondPropertyMultiplier) {
        this.addBaseProperty(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffProperty(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBaseProperty(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffProperty(tradeoffName, secondPropertyName, secondPropertyMultiplier);
        return this;
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal) {
        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append(".base").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal, String unit) {
        units.put(propertyName, unit);
        return addBaseProperty(propertyName, baseVal);
    }

    public boolean equals(PowerModuleBase other) {
        return other != null && other.getDataName().equals(this.getDataName());
    }

    @Override
    public abstract String getUnlocalizedName();
}