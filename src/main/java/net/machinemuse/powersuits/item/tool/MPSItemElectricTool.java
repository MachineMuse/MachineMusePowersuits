package net.machinemuse.powersuits.item;

//import appeng.api.config.AccessRestriction;
import com.google.common.collect.Sets;
import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.machinemuse.numina.math.geometry.Colour;
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
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips) {
        ModularItemBase.getInstance().addInformation(stack, playerIn, currentTipList, advancedToolTips);
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