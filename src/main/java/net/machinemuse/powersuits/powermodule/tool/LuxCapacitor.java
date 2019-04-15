package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public LuxCapacitor(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.GLOWSTONE_DUST, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.IRON_INGOT, 2));
        addBasePropertyDouble(MPSModuleConstants.LUX_CAPACITOR_ENERGY_CONSUMPTION, 1000, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.RED, MPSModuleConstants.LUX_CAPACITOR_RED_HUE, 1, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.GREEN, MPSModuleConstants.LUX_CAPACITOR_GREEN_HUE, 1, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.BLUE, MPSModuleConstants.LUX_CAPACITOR_BLUE_HUE, 1, "%");
//        addTradeoffPropertyDouble("alpha", )// TODO: add alpha for the lense
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_LUX_CAPACITOR__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        if (!worldIn.isRemote) {
            double energyConsumption = getEnergyUsage(itemStackIn);
            MuseHeatUtils.heatPlayer(playerIn, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, (int) energyConsumption);

                double red = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_RED_HUE);
                double green = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_GREEN_HUE);
                double blue = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_BLUE_HUE);

                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(worldIn, playerIn, new Colour(red, green, blue));
                worldIn.spawnEntity(luxCapacitor);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.LUX_CAPACITOR_ENERGY_CONSUMPTION);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.luxCapacitor;
    }
}
