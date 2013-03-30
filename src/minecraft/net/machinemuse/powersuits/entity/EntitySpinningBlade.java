package net.machinemuse.powersuits.entity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.IShearable;

public class EntitySpinningBlade extends EntityThrowable {
	public static final int SIZE = 24;
	public double damage;
	public Entity shootingEntity;
	public ItemStack shootingItem;

	public EntitySpinningBlade(World par1World, EntityLiving shootingEntity) {
		super(par1World, shootingEntity);
		this.shootingEntity = shootingEntity;
		if (shootingEntity instanceof EntityPlayer) {
			this.shootingItem = ((EntityPlayer) shootingEntity).getCurrentEquippedItem();
		}
		this.damage = damage;
		Vec3 direction = shootingEntity.getLookVec().normalize();
		double scale = 1.0;
		this.motionX = direction.xCoord * scale;
		this.motionY = direction.yCoord * scale;
		this.motionZ = direction.zCoord * scale;
		double r = SIZE / 50.0 - direction.yCoord * shootingEntity.getEyeHeight();
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
	}

	@Override
	protected void onImpact(MovingObjectPosition hitMOP) {
		if (hitMOP.typeOfHit == EnumMovingObjectType.TILE) {
			World world = WorldProvider.getProviderForDimension(this.dimension).worldObj;
			int id = world.getBlockId(hitMOP.blockX, hitMOP.blockY, hitMOP.blockZ);
			Block block = Block.blocksList[id];
			if (block instanceof IShearable) {
				IShearable target = (IShearable) block;
				if (target.isShearable(this.shootingItem, world, hitMOP.blockX, hitMOP.blockY, hitMOP.blockZ))
				{
					ArrayList<ItemStack> drops = target.onSheared(this.shootingItem, world, hitMOP.blockX, hitMOP.blockY, hitMOP.blockZ,
							EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, this.shootingItem));
					Random rand = new Random();

					for (ItemStack stack : drops)
					{
						float f = 0.7F;
						double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
						double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
						double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
						EntityItem entityitem = new EntityItem(world, hitMOP.blockX + d, hitMOP.blockY + d1, hitMOP.blockZ + d2, stack);
						entityitem.delayBeforeCanPickup = 10;
						world.spawnEntityInWorld(entityitem);
					}
					if (this.shootingEntity instanceof EntityPlayer) {
						((EntityPlayer) shootingEntity).addStat(StatList.mineBlockStatArray[id], 1);
					}
				}
			} else { // Block hit was not IShearable
				this.kill();
			}
		} else if (hitMOP.typeOfHit == EnumMovingObjectType.ENTITY && hitMOP.entityHit != this.shootingEntity) {
			if (hitMOP.entityHit instanceof IShearable) {
				IShearable target = (IShearable) hitMOP.entityHit;
				Entity entity = hitMOP.entityHit;
				if (target.isShearable(this.shootingItem, entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ))
				{
					ArrayList<ItemStack> drops = target.onSheared(this.shootingItem, entity.worldObj,
							(int) entity.posX, (int) entity.posY, (int) entity.posZ,
							EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, this.shootingItem));

					Random rand = new Random();
					for (ItemStack drop : drops)
					{
						EntityItem ent = entity.entityDropItem(drop, 1.0F);
						ent.motionY += rand.nextFloat() * 0.05F;
						ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
						ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					}
				}
			} else {
				hitMOP.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), (int) damage);
			}
		}
	}
}
