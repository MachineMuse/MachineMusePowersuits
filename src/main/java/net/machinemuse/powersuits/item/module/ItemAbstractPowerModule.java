package net.machinemuse.powersuits.item.module;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.nbt.propertymodifier.*;
import net.machinemuse.powersuits.basemod.MPSConfig;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.constants.MPSModuleConstants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.machinemuse.numina.constants.NuminaNBTConstants.TAG_ONLINE;

public class ItemAbstractPowerModule extends Item implements IPowerModule {
    protected final EnumModuleTarget target;
    protected final EnumModuleCategory category;
    protected static Map<String, String> units = new HashMap<>();
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected boolean isAllowed;

    public ItemAbstractPowerModule(String regName, EnumModuleTarget targetIn, EnumModuleCategory categoryIn) {
        this(regName, new Item.Properties()
                .maxStackSize(1)
                .group(MPSItems.INSTANCE.creativeTab)
                .defaultMaxDamage(-1)
                .setNoRepair(), targetIn, categoryIn);
    }

    public ItemAbstractPowerModule(String regName, Properties properties, EnumModuleTarget targetIn, EnumModuleCategory categoryIn) {
        super(properties);
        this.target = targetIn;
        this.category = categoryIn;
        setRegistryName(regName);
        this.propertyModifiers = new HashMap();
        this.isAllowed = MPSConfig.INSTANCE.getModuleAllowedorDefault(regName, true);
    }

    public EnumModuleTarget getTarget() {
        return target;
    }

    public EnumModuleCategory getCategory() {
        return category;
    }

    @Override
    public double applyPropertyModifiers(NBTTagCompound moduleTag, String propertyName, double propertyValue) {
        Iterable<IPropertyModifier> propertyModifiersIterable = propertyModifiers.get(propertyName);
        if (propertyModifiersIterable != null) {
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                if (modifier instanceof IPropertyModifierDouble)
                    propertyValue = ((IPropertyModifierDouble) modifier).applyModifier(moduleTag, propertyValue);
//                else if (modifier instanceof IPropertyModifierInteger)
//                    propertyValue = ((IPropertyModifierInteger) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
        return propertyModifiers;
    }

    @Override
    public boolean isAllowed() {
        return this.isAllowed;
    }

    @OnlyIn(Dist.CLIENT)
    public static String getUnit(String propertyName) {
        String unit = units.get(propertyName);
        if (unit != null && unit.startsWith(MPSModuleConstants.MODULE_TRADEOFF_PREFIX))
            unit = I18n.format(unit);

        return unit == null ? "" : unit;
    }

    public void setIsAllowed(boolean allowed) {
        this.isAllowed = allowed;
    }

    public ItemAbstractPowerModule addPropertyModifier(String propertyName, IPropertyModifier modifier) {
        List<IPropertyModifier> modifiers = propertyModifiers.get(propertyName);
        if (modifiers == null) {
            modifiers = new LinkedList();
        }
        modifiers.add(modifier);
        propertyModifiers.put(propertyName, modifiers);
        return this;
    }

    /** Double ------------------------------------------------------------------------------------ */
    /**
     * Adds a base key and multiplierValue to the map based on the config setting.
     */
    public ItemAbstractPowerModule addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier) {
        String key = new StringBuilder(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveDouble(tradeoffName, propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    public ItemAbstractPowerModule addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier, String unit) {
        units.put(propertyName, unit);
        return addTradeoffPropertyDouble(tradeoffName, propertyName, multiplier);
    }

//    public ItemAbstractPowerModule addSimpleTradeoffDouble(IPowerModule module,
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
    public ItemAbstractPowerModule addBasePropertyDouble(String propertyName, double baseVal) {
        String key = new StringBuilder(propertyName).append(".base").toString();
        double propFromConfig = MPSConfig.INSTANCE.getPropertyDoubleOrDefault(key, baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveDouble(propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    public ItemAbstractPowerModule addBasePropertyDouble(String propertyName, double baseVal, String unit) {
        units.put(propertyName, unit);
        return addBasePropertyDouble(propertyName, baseVal);
    }


    /**
     * Integer -----------------------------------------------------------------------------------
     */
    public ItemAbstractPowerModule addIntTradeoffProperty(String tradeoffName, String propertyName, int multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        String key = new StringBuilder(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, propFromConfig, roundTo, offset));
    }
//
//    public ItemAbstractPowerModule addTradeoffPropertyInteger(String tradeoffName, String propertyName, int multiplier) {
//        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append('.').append(tradeoffName).append(".multiplier").toString();
//        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, multiplier);
//        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveInteger(tradeoffName, propFromConfig));
//    }
//
//
//    public ItemAbstractPowerModule addTradeoffPropertyInteger(String tradeoffName, String propertyName, int multiplier, String unit) {
//        units.put(propertyName, unit);
//        return addTradeoffPropertyInteger(tradeoffName, propertyName, multiplier);
//    }

//    public ItemAbstractPowerModule addSimpleTradeoffInteger(IPowerModule module, String tradeoffName, String firstPropertyName, String firstUnits,
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
//    public ItemAbstractPowerModule addBasePropertyInteger(String propertyName, int baseVal) {
//        String key = new StringBuilder(getDataName()).append('.').append(propertyName).append(".base").toString();
//        int propFromConfig = MPSConfig.INSTANCE.getPropertyIntegerOrDefault(key, baseVal);
//        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveInteger(propFromConfig));
//    }
//
//    public ItemAbstractPowerModule addBasePropertyInteger(String propertyName, int baseVal, String unit) {
//        units.put(propertyName, unit);
//        return addBasePropertyInteger(propertyName, baseVal);
//    }
}