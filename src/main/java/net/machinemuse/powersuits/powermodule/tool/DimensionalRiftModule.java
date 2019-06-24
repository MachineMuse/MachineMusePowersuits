package net.machinemuse.powersuits.powermodule.tool;


import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;

import static net.machinemuse.numina.constants.NuminaNBTConstants.TAG_ONLINE;


/**
 * Created by Eximius88 on 2/3/14.
 */
public class DimensionalRiftModule extends PowerModuleBase implements IRightClickModule {
    final int theOverworld = 0;
    final int theNether = -1;
    final int theEnd = 1;


    public DimensionalRiftModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addBasePropertyDouble(MPSModuleConstants.HEAT_GENERATION, 55);
        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 200000);
        this.defaultTag.setBoolean(TAG_ONLINE, false);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_DIMENSIONAL_RIFT__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!playerIn.isRiding() && !playerIn.isBeingRidden() && playerIn.isNonBoss() && ((playerIn instanceof EntityPlayerMP))) {
            EntityPlayerMP player = (EntityPlayerMP) playerIn;
            BlockPos coords = playerIn.bedLocation != null ? playerIn.bedLocation : playerIn.world.getSpawnPoint();

            while (!worldIn.isAirBlock(coords) && !worldIn.isAirBlock(coords.up())) {
                coords = coords.up();
            }

            playerIn.changeDimension(0, new CommandTeleporter(coords));
            int energyConsumption = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.ENERGY_CONSUMPTION);
            int playerEnergy = ElectricItemUtils.getPlayerEnergy(playerIn);
            if (playerEnergy >= energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(player, getEnergyUsage(itemStackIn));
                MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.HEAT_GENERATION));
                return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
            }
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
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.dimRiftGen;
    }

    // Copied from Forge.
    private static class CommandTeleporter implements ITeleporter
    {
        private final BlockPos targetPos;

        private CommandTeleporter(BlockPos targetPos)
        {
            this.targetPos = targetPos;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw)
        {
            entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);
        }
    }
}
