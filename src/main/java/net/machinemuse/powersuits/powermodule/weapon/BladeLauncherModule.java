package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BladeLauncherModule extends PowerModuleBase implements IRightClickModule {
    public BladeLauncherModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
        addBasePropertyDouble(MPSModuleConstants.BLADE_ENERGY, 5000, "RF");
        addBasePropertyDouble(MPSModuleConstants.BLADE_DAMAGE, 6, "pt");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_BLADE_LAUNCHER__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.BLADE_ENERGY)) {
                playerIn.setActiveHand(hand);
                return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
            }
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        // int chargeTicks = Math.max(itemStack.getMaxItemUseDuration() - par4, 10);
        if (!worldIn.isRemote) {
            int energyConsumption = getEnergyUsage(itemStack);
            if (ElectricItemUtils.getPlayerEnergy((EntityPlayer) entityLiving) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, energyConsumption);
                EntitySpinningBlade blade = new EntitySpinningBlade(worldIn, entityLiving);
                worldIn.spawnEntity(blade);
            }
        }
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) Math.round(ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.BLADE_ENERGY));
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.bladeLauncher;
    }
}