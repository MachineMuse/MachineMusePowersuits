package net.machinemuse.powersuits.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityBlinkDriveBolt extends EntityThrowable {
	public int lifespan;
	public double size;
	public static final int SIZE = 24;
	public Entity shootingEntity;

	public EntityBlinkDriveBolt(World world)
	{
		super(world);
		dataWatcher.addObject(SIZE, Byte.valueOf((byte) this.size));
	}

	public EntityBlinkDriveBolt(World world, EntityLiving shootingEntity) {
		super(world);
		this.shootingEntity = shootingEntity;
		this.size = 12;
		Vec3 direction = shootingEntity.getLookVec().normalize();
		double scale = 1.0;
		this.motionX = direction.xCoord * scale;
		this.motionY = direction.yCoord * scale;
		this.motionZ = direction.zCoord * scale;
		double r = this.size / 50.0 - direction.yCoord * shootingEntity.getEyeHeight();
		double xoffset = 1.3f + r;
		double yoffset = -.2;
		double zoffset = 0.3f;
		Vec3 horzdir = direction.normalize();
		horzdir.yCoord = 0;
		horzdir = horzdir.normalize();
		this.posX = shootingEntity.posX + direction.xCoord * xoffset - direction.yCoord * horzdir.xCoord * yoffset - horzdir.zCoord * zoffset;
		this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.yCoord * xoffset + (1 - Math.abs(direction.yCoord)) * yoffset;
		this.posZ = shootingEntity.posZ + direction.zCoord * xoffset - direction.yCoord * horzdir.zCoord * yoffset + horzdir.xCoord * zoffset;
		this.boundingBox.setBounds(posX - r, posY - r, posZ - r, posX + r, posY + r, posZ + r);
		dataWatcher.addObject(SIZE, Byte.valueOf((byte) this.size));
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.ticksExisted > this.getMaxLifetime()) {
			this.setDead();
		}
		this.size = dataWatcher.getWatchableObjectByte(SIZE);
		if (this.isInWater()) {
			this.setDead();
			for (int var3 = 0; var3 < this.size; ++var3)
			{
				this.worldObj.spawnParticle("flame", this.posX + Math.random() * 1, this.posY + Math.random() * 1, this.posZ + Math.random()
						* 0.1,
						0.0D, 0.0D, 0.0D);
			}

		}
	}

	public int getMaxLifetime()
	{
		return 200;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 * (damage is computed separately in the onDamage function)
	 */
	public boolean canAttackWithItem()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}

	@Override
	protected void entityInit() {
		this.renderDistanceWeight = 10.0D;
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
	protected float getGravityVelocity()
	{
		return 0;
	}

	@Override
	protected void onImpact(MovingObjectPosition event) {

		switch (event.typeOfHit) {
		case ENTITY:
			if (event.entityHit != null) {
				event.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), (int) 0);
			}
			break;
		case TILE:
			break;
		default:
			break;
		}
		
		if (this.shootingEntity != null && this.shootingEntity instanceof EntityPlayerMP)
        {
            EntityPlayerMP var3 = (EntityPlayerMP)this.shootingEntity;

            if (!var3.playerNetServerHandler.connectionClosed && var3.worldObj == this.worldObj)
            {
                var3.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                var3.fallDistance = 0.0F;
                //this.getThrower().attackEntityFrom(DamageSource.fall, 5);
            }
        }
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			this.setDead();
		}
	}
}