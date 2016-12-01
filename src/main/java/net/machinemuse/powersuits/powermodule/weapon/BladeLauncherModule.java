package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BladeLauncherModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_BLADE_LAUNCHER = "Blade Launcher";
    public static final String BLADE_ENERGY = "Spinning Blade Energy Consumption";
    public static final String BLADE_DAMAGE = "Spinning Blade Damage";

    public BladeLauncherModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(BLADE_ENERGY, 500, "J");
        addBaseProperty(BLADE_DAMAGE, 6, "pt");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_BLADE_LAUNCHER;
    }

    @Override
    public String getUnlocalizedName() { return "bladeLauncher";
    }

    @Override
    public String getDescription() {
        return "Launches a spinning blade of death (or shearing).";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (ElectricItemUtils.getPlayerEnergy(playerIn) > ModuleManager.computeModularProperty(itemStackIn, BLADE_ENERGY)) {
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return new ActionResult(EnumActionResult.PASS, this);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        // int chargeTicks = Math.max(itemStack.getMaxItemUseDuration() - par4, 10);

        if (!worldIn.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStack, BLADE_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy((EntityPlayer) entityLiving) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, energyConsumption);
                EntitySpinningBlade blade = new EntitySpinningBlade(worldIn, entityLiving);
                worldIn.spawnEntityInWorld(blade);
            }
        }
    }

//    @Override
//    public EnumAction getItemUseAction(ItemStack stack) {
//        return EnumAction.BOW;
//    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.bladeLauncher;
    }
}