package net.machinemuse.powersuits.item;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.adapter.IMuseElectricItem;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.powermodule.cosmetic.CosmeticGlowModule;
import net.machinemuse.powersuits.powermodule.cosmetic.TintModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.numina.general.MuseMathUtils.clampDouble;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IModularItemBase extends IModularItem, IMuseElectricItem {
    @SideOnly(Side.CLIENT)
    default int getColorFromItemStack(ItemStack stack, int par2) {
        return getColorFromItemStack(stack).getInt();
    }

    default Colour getGlowFromItemStack(ItemStack stack) {
        if (!ModuleManager.itemHasActiveModule(stack, CosmeticGlowModule.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.BLUE_GLOW);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
        return colour;
    }

    default Colour getColorFromItemStack(ItemStack stack) {
        if (!ModuleManager.itemHasActiveModule(stack, TintModule.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.computeModularProperty(stack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.computeModularProperty(stack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.computeModularProperty(stack, TintModule.BLUE_TINT);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
        return colour;
    }

    @SideOnly(Side.CLIENT)
    default void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips);
    }

    default String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

    @SideOnly(Side.CLIENT)
    @Override
    default String getToolTip(ItemStack itemStack) {
        return itemStack.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL).toString();
    }

    /* IModularItem ------------------------------------------------------------------------------- */
    @Override
    default List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", getCurrentMPSEnergy(stack)) + 'J');
        info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + 'g');
        return info;
    }

    default double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }
}