package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.api.electricity.adapter.IMuseElectricItem;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.powermodule.cosmetic.CosmeticGlowModule;
import net.machinemuse.powersuits.powermodule.cosmetic.TintModule;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
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
        if (!ModuleManager.INSTANCE.itemHasActiveModule(stack, CosmeticGlowModule.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.INSTANCE.computeModularProperty(stack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.INSTANCE.computeModularProperty(stack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.INSTANCE.computeModularProperty(stack, CosmeticGlowModule.BLUE_GLOW);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
        return colour;
    }

    default Colour getColorFromItemStack(ItemStack stack) {
        if (!ModuleManager.INSTANCE.itemHasActiveModule(stack, TintModule.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.INSTANCE.computeModularProperty(stack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.INSTANCE.computeModularProperty(stack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.INSTANCE.computeModularProperty(stack, TintModule.BLUE_TINT);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
        return colour;
    }

    default String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    default String getToolTip(ItemStack itemStack) {
//        return itemStack.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL).toString();
//    }

    default double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }
}