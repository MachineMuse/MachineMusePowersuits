package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
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

public class BladeLauncherModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_BLADE_LAUNCHER = "Blade Launcher";
    public static final String BLADE_ENERGY = "Spinning Blade Energy Consumption";
    public static final String BLADE_DAMAGE = "Spinning Blade Damage";

    public BladeLauncherModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addBaseProperty(BLADE_ENERGY, 500, "J");
        addBaseProperty(BLADE_DAMAGE, 6, "pt");
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_BLADE_LAUNCHER;
    }

    @Override
    public String getUnlocalizedName() {
        return "bladeLauncher";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > ModuleManager.INSTANCE.computeModularProperty(itemStackIn, BLADE_ENERGY)) {
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
            double energyConsumption = ModuleManager.INSTANCE.computeModularProperty(itemStack, BLADE_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy((EntityPlayer) entityLiving) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, energyConsumption);
                EntitySpinningBlade blade = new EntitySpinningBlade(worldIn, entityLiving);
                worldIn.spawnEntity(blade);
            }
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.bladeLauncher;
    }
}
