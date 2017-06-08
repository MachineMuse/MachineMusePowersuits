package net.machinemuse.powersuits.item;

//import appeng.api.config.AccessRestriction;
import com.google.common.collect.Sets;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.List;
import java.util.Set;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MPSItemElectricTool extends ItemTool implements IModularItemBase, IMuseElectricItem {
    public static final Set<Block> blocksEffectiveOn = Sets.newHashSet(new Block[] {
            Blocks.COBBLESTONE, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB, Blocks.STONE_SLAB2, Blocks.STONE, Blocks.SANDSTONE,
            Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE, Blocks.IRON_BLOCK, Blocks.COAL_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK, Blocks.ICE, Blocks.NETHERRACK, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK, Blocks.REDSTONE_ORE,
            Blocks.LIT_REDSTONE_ORE, Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL});


    protected MPSItemElectricTool(float attackDamageIn, float attackSpeedIn, ToolMaterial material) {
        super(attackDamageIn, attackSpeedIn, material, blocksEffectiveOn);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @Override
    public double getCurrentMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentMPSEnergy(stack);
    }

    @Override
    public double getMaxMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getMaxMPSEnergy(stack);
    }

    @Override
    public void setCurrentMPSEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentMPSEnergy(stack, energy);
    }

    @Override
    public double drainMPSEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainMPSEnergyFrom(stack, requested);
    }

    @Override
    public double giveMPSEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveMPSEnergyTo(stack, provided);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips) {
        ModularItemBase.getInstance().addInformation(stack, playerIn, currentTipList, advancedToolTips);
    }

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    public IMuseElectricItem getManager(ItemStack stack) {
        return MuseElectricItem.getInstance().getManager(stack);
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCharge(itemStack);
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxCharge(itemStack);
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTier(itemStack);
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTransferLimit(itemStack);
    }

    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return MuseElectricItem.getInstance().charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return MuseElectricItem.getInstance().discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return MuseElectricItem.getInstance().canUse(itemStack, amount);
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getChargedItem(itemStack);
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(stack, energy, simulate);
    }

    @Override
    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(stack, energy, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getEnergyStored(theItem);
    }

    @Override
    public int getMaxEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(theItem);
    }

    /* Mekanism ----------------------------------------------------------------------------------- */
    @Override
    public double getEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEnergy(itemStack);
    }

    @Override
    public void setEnergy(ItemStack itemStack, double v) {
        MuseElectricItem.getInstance().setEnergy(itemStack, v);
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxEnergy(itemStack);
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxTransfer(itemStack);
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canReceive(itemStack);
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canSend(itemStack);
    }

    /* IModularItemBase ------------------------------------------------------------------------------ */

    @Override // TODO: get rid of this as the whole system around this has changed.
    public int getColorFromItemStack(ItemStack stack, int p1) {
        return 0;
    }

    @Override
    public Colour getGlowFromItemStack(ItemStack stack) {
        return ModularItemBase.getInstance().getGlowFromItemStack(stack);
    }

    @Override
    public Colour getColorFromItemStack(ItemStack stack) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack);
    }

    @Override
    public String formatInfo(String string, double value) {
        return ModularItemBase.getInstance().formatInfo(string, value);
    }

    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        return ModularItemBase.getInstance().getLongInfo(player, stack);
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }

    /* MPSItemElectricTool ------------------------------------------------------------------------ */
    @Override
    public double getPlayerEnergy(EntityPlayer player) {
        return 0;
    }

    @Override
    public void drainPlayerEnergy(EntityPlayer player, double drainEnergy) {

    }

    @Override
    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
    }
}