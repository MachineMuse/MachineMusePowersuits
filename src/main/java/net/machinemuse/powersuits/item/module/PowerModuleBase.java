package net.machinemuse.powersuits.item.module;

import net.machinemuse.numina.api.constants.NuminaModuleConstants;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.nbt.*;
import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class PowerModuleBase extends Item implements IModule {
    protected NonNullList<ItemStack> defaultInstallCost;
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected static Map<String, String> units = new HashMap<>();
    protected NBTTagCompound defaultTag;
    protected boolean isAllowed;
    protected EnumModuleTarget moduleTarget;

    public PowerModuleBase(EnumModuleTarget moduleTarget, String resourceDommain, String unlocalizedName) {
        this.moduleTarget = moduleTarget;
        this.setRegistryName(MPSModConstants.MODID, resourceDommain);
        this.setUnlocalizedName(unlocalizedName);
        this.defaultInstallCost = NonNullList.create();
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(NuminaModuleConstants.ONLINE, true);
        this.isAllowed = MPSConfig.getInstance().getModuleAllowed(getUnlocalizedName());
    }

    @Override
    public EnumModuleTarget getTarget() {
        return null;
    }

    public static String getUnit(String propertyName) {
        String unit = units.get(propertyName);
        return unit == null ? "" : unit;
    }

    @Override
    public NonNullList<ItemStack> getInstallCost() {
        if(ModuleManager.getInstance().hasCustomInstallCost(this.getUnlocalizedName())) {
            return ModuleManager.getInstance().getCustomInstallCost(this.getUnlocalizedName());
        } else {
            return defaultInstallCost;
        }
    }

    public PowerModuleBase addInstallCost(ItemStack stack) {
        this.defaultInstallCost.add(stack);
        return this;
    }

    @Deprecated // move to config
    @Override
    public boolean isValidForItem(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof IMuseItem))
            return false;
        Item item = stack.getItem();
        switch (moduleTarget) {
            case ALLITEMS:
                return true;
            case TOOLONLY:
                return item instanceof ItemPowerFist;
            case ARMORONLY:
                return item instanceof ItemPowerArmor;
            case HEADONLY:
                return item instanceof ItemPowerArmorHelmet;
            case TORSOONLY:
                return item instanceof ItemPowerArmor;
            case LEGSONLY:
                return item instanceof ItemPowerArmor;
            case FEETONLY:
                return item instanceof ItemPowerArmorBoots;
        }
        return false;
    }

    @Override
    public NBTTagCompound getNewTag() {
        return defaultTag.copy();
    }

    @Override
    public boolean isAllowed() {
        return this.isAllowed;
    }

    public void setIsAllowed(boolean allowed) {
        this.isAllowed = allowed;
    }

    @Override
    public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
        return propertyModifiers;
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

    /** Integer ----------------------------------------------------------------------------------- */
    @Nullable
    @Override // FIXME: all this stuff needs to go away. Set it once and be done with it, not calculate a value over and over again..
    public int applyPropertyModifiersInt(NBTTagCompound moduleTag, String propertyName, int propertyValue) {
        Iterable<IPropertyModifier> propertyModifiersIterable = propertyModifiers.get(propertyName);
        if (propertyModifiersIterable != null) {
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                if (modifier instanceof IPropertyModifierInteger)
                    propertyValue = ((IPropertyModifierInteger) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    String getMultiplierString(String propertyName, String tradeoffName) {
        return new StringBuilder(getUnlocalizedName()).append(".").append(propertyName).append(".").append(tradeoffName).append(".multiplier").toString();
    }

    String getBaseString(String propertyName) {
        return new StringBuilder(getUnlocalizedName()).append(".").append(propertyName).append(".base").toString();
    }



    public PowerModuleBase  addTradeoffPropertyInt(String tradeoffName, String propertyName, int multiplier) {
        int propFromConfig = MPSConfig.getInstance().getPropertyIntOrDefault(getMultiplierString(propertyName, tradeoffName), multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveInteger(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffPropertyInt(String tradeoffName, String propertyName, int multiplier, String unit) {
        int propFromConfig = MPSConfig.getInstance().getPropertyIntOrDefault(getMultiplierString(propertyName, tradeoffName), multiplier);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveInteger(tradeoffName, propFromConfig));
    }

    //------



    public PowerModuleBase addSimpleTradeoffInt(IModule module, String tradeoffName, String firstPropertyName, String firstUnits,
                                                int firstPropertyBase, int firstPropertyMultiplier, String secondPropertyName,
                                                String secondUnits, int secondPropertyBase,
                                                int secondPropertyMultiplier) {
        this.addBasePropertyInt(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffPropertyInt(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBasePropertyInt(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffPropertyInt(tradeoffName, secondPropertyName, secondPropertyMultiplier);
        return this;
    }

    public PowerModuleBase addBasePropertyInt(String propertyName, int baseVal) {
        int propFromConfig = MPSConfig.getInstance().getPropertyIntOrDefault(getBaseString(propertyName), baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveInteger(propFromConfig));
    }

    public PowerModuleBase addBasePropertyInt(String propertyName, int baseVal, String unit) {
        int propFromConfig = MPSConfig.getInstance().getPropertyIntOrDefault(getBaseString(propertyName), baseVal);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveInteger(propFromConfig));
    }




    /** Double ------------------------------------------------------------------------------------ */
    @Nullable
    @Override
    public double applyPropertyModifiersDouble(NBTTagCompound moduleTag, String propertyName, double propertyValue) {
        Iterable<IPropertyModifier> propertyModifiersIterable = propertyModifiers.get(propertyName);
        if (propertyModifiersIterable != null) {
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                if (modifier instanceof IPropertyModifierDouble)
                    propertyValue = ((IPropertyModifierDouble) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    public PowerModuleBase addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier) {
        double propFromConfig = MPSConfig.getInstance().getPropertyDoubleOrDefault(getMultiplierString(propertyName, tradeoffName), multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveDouble(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier, String unit) {
        double propFromConfig = MPSConfig.getInstance().getPropertyDoubleOrDefault(getMultiplierString(propertyName, tradeoffName), multiplier);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveDouble(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addSimpleTradeoffDouble(IModule module,
                                                   String tradeoffName,
                                                   String firstPropertyName,
                                                   String firstUnits,
                                                   double firstPropertyBase,
                                                   double firstPropertyMultiplier,
                                                   String secondPropertyName,
                                                   String secondUnits,
                                                   double secondPropertyBase,
                                                   double secondPropertyMultiplier) {
        this.addBasePropertyDouble(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffPropertyDouble(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBasePropertyDouble(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffPropertyDouble(tradeoffName, secondPropertyName, secondPropertyMultiplier);
        return this;
    }

    public PowerModuleBase addBasePropertyDouble(String propertyName, double baseVal) {
        double propFromConfig = MPSConfig.getInstance().getPropertyDoubleOrDefault(getBaseString(propertyName), baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveDouble(propFromConfig));
    }

    public PowerModuleBase addBasePropertyDouble(String propertyName, double baseVal, String unit) {
        double propFromConfig = MPSConfig.getInstance().getPropertyDoubleOrDefault(getBaseString(propertyName), baseVal);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveDouble(propFromConfig));
    }

    public boolean equals(PowerModuleBase other) {
        return other != null && other.getUnlocalizedName().equals(this.getUnlocalizedName());
    }
}