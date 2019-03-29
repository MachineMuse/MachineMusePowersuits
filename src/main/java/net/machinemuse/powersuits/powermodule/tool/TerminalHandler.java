package net.machinemuse.powersuits.powermodule.tool;

import appeng.api.AEApi;
import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import extracells.api.ECApi;
import extracells.api.IWirelessFluidTermHandler;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eximius88 on 1/13/14.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "extracells.api.IWirelessFluidTermHandler", modid = "extracells", striprefs = true),
        @Optional.Interface(iface = "appeng.api.features.IWirelessTermHandler", modid = "appliedenergistics2", striprefs = true)
})
public class TerminalHandler implements
        IWirelessTermHandler, IWirelessFluidTermHandler {

    public static void registerHandler() {
        if (ModCompatibility.isAppengLoaded()) {
            TerminalHandler handler = new TerminalHandler();
            registerAEHandler(handler);
            if (ModCompatibility.isExtraCellsLoaded()) {
                registerECHandler(handler);
            }
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void registerAEHandler(TerminalHandler handler) {
        AEApi.instance().registries().wireless().registerWirelessHandler(handler);
    }

    @Optional.Method(modid = "extracells")
    private static void registerECHandler(TerminalHandler handler) {
        ECApi.instance().registerWirelessTermHandler(handler);
    }

    public static NBTTagCompound openNbtData(@Nonnull ItemStack item) {
        NBTTagCompound compound = item.getTagCompound();
        if (compound == null)
            item.setTagCompound(compound = new NBTTagCompound());
        return compound;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean canHandle(@Nonnull ItemStack is) {
        if (is.isEmpty() || is.getTranslationKey() == null)
            return false;
        return is.getTranslationKey().equals(MPSItems.INSTANCE.powerFist.getTranslationKey());
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean usePower(EntityPlayer entityPlayer, double v, @Nonnull ItemStack itemStack) {
        if ((v * NuminaConfig.INSTANCE.getAE2Ratio()) < (ElectricItemUtils.getPlayerEnergy(entityPlayer) * NuminaConfig.INSTANCE.getAE2Ratio())) {
            ElectricItemUtils.drainPlayerEnergy(entityPlayer, (int) (v * NuminaConfig.INSTANCE.getAE2Ratio()));
            return true;
        }
        return false;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean hasPower(EntityPlayer entityPlayer, double v, @Nonnull ItemStack itemStack) {
        return ((v * NuminaConfig.INSTANCE.getAE2Ratio()) < (ElectricItemUtils.getPlayerEnergy(entityPlayer) * NuminaConfig.INSTANCE.getAE2Ratio()));
    }

    @Optional.Method(modid = "extracells")
    @Override
    public boolean isItemNormalWirelessTermToo(@Nonnull ItemStack is) {
        return true;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public IConfigManager getConfigManager(@Nonnull ItemStack itemStack) {
        IConfigManager config = new WirelessConfig(itemStack);
        config.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        config.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        config.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);

        config.readFromNBT(itemStack.getTagCompound());
        return config;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public String getEncryptionKey(@Nonnull ItemStack item) {
        if (!item.isEmpty()) {
            NBTTagCompound tag = openNbtData(item);
            if (tag != null) {
                return tag.getString("encKey");
            }
        }
        return null;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public void setEncryptionKey(@Nonnull ItemStack item, String encKey, String name) {
        if (!item.isEmpty()) {
            NBTTagCompound tag = openNbtData(item);
            if (tag != null) {
                tag.setString("encKey", encKey);
            }
        }
    }

    @Optional.Interface(iface = "appeng.api.util.IConfigManager", modid = "appliedenergistics2", striprefs = true)
    class WirelessConfig implements IConfigManager {
        final ItemStack stack;
        private final Map<Settings, Enum<?>> enums = new EnumMap<>(Settings.class);

        public WirelessConfig(@Nonnull ItemStack itemStack) {
            this.stack = itemStack;
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public Set<Settings> getSettings() {
            return enums.keySet();
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public void registerSetting(Settings settings, Enum<?> anEnum) {
            if (!enums.containsKey(settings)) {
                putSetting(settings, anEnum);
            }
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public Enum<?> getSetting(Settings settings) {
            if (enums.containsKey(settings)) {
                return enums.get(settings);
            }
            return null;
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public Enum<?> putSetting(Settings settings, Enum<?> anEnum) {
            enums.put(settings, anEnum);
            writeToNBT(stack.getTagCompound());
            return anEnum;
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            NBTTagCompound tag = new NBTTagCompound();
            if (tagCompound.hasKey("configWirelessTerminal"))
                tag = tagCompound.getCompoundTag("configWirelessTerminal");

            for (Enum e : enums.keySet())
                tag.setString(e.name(), enums.get(e).toString());
            tagCompound.setTag("configWirelessTerminal", tag);
        }

        @Optional.Method(modid = "appliedenergistics2")
        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            if (tagCompound.hasKey("configWirelessTerminal")) {
                NBTTagCompound tag = tagCompound.getCompoundTag("configWirelessTerminal");

                for (Settings key : enums.keySet()) {
                    try {
                        if (tag.hasKey(key.name())) {
                            String value = tag.getString(key.name());
                            Enum oldValue = enums.get(key);
                            Enum newValue = Enum.valueOf(oldValue.getClass(), value);
                            putSetting(key, newValue);
                        }
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
    }
}