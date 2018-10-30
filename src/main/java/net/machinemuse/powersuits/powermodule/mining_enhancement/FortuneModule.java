package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.api.module.*;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class FortuneModule extends PowerModuleBase implements IEnchantmentModule, IMiningEnhancementModule {
    public FortuneModule(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);
        addBasePropertyDouble(MPSModuleConstants.FORTUNE_ENERGY_CONSUMPTION, 500, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.ENCHANTMENT_LEVEL, MPSModuleConstants.FORTUNE_ENERGY_CONSUMPTION, 9500);
        addIntTradeoffProperty(MPSModuleConstants.ENCHANTMENT_LEVEL, MPSModuleConstants.FORTUNE_ENCHANTMENT_LEVEL, 3, "", 1, 1);
    }

    @Override
    public Enchantment getEnchantment() {
        return Enchantments.FORTUNE;
    }

    @Override
    public int getLevel(@Nonnull ItemStack itemStack) {

        System.out.println("level: " + (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.FORTUNE_ENCHANTMENT_LEVEL));

        return 0;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return null;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return false;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.FORTUNE_ENERGY_CONSUMPTION);
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_FORTUNE_DATANAME;
    }
}
