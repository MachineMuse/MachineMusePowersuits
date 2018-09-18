package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.item.IModeChangingItem;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.electricity.adapter.ElectricAdapter;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public abstract class MuseCommonStrings {
    /**
     * Adds information to the item's tooltip when 'getting' it.
     *
     * @param stack            The itemstack to get the tooltip for
     * @param worldIn          The world (unused)
     * @param currentTipList   A list of strings containing the existing tooltip. When
     *                         passed, it will just contain the name of the item;
     *                         enchantments and lore are
     *                         appended afterwards.
     * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
     *                         their settings.
     */
    public static void addInformation(@Nonnull ItemStack stack, World worldIn, List currentTipList, ITooltipFlag advancedToolTips) {
        EntityPlayer player  = Minecraft.getMinecraft().player;

        // Mode changing item such as power fist
        if (stack.getItem() instanceof IModeChangingItem) {
            String mode = MuseItemUtils.getStringOrNull(stack, NuminaNBTConstants.TAG_MODE);
            if (mode != null)
                currentTipList.add(I18n.format("tooltip.mode") + " " + MuseStringUtils.wrapFormatTags(mode, MuseStringUtils.FormatCodes.Red));
            else
                currentTipList.add(I18n.format("tooltip.changeModes"));
        }

        // this is just some random info on the fluids installed
        if (stack.getItem() instanceof ItemPowerArmorChestplate) {
            String fluidInfo;

            NBTTagCompound itemNBT = MuseNBTUtils.getMuseItemTag(stack);
            if (itemNBT.hasKey(MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME, Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound moduleTag = itemNBT.getCompoundTag(MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME);
                if (moduleTag != null) {
                    NBTTagCompound fluidTag = moduleTag.getCompoundTag(NuminaNBTConstants.FLUID_NBT_KEY);
                    FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTag);
                    if (fluidStack != null) {
                        int waterLevel = fluidStack != null ? fluidStack.amount : 0;
                        int fluidTemp = fluidStack != null ? fluidStack.getFluid().getTemperature() : 0;

                        if (waterLevel > 0) {
                            fluidInfo = I18n.format(fluidStack.getLocalizedName()) + " " + MuseStringUtils.formatNumberShort(waterLevel) + '/'
                                    + MuseStringUtils.formatNumberShort(100000);
                            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                                    MuseStringUtils.FormatCodes.Indigo));

                            fluidInfo = I18n.format(fluidStack.getLocalizedName()) + " " + MuseStringUtils.formatNumberFromUnits(fluidTemp, "°");


                            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                                    MuseStringUtils.FormatCodes.Indigo));
                        }
                    }
                }
            }

            if (itemNBT.hasKey(MPSModuleConstants.ADVANCED_COOLING_SYSTEM__DATANAME, Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound moduleTag = itemNBT.getCompoundTag(MPSModuleConstants.ADVANCED_COOLING_SYSTEM__DATANAME);
                if (moduleTag != null) {
                    NBTTagCompound fluidTag = moduleTag.getCompoundTag(NuminaNBTConstants.FLUID_NBT_KEY);
                    FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTag);
                    if (fluidStack != null) {
                        int fluidLevel = fluidStack != null ? fluidStack.amount : 0;

                        if (fluidLevel > 0) {
                            int fluidTemp = fluidStack != null ? fluidStack.getFluid().getTemperature() : 0;
                            String fluidName = fluidStack != null ? fluidStack.getLocalizedName() : null;

                            fluidInfo = I18n.format(fluidName) + " " + MuseStringUtils.formatNumberShort(fluidLevel) + '/'
                                    + MuseStringUtils.formatNumberShort(100000);
                            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                                    MuseStringUtils.FormatCodes.Indigo));

                            fluidInfo = I18n.format(fluidStack.getLocalizedName()) + " " + MuseStringUtils.formatNumberFromUnits(fluidTemp, "°");

                            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                                    MuseStringUtils.FormatCodes.Indigo));
                        }
                    }
                }
            }
        }

        ElectricAdapter adapter = ElectricAdapter.wrap(stack);
        if (adapter != null) {
            String energyinfo = I18n.format("tooltip.energy") + " " + MuseStringUtils.formatNumberShort(adapter.getEnergyStored()) + '/'
                    + MuseStringUtils.formatNumberShort(adapter.getMaxEnergyStored());
            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(energyinfo, MuseStringUtils.FormatCodes.Italic.character,
                    MuseStringUtils.FormatCodes.Aqua));
        }
        if (MPSConfig.INSTANCE.doAdditionalInfo()) {
            List<String> installed = MuseCommonStrings.getItemInstalledModules(player, stack);
            if (installed.size() == 0) {
                String message = I18n.format("tooltip.noModules");
                currentTipList.addAll(MuseStringUtils.wrapStringToLength(message, 30));
            } else {
                currentTipList.add(I18n.format("tooltip.installedModules"));
                for (String moduleName : installed) {
                    currentTipList.add(MuseStringUtils.wrapFormatTags(moduleName, MuseStringUtils.FormatCodes.Indigo));
                }
//                currentTipList.addAll(installed);
            }
        } else {
            currentTipList.add(additionalInfoInstructions());
        }
    }

    @SideOnly(Side.CLIENT)
    public static String additionalInfoInstructions() {
        String message = I18n.format("tooltip.pressShift");
        return MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
    }


    // //////////////////////// //
    // --- OTHER PROPERTIES --- //
    // //////////////////////// //
    public static double getOrSetModuleProperty(NBTTagCompound moduleTag, String propertyName, double defaultValue) {
        if (!moduleTag.hasKey(propertyName)) {
            moduleTag.setDouble(propertyName, defaultValue);
        }
        return moduleTag.getDouble(propertyName);
    }

//    public static double getTotalWeight(ItemStack stack) {
//        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.WEIGHT);
//    }

    public static List<String> getItemInstalledModules(EntityPlayer player, ItemStack stack) {
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        List<String> modules = new LinkedList();
        for (IPowerModule module : ModuleManager.INSTANCE.getValidModulesForItem(stack)) {
            if (ModuleManager.INSTANCE.tagHasModule(itemTag, module.getDataName())) {
                modules.add(module.getDataName());
            }
        }
        return modules;
    }
}
