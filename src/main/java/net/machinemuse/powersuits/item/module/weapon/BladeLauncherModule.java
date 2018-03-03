package net.machinemuse.powersuits.item.module.weapon;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
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
    public static final String BLADE_ENERGY = "Spinning Blade Energy Consumption";
    public static final String BLADE_DAMAGE = "Spinning Blade Damage";

    public BladeLauncherModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(BLADE_ENERGY, 500, "J");
        addBasePropertyDouble(BLADE_DAMAGE, 6, "pt");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_WEAPON;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, BLADE_ENERGY)) {
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
            int energyConsumption = ModuleManager.getInstance().computeModularPropertyInteger(itemStack, BLADE_ENERGY);
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
