package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
            Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone,
            Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore,
            Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore,
            Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail});


    protected MPSItemElectricTool(float damageBonus, ToolMaterial material) {
        super(damageBonus, material, blocksEffectiveOn);

    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @Override
    public double getCurrentEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(stack);
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getMaxEnergy(stack);
    }

    @Override
    public void setCurrentEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentEnergy(stack, energy);
    }

    @Override
    public double drainEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainEnergyFrom(stack, requested);
    }

    @Override
    public double giveEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveEnergyTo(stack, provided);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        ModularItemBase.getInstance().addInformation(stack, player, currentTipList, advancedToolTips);
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

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    public double injectAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().injectAEPower(stack, ae);
    }

    @Override
    public double extractAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().extractAEPower(stack, ae);
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAEMaxPower(stack);
    }

    @Override
    public double getAECurrentPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAECurrentPower(stack);
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack stack) {
        return MuseElectricItem.getInstance().getPowerFlow(stack);
    }

    /* IModularItemBase ------------------------------------------------------------------------------ */
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