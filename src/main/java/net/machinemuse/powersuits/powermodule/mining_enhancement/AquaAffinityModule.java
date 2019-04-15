package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import javax.annotation.Nonnull;


// Note: tried as an enchantment, but failed to function properly due to how block breaking code works
public class AquaAffinityModule extends PowerModuleBase implements IMiningEnhancementModule, IBlockBreakingModule {
    public AquaAffinityModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addBasePropertyDouble(MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "RF");
        addBasePropertyDouble(MPSModuleConstants.UNDERWATER_HARVEST_SPEED, 0.2, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION, 1000);
        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.UNDERWATER_HARVEST_SPEED, 0.8);
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
    public String getDataName() {
        return MPSModuleConstants.MODULE_AQUA_AFFINITY__DATANAME;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos, int playerEnergy) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy) {
        if (this.canHarvestBlock(itemStack, state, (EntityPlayer) entityLiving, pos, playerEnergy)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, getEnergyUsage(itemStack));
            return true;
        }
        return false;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION);
    }

    @Nonnull
    @Override
    public ItemStack getEmulatedTool() {
        return ItemStack.EMPTY;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();

        if (event.getNewSpeed() > 1
                && (player.isInsideOfMaterial(Material.WATER) || !player.onGround)
                && ElectricItemUtils.getPlayerEnergy(player) > getEnergyUsage(stack)) {
            event.setNewSpeed((float) (event.getNewSpeed() * 5 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.UNDERWATER_HARVEST_SPEED)));
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aquaAffinity;
    }
}