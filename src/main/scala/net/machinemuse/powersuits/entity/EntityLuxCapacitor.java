package net.machinemuse.powersuits.entity;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.FACING;

public class EntityLuxCapacitor extends EntityThrowable {
    public Colour color = new Colour(1.0D, 1.0D, 1.0D, 1.0D);

    public EntityLuxCapacitor(World par1World) {
        super(par1World);
    }

    public EntityLuxCapacitor(World world, EntityLivingBase shootingEntity, int color) {
        this(world, shootingEntity);
        this.color = new Colour(color);

//        System.out.println("=============================");
//        System.out.println("this.Colour int: " + (this.color.getInt() & 0xFFFFFFFFL));
//        System.out.println("this.Colour red: " + (this.color.r));
//        System.out.println("this.Colour green: " + (this.color.g));
//        System.out.println("this.Colour blue: " + (this.color.b));
//        System.out.println("this.Colour alpha: " + (this.color.a));
//        System.out.println("=============================");


    }

    public EntityLuxCapacitor(World world, EntityLivingBase shootingEntity, double r, double g, double b) {
        this(world, shootingEntity);
        this.color = new Colour(r, g, b, 1.0);
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
    protected void onImpact(RayTraceResult rayTraceResult) {
        if (!this.isDead && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumFacing dir = rayTraceResult.sideHit.getOpposite();
            int x = rayTraceResult.getBlockPos().getX() - dir.getFrontOffsetX();
            int y = rayTraceResult.getBlockPos().getY() - dir.getFrontOffsetY();
            int z = rayTraceResult.getBlockPos().getZ() - dir.getFrontOffsetZ();
            BlockPos blockPos1 = new BlockPos(x, y, z);
            if (y > 0) {
                Block block = worldObj.getBlockState(blockPos1).getBlock();
                if (block == null || block.isAir(worldObj.getBlockState(blockPos1), worldObj, blockPos1)) {
                    Block blockToStickTo = worldObj.getBlockState(new BlockPos(rayTraceResult.getBlockPos().getX(), rayTraceResult.getBlockPos().getY(), rayTraceResult.getBlockPos().getZ())).getBlock();
                    if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos1), worldObj, blockPos1)) {
                        createBlockAndTE(blockPos1, dir);
                    } else {
                        for (EnumFacing d : EnumFacing.VALUES) {
                            int xo = x + d.getFrontOffsetX();
                            int yo = y + d.getFrontOffsetY();
                            int zo = z + d.getFrontOffsetZ();
                            BlockPos blockPos2 = new BlockPos(xo, yo, zo);
                            blockToStickTo = worldObj.getBlockState( new BlockPos(xo, yo, zo)).getBlock();
                            if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos2), worldObj, blockPos1)) {
                                createBlockAndTE(blockPos1, d);
                                break;
                            }
                        }
                    }
                }
            }
            this.setDead();
        }
    }

    private void createBlockAndTE(BlockPos pos, EnumFacing facing) {
        worldObj.setBlockState(pos,BlockLuxCapacitor.instance.getDefaultState().withProperty(FACING, facing.getOpposite()));




        worldObj.setTileEntity(pos, new TileEntityLuxCapacitor(facing, color));
    }
}
