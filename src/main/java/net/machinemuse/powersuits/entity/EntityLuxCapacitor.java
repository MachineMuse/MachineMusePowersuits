package net.machinemuse.powersuits.entity;

import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.tileentity.TileEntityLuxCapacitor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.PacketBuffer;
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

    BlockItemUseContext getUseContext(BlockPos pos, EnumFacing facing, RayTraceResult hitResult) {
        return new BlockItemUseContext(
                new ItemUseContext(
                        (EntityPlayer)this.thrower,
                        new ItemStack(MPSItems.INSTANCE.itemLuxCapacitor),
                        pos,
                        facing,
                        hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ()));
    }

    public EntityLuxCapacitor(World world) {
        super(MPSItems.LUX_CAPACITOR_ENTITY_TYPE, world);
        if (color == null)
            color = Colour.WHITE;
    }

    public EntityLuxCapacitor(World world, EntityLivingBase shootingEntity, Colour color) {
        super(MPSItems.LUX_CAPACITOR_ENTITY_TYPE, shootingEntity, world);
        this.color = color != null ? color : BlockLuxCapacitor.defaultColor;
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
        this.setBoundingBox(new AxisAlignedBB(posX - r, posY - 0.0625, posZ - r, posX + r, posY + 0.0625, posZ + r));
    }

    @Override
    protected void onImpact(RayTraceResult hitResult) {
        if (color == null)
            color = Colour.WHITE;

        if (!this.removed && hitResult.type == RayTraceResult.Type.BLOCK) {
            EnumFacing dir = hitResult.sideHit.getOpposite();
            int x = hitResult.getBlockPos().getX() - dir.getXOffset();
            int y = hitResult.getBlockPos().getY() - dir.getYOffset();
            int z = hitResult.getBlockPos().getZ() - dir.getZOffset();
            if (y > 0) {
                BlockPos blockPos = new BlockPos(x, y, z);
                if (MPSItems.INSTANCE.luxCapacitor.getDefaultState().isValidPosition(world, blockPos)) {
                    IBlockState blockState = MPSItems.INSTANCE.luxCapacitor.getStateForPlacement(getUseContext(blockPos, hitResult.sideHit, hitResult));
                    world.setBlockState(blockPos, ((IExtendedBlockState) blockState).withProperty(COLOR, color));
                    world.setTileEntity(blockPos, new TileEntityLuxCapacitor(color));
                } else {
                    for (EnumFacing facing : EnumFacing.values()) {
                        if (MPSItems.INSTANCE.luxCapacitor.getDefaultState().with(FACING, facing).isValidPosition(world, blockPos)) {
                            IBlockState blockState = MPSItems.INSTANCE.luxCapacitor.getStateForPlacement(getUseContext(blockPos, facing, hitResult));
                            world.setBlockState(blockPos, ((IExtendedBlockState) blockState).withProperty(COLOR, color));
                            world.setTileEntity(blockPos, new TileEntityLuxCapacitor(color));
                            break;
                        }
                    }
                }
                this.remove();
            }
        }
    }

    /* using these to sync color between client and server, since without this, color isn't initialized */
    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        if (color == null)
            color = Colour.WHITE;
        buffer.writeInt(color.getInt());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.color = new Colour(additionalData.readInt());
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksExisted > 400) {
            this.remove();
        }
    }
}