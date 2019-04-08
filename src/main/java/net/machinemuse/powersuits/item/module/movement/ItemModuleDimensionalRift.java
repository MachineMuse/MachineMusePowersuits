package net.machinemuse.powersuits.item.module.movement;


import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class ItemModuleDimensionalRift extends ItemAbstractPowerModule implements IRightClickModule {
    final int theOverworld = 0;
    final int theNether = -1;
    final int theEnd = 1;


    public ItemModuleDimensionalRift(String regName) {
        super(regName, EnumModuleTarget.FEETONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        addBasePropertyDouble(MPSModuleConstants.HEAT_GENERATION, 55);
//        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 200000);
//        this.defaultTag.setBoolean(TAG_ONLINE, false);
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        if (!playerIn.isRiding() && !playerIn.isBeingRidden() && playerIn.isNonBoss() && ((playerIn instanceof EntityPlayerMP))) {
//            EntityPlayerMP player = (EntityPlayerMP) playerIn;
//            BlockPos coords = playerIn.bedLocation != null ? playerIn.bedLocation : playerIn.world.getSpawnPoint();
//
//
//
//            while ((worldIn.getBlockState(new BlockPos(coords.getX(), coords.getY(), coords.getZ())).getBlock() != Blocks.AIR) &&
//                    (worldIn.getBlockState(coords.up())) != Blocks.AIR) {
//                coords = coords.up();
//            }
//
//
//            playerIn.changeDimension(0, new CommandTeleporter(coords));
//
//
//            int energyConsumption = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.ENERGY_CONSUMPTION);
//            int playerEnergy = ElectricItemUtils.getPlayerEnergy(playerIn);
//            if (playerEnergy >= energyConsumption) {
//                // Fixme ... absolutely need a cooldown timer.
//                // Fixme... was looking at the teleport command, not much different meh
//                //...FIXME ... testing a builtin method which is hopefully faster and less of a server hit.
//////            Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
//////
//////                float yaw = player.rotationYaw;
//////
//////                if (argYaw.isRelative()) {
//////                    set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
//////                } else {
//////                    yaw = MathHelper.wrapDegrees(yaw);
//////                }
//////
//////                float f1 = (float) argPitch.getAmount();
//////
//////                if (argPitch.isRelative()) {
//////                    set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
//////                } else {
//////                    f1 = MathHelper.wrapDegrees(f1);
//////                }
//////
//////                player.dismountRidingEntity();
//////                player.connection.setPlayerLocation(argX.getResult(), argY.getResult(), argZ.getResult(), yaw, f1, set);
//////                player.setRotationYawHead(yaw);
//////            } else {
//////                float f2 = (float) MathHelper.wrapDegrees(argYaw.getResult());
//////                float f3 = (float) MathHelper.wrapDegrees(argPitch.getResult());
//////                f3 = MathHelper.clamp(f3, -90.0F, 90.0F);
//////                player.setLocationAndAngles(argX.getResult(), argY.getResult(), argZ.getResult(), f2, f3);
//////                player.setRotationYawHead(f2);
//////            }
//////
//////            if (!(player instanceof EntityLivingBase) || !((EntityLivingBase) player).isElytraFlying()) {
//////                player.motionY = 0.0D;
//////                player.onGround = true;
//////            }
//////
//////            playerIn.moveToBlockPosAndAngles(targetPos, playerIn.rotationYaw, playerIn.rotationPitch);
////
////            if (playerIn.dimension != theNether) {
////
////
////                playerIn.setLocationAndAngles(0.5D, playerIn.posY, 0.5D, player.rotationYaw, player.rotationPitch);
////                player.mcServer.getPlayerList().transferPlayerToDimension(player, theNether, new DimensionalRiftHelper(player.mcServer.getWorld(theNether)));
////                ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.ENERGY_CONSUMPTION));
////                MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.HEAT_GENERATION));
////            } else if (player.dimension == theNether || player.dimension == theEnd)
////                player.setLocationAndAngles(0.5D, player.posY, 0.5D, player.rotationYaw, player.rotationPitch);
////            player.mcServer.getPlayerList().transferPlayerToDimension(player, 0, new DimensionalRiftHelper(player.mcServer.getWorld(theOverworld)));
////
////
////
////            if (player.dimension == theOverworld) {
////                BlockPos coords = (player instanceof EntityPlayer) ? (player).getBedLocation(player.dimension) : null;
////                if ((coords == null) || ((coords.getX() == 0) && (coords.getY() == 0) && (coords.getZ() == 0))) {
////                    coords = worldIn.getSpawnPoint();
////                }
////                int yPos = coords.getY();
////                while ((worldIn.getBlockState(new BlockPos(coords.getX(), yPos, coords.getZ())).getBlock() != Blocks.AIR) &&
////                        (worldIn.getBlockState(new BlockPos(coords.getX(), yPos + 1, coords.getZ())) != Blocks.AIR)) {
////                    yPos++;
////                }
////                (player).setPositionAndUpdate(coords.getX() + 0.5D, yPos, coords.getZ() + 0.5D);
////            }
//                ElectricItemUtils.drainPlayerEnergy(player, getEnergyUsage(itemStackIn));
//                MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.HEAT_GENERATION));
//                return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//            }
//        }
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
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
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
