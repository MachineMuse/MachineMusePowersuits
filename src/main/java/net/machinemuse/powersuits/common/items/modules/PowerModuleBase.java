package net.machinemuse.powersuits.common.items.modules;

import net.machinemuse.numina.api.module.ILocalizeableModule;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.item.IModule;
import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public abstract class PowerModuleBase implements ILocalizeableModule {
    protected List<ItemStack> defaultInstallCost;
    protected List<IModularItem> validItems;
    protected Map<String, List<IPropertyModifier>> propertyModifiers;
    protected static Map<String, String> units = new HashMap<>();
    protected NBTTagCompound defaultTag;
    protected boolean isAllowed;
    protected TextureAtlasSprite icon;

    public PowerModuleBase(String name, List<IModularItem> validItems) {
        this.validItems = validItems;
        this.defaultInstallCost = new ArrayList();
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
//        this.isAllowed = MPSSettings.modules.getOrDefault(name, true);
        this.isAllowed = MPSSettings.modules.getModuleAllowedorDefault(name, true);
    }

    public PowerModuleBase(List<IModularItem> validItems) {
        this.validItems = validItems;
        this.defaultInstallCost = new ArrayList();
        this.propertyModifiers = new HashMap();
        this.defaultTag = new NBTTagCompound();
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
//        this.isAllowed = MPSSettings.modules.getOrDefault(this.getDataName(), true);
        this.isAllowed = MPSSettings.modules.getModuleAllowedorDefault(this.getDataName(), true);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return icon;
    }

    @Override
    public List<ItemStack> getInstallCost() {
        if(ModuleManager.hasCustomInstallCost(this.getDataName())) {
            return ModuleManager.getCustomInstallCost(this.getDataName());
        } else {
            return defaultInstallCost;
        }
    }

    public PowerModuleBase addInstallCost(ItemStack stack) {
        this.defaultInstallCost.add(stack);
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
                propertyValue = (double) modifier.applyModifier(moduleTag, propertyValue);
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
        double propFromConfig = MPSSettings.modules.getPropertyDoubleorDefault(getDataName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier);
        return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
    }

    public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
        double propFromConfig = MPSSettings.modules.getPropertyDoubleorDefault(getDataName() + '.' + propertyName + '.' + tradeoffName + ".multiplier", multiplier);
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

    public PowerModuleBase addSimpleTradeoff(IModule module, String tradeoffName, String firstPropertyName, String firstUnits,
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
        double propFromConfig = MPSSettings.modules.getPropertyDoubleorDefault(getDataName() + '.' + propertyName + ".base", baseVal);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public PowerModuleBase addBaseProperty(String propertyName, double baseVal, String unit) {
        double propFromConfig = MPSSettings.modules.getPropertyDoubleorDefault(getDataName() + '.' + propertyName + ".base", baseVal);
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
    }

    public boolean equals(PowerModule other) {
        return other != null && other.getDataName().equals(this.getDataName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getStitchedTexture(ItemStack item) {
        return MuseTextureUtils.TEXTURE_QUILT;
    }

    @Override
    public String getUnlocalizedName() {
        return "Unknown Module";
    }
}