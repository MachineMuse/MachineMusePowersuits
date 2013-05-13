package net.machinemuse.powersuits.powermodule;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.IPropertyModifier;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

import java.util.*;

public abstract class PowerModuleBase implements IPowerModule {
    protected List<ItemStack> installCost;
    protected List<IModularItem> validItems;
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected static Map<String, String> units = new HashMap<String, String>();
    protected NBTTagCompound defaultTag;
    protected boolean isAllowed;
    protected Icon icon;

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
        this.isAllowed = Config.getConfig().get("Modules", getName(), true).getBoolean(true);
    }

    @Override
    public Icon getIcon(ItemStack item) {
        return icon;
    }

    @Override
    public void registerIcon(IconRegister register) {
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
    public boolean isValidForItem(ItemStack stack, EntityPlayer player) {
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
        if (propertyModifiersIterable != null && itemTag.hasKey(this.getName())) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(this.getName());
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
        double propFromConfig = Config.getConfig().get("Properties", getName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier)
                .getDouble(multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
        double propFromConfig = Config.getConfig().get("Properties", getName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier)
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
        double propFromConfig = Config.getConfig().get("Properties", getName() + '.' + propertyName + ".base", baseVal).getDouble(baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal, String unit) {
        double propFromConfig = Config.getConfig().get("Properties", getName() + '.' + propertyName + ".base", baseVal).getDouble(baseVal);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public boolean equals(PowerModule other) {
        return other != null && other.getName().equals(this.getName());
    }

    @Override
    public String getStitchedTexture(ItemStack item) {
        return "/gui/items.png";
        // alternatively
        // return "/terrain.png";
    }

}
