package net.machinemuse.powersuits.entity;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.geometry.Colour;
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
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;
import static net.minecraft.block.BlockDirectional.FACING;

public class EntityLuxCapacitor extends EntityThrowable implements IEntityAdditionalSpawnData {
    public Colour color;

    public EntityLuxCapacitor(World par1World) {
        super(par1World);
    }

    public EntityLuxCapacitor(World world, EntityLivingBase shootingEntity, Colour color) {
        this(world, shootingEntity);
        this.color = color;
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
    protected void onImpact(RayTraceResult hitResult) {
        if (!this.isDead && hitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumFacing dir = hitResult.sideHit.getOpposite();
            int x = hitResult.getBlockPos().getX() - dir.getFrontOffsetX();
            int y = hitResult.getBlockPos().getY() - dir.getFrontOffsetY();
            int z = hitResult.getBlockPos().getZ() - dir.getFrontOffsetZ();
            if (y > 0) {
                BlockPos blockPos = new BlockPos(x, y, z);
                Block block = worldObj.getBlockState(blockPos).getBlock();
                if (block == null || block.isAir(worldObj.getBlockState(blockPos), worldObj, blockPos)) {
                    Block blockToStickTo = worldObj.getBlockState(new BlockPos(hitResult.getBlockPos().getX(),
                            hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ())).getBlock();
                    if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos), worldObj, blockPos)) {
                        worldObj.setBlockState(blockPos, ((IExtendedBlockState) MPSItems.luxCapacitor.getDefaultState()).withProperty(COLOR, color));
                        worldObj.setTileEntity(blockPos, new TileEntityLuxCapacitor(dir, color));

                    } else {
                        for (EnumFacing d : EnumFacing.VALUES) {
                            int xo = x + d.getFrontOffsetX();
                            int yo = y + d.getFrontOffsetY();
                            int zo = z + d.getFrontOffsetZ();
                            BlockPos blockPos2 = new BlockPos(xo, yo, zo);
                            blockToStickTo = worldObj.getBlockState( new BlockPos(xo, yo, zo)).getBlock();
                            if (blockToStickTo.isNormalCube(worldObj.getBlockState(blockPos2), worldObj, blockPos)) {
                                worldObj.setBlockState(blockPos, ((IExtendedBlockState) MPSItems.luxCapacitor.getDefaultState()).withProperty(COLOR, color));
                                worldObj.setTileEntity(blockPos, new TileEntityLuxCapacitor(d, color));
                                break;
                            }
                        }
                    }
                }
                this.setDead();
            }
        }
    }

    /* using these to sync color between client and server, since without this, color isn't initialized */
    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(color.getInt());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.color = new Colour(additionalData.readInt());
    }
}