package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IEnchantmentModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;


public class SilkTouchModule extends PowerModuleBase implements IEnchantmentModule, IMiningEnhancementModule {
    final ItemStack book;

    // TODO: add trade off and/or power consumption and a toggle mechanic... maybe through ticking

    public SilkTouchModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        book = new ItemStack(Items.ENCHANTED_BOOK);
        book.addEnchantment(Enchantments.SILK_TOUCH, 1);

//        ModuleManager.INSTANCE.addInstallCost(getDataName(), book);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 12));

        addBasePropertyDouble(MPSModuleConstants.SILK_TOUCH_ENERGY_CONSUMPTION, 2500, "RF");
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.apiaristArmor;
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
            else {
                Block block = player.world.getBlockState(pos).getBlock();
                if (block.canSilkHarvest(player.world, pos, player.world.getBlockState(pos), player)) {
                    ElectricItemUtils.drainPlayerEnergy(player, getEnergyUsage(itemstack));
                }
            }
        }
        return false;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SILK_TOUCH_ENERGY_CONSUMPTION);
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_SILK_TOUCH__DATANAME;
    }

    @Override
    public Enchantment getEnchantment() {
        return Enchantments.SILK_TOUCH;
    }

    @Override
    public int getLevel(@Nonnull ItemStack itemStack) {
        return 1;
    }
}