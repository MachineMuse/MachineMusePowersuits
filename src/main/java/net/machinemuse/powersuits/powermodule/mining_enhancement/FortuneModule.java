package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IEnchantmentModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class FortuneModule extends PowerModuleBase implements IEnchantmentModule, IMiningEnhancementModule {
    ItemStack book;

    public FortuneModule(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);
        book = new ItemStack(Items.ENCHANTED_BOOK);
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
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.FORTUNE_ENCHANTMENT_LEVEL);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(book).getParticleTexture();
    }

    /**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos Block's position in world
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (!player.world.isRemote) {
            if (getEnergyUsage(itemstack) > ElectricItemUtils.getPlayerEnergy(player))
                removeEnchantment(itemstack);
            else
                ElectricItemUtils.drainPlayerEnergy(player, getEnergyUsage(itemstack));
        }
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