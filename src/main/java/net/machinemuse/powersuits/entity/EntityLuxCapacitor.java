package net.machinemuse.powersuits.entity;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.utils.math.Colour;
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
        this.motionX = direction.x * speed;
        this.motionY = direction.y * speed;
        this.motionZ = direction.z * speed;
        double r = 0.4375;
        double xoffset = 0.1;
        double yoffset = 0;
        double zoffset = 0;
        double horzScale = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
        double horzx = direction.x / horzScale;
        double horzz = direction.z / horzScale;
        this.posX = shootingEntity.posX + direction.x * xoffset - direction.y * horzx * yoffset - horzz * zoffset;
        this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.y * xoffset + (1 - Math.abs(direction.y)) * yoffset;
        this.posZ = shootingEntity.posZ + direction.z * xoffset - direction.y * horzz * yoffset + horzx * zoffset;
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
                Block block = world.getBlockState(blockPos).getBlock();
                if (block == null || block.isAir(world.getBlockState(blockPos), world, blockPos)) {
                    Block blockToStickTo = world.getBlockState(new BlockPos(hitResult.getBlockPos().getX(),
                            hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ())).getBlock();
                    if (blockToStickTo.isNormalCube(world.getBlockState(blockPos), world, blockPos) &&
                            !(blockToStickTo instanceof BlockLuxCapacitor) && color != null) {
                        // FIXME: disabled while working on models.
                        world.setBlockState(blockPos, ((IExtendedBlockState) BlockLuxCapacitor.getInstance().getDefaultState().
                                withProperty(FACING, dir)).withProperty(COLOR, color));
                        world.setTileEntity(blockPos, new TileEntityLuxCapacitor(color));

                    } else {
                        for (EnumFacing d : EnumFacing.VALUES) {
                            int xo = x + d.getFrontOffsetX();
                            int yo = y + d.getFrontOffsetY();
                            int zo = z + d.getFrontOffsetZ();
                            BlockPos blockPos2 = new BlockPos(xo, yo, zo);
                            blockToStickTo = world.getBlockState( new BlockPos(xo, yo, zo)).getBlock();
                            // FIXME: disabled while working on models
                            if (blockToStickTo.isNormalCube(world.getBlockState(blockPos2), world, blockPos) &&
                                    !(blockToStickTo instanceof BlockLuxCapacitor) && color != null) {
                                world.setBlockState(blockPos, ((IExtendedBlockState) BlockLuxCapacitor.getInstance().getDefaultState().
                                        withProperty(FACING, dir)).withProperty(COLOR, color));
                                world.setTileEntity(blockPos, new TileEntityLuxCapacitor(color));
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
        if (color != null)
            buffer.writeInt(color.getInt());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.color = new Colour(additionalData.readInt());
    }
}