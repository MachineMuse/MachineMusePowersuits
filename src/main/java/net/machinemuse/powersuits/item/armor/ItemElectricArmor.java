package net.machinemuse.powersuits.item.armor;

//import appeng.api.config.AccessRestriction;

import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.module.cosmetic.CosmeticGlowModule;
import net.machinemuse.powersuits.item.module.cosmetic.TintModule;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.numina.math.MuseMathUtils.clampDouble;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements IMuseItem, IMuseElectricItem {
    public ItemElectricArmor(ItemArmor.ArmorMaterial material, int renderIndexIn, EntityEquipmentSlot slot) {
        super(material, renderIndexIn, slot);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return getColorFromItemStack(stack).getInt();
    }

    @Override
    public Colour getGlowFromItemStack(ItemStack stack) {
        if (!ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.BLUE_GLOW);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
        return colour;
    }

    @Override
    public Colour getColorFromItemStack(ItemStack stack) {
        if (!ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.BLUE_TINT);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
        return colour;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseItemUtils.addInformation(stack, player, currentTipList, advancedToolTips);
    }

    @Override
    public String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

    /* IMuseItem ------------------------------------------------------------------------------- */
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", getEnergyStored(stack)) + 'J');
        info.add(formatInfo("Weight", WeightHelper.getTotalWeight(stack)) + 'g');
        return info;
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }

    //=================================================================================
    @Override
    public boolean hasColor(ItemStack stack) {
        return true;
    }

    @Override
    public int getColor(ItemStack stack) {
        Colour c = this.getColorFromItemStack(stack);
//        setColor(stack, c.getInt());

        return c.getInt();
    }

//    @Override
//    public void setColor(ItemStack stack, int color) {
//        NBTTagCompound nbttagcompound = stack.getTagCompound();
//
//        if (nbttagcompound == null)
//        {
//            nbttagcompound = new NBTTagCompound();
//            stack.setTagCompound(nbttagcompound);
//        }
//
//        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
//
//        if (!nbttagcompound.hasKey("display", 10))
//        {
//            nbttagcompound.setTag("display", nbttagcompound1);
//        }
//
//        nbttagcompound1.setInteger("color", color);
//    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    public int receiveEnergy(ItemStack container, int energy, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(energy, simulate) : 0;
    }

    public int extractEnergy(ItemStack container, int energy, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.extractEnergy(energy, simulate) : 0;
    }
}