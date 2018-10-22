package net.machinemuse.powersuits.entity;


import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;

public class EntityPlasmaBolt extends EntityThrowable implements IEntityAdditionalSpawnData {
    public static final int SIZE = 24;
    public double size;
    public double damagingness;
    public double explosiveness;
    public Entity shootingEntity;

    public EntityPlasmaBolt(World world) {
        super(world);
    }

    public EntityPlasmaBolt(World world, EntityLivingBase shootingEntity, double explosivenessIn, double damagingnessIn, int chargeTicks) {
        super(world);
        this.shootingEntity = shootingEntity;
        this.size = ((chargeTicks) > 50 ? 50 : chargeTicks);
        this.explosiveness = explosivenessIn;
        this.damagingness = damagingnessIn;
        Vec3d direction = shootingEntity.getLookVec().normalize();
        double scale = 1.0;
        this.motionX = direction.x * scale;
        this.motionY = direction.y * scale;
        this.motionZ = direction.z * scale;
        double r = this.size / 50.0;
        double xoffset = 1.3f + r - direction.y * shootingEntity.getEyeHeight();
        double yoffset = -.2;
        double zoffset = 0.3f;
        double horzScale = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
        double horzx = direction.x / horzScale;
        double horzz = direction.z / horzScale;
        this.posX = shootingEntity.posX + direction.x * xoffset - direction.y * horzx * yoffset - horzz * zoffset;
        this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.y * xoffset + (1 - Math.abs(direction.y)) * yoffset;
        this.posZ = shootingEntity.posZ + direction.z * xoffset - direction.y * horzz * yoffset + horzx * zoffset;
        this.setEntityBoundingBox(new AxisAlignedBB(posX - r, posY - r, posZ - r, posX + r, posY + r, posZ + r));
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.ticksExisted > this.getMaxLifetime()) {
            this.setDead();
        }
        if (this.isInWater()) {
            this.setDead();
            for (int var3 = 0; var3 < this.size; ++var3) {
                this.world.spawnParticle(EnumParticleTypes.FLAME,
                        this.posX + Math.random() * 1, this.posY + Math.random() * 1, this.posZ + Math.random()
                                * 0.1,
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    public int getMaxLifetime() {
        return 200;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they
     * walk on. used for spiders and wolves to prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound var1) {
        this.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound var1) {
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        double damage = this.size / 50.0 * this.damagingness;
        switch (result.typeOfHit) {
            case ENTITY:
                if (result.entityHit != null && result.entityHit != shootingEntity) {
                    result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), (int) damage);
                }
                break;
            case BLOCK:
                break;
            default:
                break;
        }
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) (this.size / 50.0f * 3 * this.explosiveness), flag);
        }
        for (int var3 = 0; var3 < 8; ++var3) {
            this.world.spawnParticle(EnumParticleTypes.FLAME,
                    this.posX + Math.random() * 0.1, this.posY + Math.random() * 0.1, this.posZ + Math.random() * 0.1,
                    0.0D, 0.0D, 0.0D);
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            this.setDead();
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeDouble(this.size);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.size = additionalData.readDouble();
    }
}