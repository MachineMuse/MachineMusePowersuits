package net.machinemuse.powersuits.entity;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLuxCapacitor extends EntityThrowable {
    public double red;
    public double green;
    public double blue;

    public EntityLuxCapacitor(World par1World) {
        super(par1World);
    }

    public EntityLuxCapacitor(World world, EntityLivingBase shootingEntity, double r, double g, double b) {
        this(world, shootingEntity);
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public EntityLuxCapacitor(World par1World, EntityLivingBase shootingEntity) {
        super(par1World, shootingEntity);
        Vec3d direction = shootingEntity.getLookVec().normalize();
        double speed = 1.0;
        this.motionX = direction.xCoord * speed;
        this.motionY = direction.yCoord * speed;
        this.motionZ = direction.zCoord * speed;
        double r = 0.4375;
        double xoffset = 0.1;
        double yoffset = 0;
        double zoffset = 0;
        double horzScale = Math.sqrt(direction.xCoord * direction.xCoord + direction.zCoord * direction.zCoord);
        double horzx = direction.xCoord / horzScale;
        double horzz = direction.zCoord / horzScale;
        this.posX = shootingEntity.posX + direction.xCoord * xoffset - direction.yCoord * horzx * yoffset - horzz * zoffset;
        this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.yCoord * xoffset + (1 - Math.abs(direction.yCoord)) * yoffset;
        this.posZ = shootingEntity.posZ + direction.zCoord * xoffset - direction.yCoord * horzz * yoffset + horzx * zoffset;
        this.setEntityBoundingBox(new AxisAlignedBB(posX - r, posY - 0.0625, posZ - r, posX + r, posY + 0.0625, posZ + r));
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.ticksExisted > 400) {
            this.setDead();
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingobjectposition) {

        if (!this.isDead && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumFacing dir = movingobjectposition.sideHit.getOpposite();
            int x = movingobjectposition.getBlockPos().getX() - dir.getFrontOffsetX();
            int y = movingobjectposition.getBlockPos().getY() - dir.getFrontOffsetY();
            int z = movingobjectposition.getBlockPos().getZ() - dir.getFrontOffsetZ();
            BlockPos blockPos1 = new BlockPos(x, y, z);
            if (y > 0) {
                Block block = worldObj.getBlockState(blockPos1).getBlock();
                if (block == null || block.isAir(worldObj.getBlockState(blockPos1), worldObj, blockPos1)) {
                    Block blockToStickTo = worldObj.getBlockState(new BlockPos(movingobjectposition.getBlockPos().getX(), movingobjectposition.getBlockPos().getY(), movingobjectposition.getBlockPos().getZ())).getBlock();
                    if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos1), worldObj, blockPos1)) {
                        worldObj.setBlockState(new BlockPos(x, y, z),MPSItems.luxCapacitor().getDefaultState().withProperty(MPSItems.luxCapacitor().FACING, dir));
                        worldObj.setTileEntity(new BlockPos(x, y, z), new TileEntityLuxCapacitor(dir, red, green, blue));
                    } else {
                        for (EnumFacing d : EnumFacing.VALUES) {
                            int xo = x + d.getFrontOffsetX();
                            int yo = y + d.getFrontOffsetY();
                            int zo = z + d.getFrontOffsetZ();
                            BlockPos blockPos2 = new BlockPos(xo, yo, zo);
                            blockToStickTo = worldObj.getBlockState( new BlockPos(xo, yo, zo)).getBlock();
                            if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos2), worldObj, new BlockPos(x, y, z))) {
                                worldObj.setBlockState(new BlockPos(x, y, z),MPSItems.luxCapacitor().getDefaultState().withProperty(MPSItems.luxCapacitor().FACING, d));
                                worldObj.setTileEntity(new BlockPos(x, y, z), new TileEntityLuxCapacitor(d, red, green, blue));
                            }
                        }
                    }
                }
            }
            this.setDead();
        }
    }
}
