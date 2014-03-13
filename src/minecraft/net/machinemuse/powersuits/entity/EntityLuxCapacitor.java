package net.machinemuse.powersuits.entity;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

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
        Vec3 direction = shootingEntity.getLookVec().normalize();
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
        this.boundingBox.setBounds(posX - r, posY - 0.0625, posZ - r, posX + r, posY + 0.0625, posZ + r);
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
    protected void onImpact(MovingObjectPosition movingobjectposition) {

        if (!this.isDead && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
            ForgeDirection dir = ForgeDirection.values()[movingobjectposition.sideHit].getOpposite();
            int x = movingobjectposition.blockX - dir.offsetX;
            int y = movingobjectposition.blockY - dir.offsetY;
            int z = movingobjectposition.blockZ - dir.offsetZ;
            if (y > 0) {
                int blockID = worldObj.getBlockId(x, y, z);
                if (blockID <= 0 || Block.blocksList[blockID] == null || Block.blocksList[blockID].isAirBlock(worldObj, x, y, z)) {
                    int blockToStickTo = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                    if (Block.isNormalCube(blockToStickTo)) {
                        worldObj.setBlock(x, y, z, BlockLuxCapacitor.assignedBlockID, 0, 7);
                        worldObj.setBlockTileEntity(x, y, z, new TileEntityLuxCapacitor(dir.getOpposite(), red, green, blue));
                    } else {
                        for (ForgeDirection d : ForgeDirection.values()) {
                            int xo = x + d.offsetX;
                            int yo = y + d.offsetY;
                            int zo = z + d.offsetZ;
                            blockToStickTo = worldObj.getBlockId(xo, yo, zo);
                            if (Block.isNormalCube(blockToStickTo)) {
                                worldObj.setBlock(x, y, z, BlockLuxCapacitor.assignedBlockID, 0, 7);
                                worldObj.setBlockTileEntity(x, y, z, new TileEntityLuxCapacitor(d, red, green, blue));
                            }
                        }
                    }
                }
            }
            this.setDead();
        }
    }
}
