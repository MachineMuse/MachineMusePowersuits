package net.machinemuse.powersuits.powermodule;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.IPropertyModifier;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.*;

public abstract class PowerModuleBase implements IPowerModule {
    protected List<ItemStack> installCost;
    protected List<IModularItem> validItems;
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected static Map<String, String> units = new HashMap<String, String>();
    protected static Map<String, String> locales = new HashMap<String, String>();
    protected NBTTagCompound defaultTag;
    //protected boolean isCreativeOnly; // Idea: Allow the option to make any module creative only?
    protected boolean isAllowed;
    protected IIcon icon;
    
    static {
        // Common property display strings. Implemented as such with plans to switch to unlocalized strings [where/when]ever possible. -- Korynkai 20150209
        addPropertyLocalString(MuseCommonStrings.WEIGHT, StatCollector.translateToLocal("module.common.weight"));
        addPropertyLocalString(MuseCommonStrings.ARMOR_VALUE_PHYSICAL, StatCollector.translateToLocal("module.common.armor.physical"));
        addPropertyLocalString(MuseCommonStrings.ARMOR_VALUE_ENERGY, StatCollector.translateToLocal("module.common.armor.energy"));
        addPropertyLocalString(MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION, StatCollector.translateToLocal("module.common.armor.consumption"));
        addPropertyLocalString("Plating Thickness", StatCollector.translateToLocal("module.common.armor.thickness"));
        addPropertyLocalString(ElectricConversions.IC2_TIER(), StatColletor.translateToLocal("module.common.battery.ic2Tier"));
        addPropertyLocalString("Battery Size", StatColletor.translateToLocal("module.common.battery.size"));
        addPropertyLocalString(ElectricItemUtils.CURRENT_HEAT(), StatCollector.translateToLocal("module.common.heat.current"));
        addPropertyLocalString(ElectricItemUtils.MAXIMUM_HEAT(), StatCollector.translateToLocal("module.common.heat.maximum"));
        addPropertyLocalString(ElectricItemUtils.CURRENT_ENERGY(), StatCollector.translateToLocal("module.common.energy.current"));
        addPropertyLocalString(ElectricItemUtils.MAXIMUM_ENERGY(), StatCollector.translateToLocal("module.common.energy.maximum"));
        addPropertyLocalString("Voltage", StatCollector.translateToLocal("module.common.voltage"));
        addPropertyLocalString("Amperage", StatCollector.translateToLocal("module.common.amperage"));
        addPropertyLocalString("Overclock", StatCollector.translateToLocal("module.common.overclock"));
        addPropertyLocalString("Thrust", StatCollector.translateToLocal("module.common.thrust"));
        addPropertyLocalString("Compensation", StatCollectior.translateToLocal("module.common.compensation"));
        addPropertyLocalString("Power", StatCollectior.translateToLocal("module.common.power"));
        addPropertyLocalString("Range", StatCollector.translateToLocal("module.common.range"));
        addPropertyLocalString("Red", StatCollector.translateToLocal("module.common.red"));
        addPropertyLocalString("Green", StatCollector.translateToLocal("module.common.green"));
        addPropertyLocalString("Blue", StatCollector.translateToLocal("module.common.blue"));
    }

    public PowerModuleBase(String name, List<IModularItem> validItems) {
        this.validItems = validItems;
        this.installCost = new ArrayList();
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
        this.isAllowed = Config.getConfig().get("Modules", name, true).getBoolean(true);
    }

    public PowerModuleBase(List<IModularItem> validItems) {
        this.validItems = validItems;
        this.installCost = new ArrayList();
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
        this.isAllowed = Config.getConfig().get("Modules", getDataName(), true).getBoolean(true);
    }

    @Override
    public IIcon getIcon(ItemStack item) {
        return icon;
    }

    @Override
    public void registerIcon(IIconRegister register) {
        if (getTextureFile() != null) {
            this.icon = register.registerIcon(MuseIcon.ICON_PREFIX + getTextureFile());
        }
    }

    public abstract String getTextureFile();

    @Override
    public List<ItemStack> getInstallCost() {
        return installCost;
    }

    public PowerModuleBase addInstallCost(ItemStack stack) {
        this.installCost.add(stack);
        return this;
    }

    @Override
    public boolean isValidForItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IModularItem && this.validItems.contains(item);
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

    @Override
    public boolean isCreativeOnly() {
        return false;
    }

    public void setIsAllowed(boolean allowed) {
        this.isAllowed = allowed;
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier) {
        double propFromConfig = Config.getConfig().get("Properties", getDataName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier)
                .getDouble(multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
        double propFromConfig = Config.getConfig().get("Properties", getDataName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier)
                .getDouble(multiplier);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
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
    
    public void addPropertyLocalString(String propertyName, String localString) {
        if (locales.containsKey(propertyName)) {
            MuseLogger.logError("Module property localization string \"" + propertyName + "\" already exists. Skipping.");
            MuseLogger.logError("This message should only ever occur if a module is declaring a property which already exists.");
            MuseLogger.logError("Please notify the developer of this issue.");
        } else {
            locales.put(propertyName, localString);
        }
    }
    
    public PowerModuleBase addSimpleTradeoff(IPowerModule module, String tradeoffName, String firstPropertyName, String firstUnits,
                                             double firstPropertyBase, double firstPropertyMultiplier, String secondPropertyName, String secondUnits, double secondPropertyBase,
                                             double secondPropertyMultiplier) {
        this.addBaseProperty(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffProperty(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBaseProperty(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffProperty(tradeoffName, secondPropertyName, secondPropertyMultiplier);
        return this;
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal) {
        double propFromConfig = Config.getConfig().get("Properties", getDataName() + '.' + propertyName + ".base", baseVal).getDouble(baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal, String unit) {
        double propFromConfig = Config.getConfig().get("Properties", getDataName() + '.' + propertyName + ".base", baseVal).getDouble(baseVal);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public boolean equals(PowerModule other) {
        return other != null && other.getDataName().equals(this.getDataName());
    }

    @Override
    public String getStitchedTexture(ItemStack item) {
        return MuseTextureUtils.ITEM_TEXTURE_QUILT();
        // alternatively
        // return "/terrain.png";
    }

}
