package net.machinemuse.powersuits.item.tool;

//import appeng.api.config.AccessRestriction;

import com.google.common.collect.Sets;
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
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.machinemuse.numina.math.MuseMathUtils.clampDouble;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MPSItemElectricTool extends ItemTool implements IMuseItem, IMuseElectricItem {
    public static final Set<Block> blocksEffectiveOn = Sets.newHashSet(new Block[] {
            Blocks.COBBLESTONE, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB, Blocks.STONE_SLAB2, Blocks.STONE, Blocks.SANDSTONE,
            Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE, Blocks.IRON_BLOCK, Blocks.COAL_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK, Blocks.ICE, Blocks.NETHERRACK, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK, Blocks.REDSTONE_ORE,
            Blocks.LIT_REDSTONE_ORE, Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL});


    protected MPSItemElectricTool(float attackDamageIn, float attackSpeedIn, ToolMaterial material) {
        super(attackDamageIn, attackSpeedIn, material, blocksEffectiveOn);
    }

    @SideOnly(Side.CLIENT)
    @Override // TODO: get rid of this as the whole system around this has changed.
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        MuseItemUtils.addInformation(stack, tooltip, flagIn);
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
        info.add(formatInfo("Energy Storage", this.getEnergyStored(stack)) + 'J');
        info.add(formatInfo("Weight", WeightHelper.getTotalWeight(stack)) + 'g');
        return info;
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

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }
}