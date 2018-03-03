package net.machinemuse.powersuits.item.module.energy;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.capabilities.MPSCapProvider;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by leon on 7/3/16.
 */
public class UltimateBatteryModule extends PowerModuleBase implements IMuseElectricItem {
    public UltimateBatteryModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ALLITEMS, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.evcapacitor, 1));
//        addBasePropertyDouble(ElectricItemUtils.MAXIMUM_ENERGY, 1250000, "J");
//        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 1500, "g");
//        addTradeoffPropertyDouble("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 4250000);
//        addTradeoffPropertyDouble("Battery Size", MPSModuleConstants.WEIGHT, 6000);
////        addBasePropertyDouble(ElectricConversions.IC2_TIER, 1);
////        addTradeoffPropertyDouble("IC2 Tier", ElectricConversions.IC2_TIER, 3);
//
//        addBasePropertyInt(ElectricConversions.IC2_TIER, 1);
//        addTradeoffPropertyInt("IC2 Tier", ElectricConversions.IC2_TIER, 3);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENERGY;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.ultimateBattery;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
        return new MPSCapProvider(stack, stack.getTagCompound());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> currentTipList, ITooltipFlag flagIn) {
        if (MPSConfig.getInstance().doAdditionalInfo()) {
            String message =  I18n.format("tooltip.componentTooltip");
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
            currentTipList.add(message);
            String description = I18n.format(this.getUnlocalizedName(stack) + ".desc");
            currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
        } else {
            currentTipList.add(MPSConfig.getInstance().additionalInfoInstructions());
        }

        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null)
            return;

        currentTipList.add(MuseStringUtils.formatNumberShort(energyStorage.getEnergyStored()) + "/" + MuseStringUtils.formatNumberShort(energyStorage.getMaxEnergyStored()) + "RF");
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
}