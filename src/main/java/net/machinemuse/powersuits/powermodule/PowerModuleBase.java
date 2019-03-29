package net.machinemuse.powersuits.powermodule;

import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.nbt.propertymodifier.*;
import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.machinemuse.numina.constants.NuminaNBTConstants.TAG_ONLINE;

public abstract class PowerModuleBase implements IPowerModule {
    protected static Map<String, String> units = new HashMap<>();
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected NBTTagCompound defaultTag;
    protected boolean isAllowed;
    EnumModuleTarget moduleTarget;

    public PowerModuleBase(String dataNameIn, EnumModuleTarget moduleTargetIn) {
        this.moduleTarget = moduleTargetIn;
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(TAG_ONLINE, true);
        this.isAllowed = MPSConfig.INSTANCE.getModuleAllowedorDefault(dataNameIn, true);
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
    }

    public PowerModuleBase(EnumModuleTarget moduleTargetIn) {
        this.moduleTarget = moduleTargetIn;
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(TAG_ONLINE, true);
        this.isAllowed = MPSConfig.INSTANCE.getModuleAllowedorDefault(getDataName(), true);
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
    }

    @SideOnly(Side.CLIENT)
    public static String getUnit(String propertyName) {
        String unit = units.get(propertyName);
        if (unit != null && unit.startsWith(MPSModuleConstants.MODULE_TRADEOFF_PREFIX))
            unit = I18n.format(unit);

        return unit == null ? "" : unit;
    }

    @Override
    public EnumModuleTarget getTarget() {
        return this.moduleTarget;
    }

    @Override
    public abstract TextureAtlasSprite getIcon(ItemStack item);

    @Override
    public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
        return propertyModifiers;
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

    public PowerModuleBase addPropertyModifier(String propertyName, IPropertyModifier modifier) {
        List<IPropertyModifier> modifiers = propertyModifiers.get(propertyName);
        if (modifiers == null) {
            modifiers = new LinkedList();
        }
        modifiers.add(modifier);
        propertyModifiers.put(propertyName, modifiers);
        return this;
    }

    @Override
    public double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue) {
        Iterable<IPropertyModifier> propertyModifiersIterable = propertyModifiers.get(propertyName);
        if (propertyModifiersIterable != null && itemTag.hasKey(this.getDataName())) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(this.getDataName());
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                if (modifier instanceof IPropertyModifierDouble)
                    propertyValue = ((IPropertyModifierDouble) modifier).applyModifier(moduleTag, propertyValue);
//                else if (modifier instanceof IPropertyModifierInteger)
//                    propertyValue = ((IPropertyModifierInteger) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }


    /** Double ------------------------------------------------------------------------------------ */
    /**
     * Adds a base key and multiplierValue to the map based on the config setting.
     */
    public PowerModuleBase addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier) {
        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveDouble(tradeoffName, propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    public PowerModuleBase addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier, String unit) {
        units.put(propertyName, unit);
        return addTradeoffPropertyDouble(tradeoffName, propertyName, multiplier);
    }

//    public PowerModuleBase addSimpleTradeoffDouble(IPowerModule module,
//                                                   String tradeoffName,
//                                                   String firstPropertyName,
//                                                   String firstUnits,
//                                                   double firstPropertyBase,
//                                                   double firstPropertyMultiplier,
//                                                   String secondPropertyName,
//                                                   String secondUnits,
//                                                   double secondPropertyBase,
//                                                   double secondPropertyMultiplier) {
//        this.addBasePropertyDouble(firstPropertyName, firstPropertyBase, firstUnits);
//        this.addTradeoffPropertyDouble(tradeoffName, firstPropertyName, firstPropertyMultiplier);
//        this.addBasePropertyDouble(secondPropertyName, secondPropertyBase, secondUnits);
//        this.addTradeoffPropertyDouble(tradeoffName, secondPropertyName, secondPropertyMultiplier);
//        return this;
//    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     */
    public PowerModuleBase addBasePropertyDouble(String propertyName, double baseVal) {
        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append(".base").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveDouble(propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    public PowerModuleBase addBasePropertyDouble(String propertyName, double baseVal, String unit) {
        units.put(propertyName, unit);
        return addBasePropertyDouble(propertyName, baseVal);
    }


    /**
     * Integer -----------------------------------------------------------------------------------
     */
    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, int multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, propFromConfig, roundTo, offset));
    }
//
//    public PowerModuleBase addTradeoffPropertyInteger(String tradeoffName, String propertyName, int multiplier) {
//        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
//        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, multiplier);
//        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveInteger(tradeoffName, propFromConfig));
//    }
//
//
//    public PowerModuleBase addTradeoffPropertyInteger(String tradeoffName, String propertyName, int multiplier, String unit) {
//        units.put(propertyName, unit);
//        return addTradeoffPropertyInteger(tradeoffName, propertyName, multiplier);
//    }

//    public PowerModuleBase addSimpleTradeoffInteger(IPowerModule module, String tradeoffName, String firstPropertyName, String firstUnits,
//                                                    int firstPropertyBase, int firstPropertyMultiplier, String secondPropertyName,
//                                                    String secondUnits, int secondPropertyBase,
//                                                    int secondPropertyMultiplier) {
//        this.addBasePropertyInteger(firstPropertyName, firstPropertyBase, firstUnits);
//        this.addTradeoffPropertyInteger(tradeoffName, firstPropertyName, firstPropertyMultiplier);
//        this.addBasePropertyInteger(secondPropertyName, secondPropertyBase, secondUnits);
//        this.addTradeoffPropertyInteger(tradeoffName, secondPropertyName, secondPropertyMultiplier);
//        return this;
//    }
//
//    public PowerModuleBase addBasePropertyInteger(String propertyName, int baseVal) {
//        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append(".base").toString();
//        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, baseVal);
//        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveInteger(propFromConfig));
//    }
//
//    public PowerModuleBase addBasePropertyInteger(String propertyName, int baseVal, String unit) {
//        units.put(propertyName, unit);
//        return addBasePropertyInteger(propertyName, baseVal);
//    }

    public boolean equals(PowerModuleBase other) {
        return other != null && other.getDataName().equals(this.getDataName());
    }

    @Override
    public String getUnlocalizedName() {
        return "module." + MPSModConstants.MODID + "." + getDataName();
    }
}