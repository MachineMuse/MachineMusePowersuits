package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.api.module.*;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
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
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(book).getParticleTexture();
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return false;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
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