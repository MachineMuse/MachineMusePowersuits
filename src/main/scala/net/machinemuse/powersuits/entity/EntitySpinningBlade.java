package net.machinemuse.powersuits.entity;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.powermodule.weapon.BladeLauncherModule;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntitySpinningBlade extends EntityThrowable {
    public static final int SIZE = 24;
    public double damage;
    public Entity shootingEntity;
    public ItemStack shootingItem;

    public EntitySpinningBlade(World world) {
        super(world);
    }

    public EntitySpinningBlade(World par1World, EntityLivingBase shootingEntity) {
        super(par1World, shootingEntity);
        this.shootingEntity = shootingEntity;
        if (shootingEntity instanceof EntityPlayer) {
            this.shootingItem = ((EntityPlayer) shootingEntity).getHeldItemMainhand();
            if (this.shootingItem != null) {
                this.damage = ModuleManager.computeModularProperty(shootingItem, BladeLauncherModule.BLADE_DAMAGE);
            }
        }
        Vec3d direction = shootingEntity.getLookVec().normalize();
        double speed = 1.0;
        double scale = 1;
        this.motionX = direction.xCoord * speed;
        this.motionY = direction.yCoord * speed;
        this.motionZ = direction.zCoord * speed;
        double r = 1;
        double xoffset = 1.3f + r - direction.yCoord * shootingEntity.getEyeHeight();
        double yoffset = -.2;
        double zoffset = 0.3f;
        double horzScale = Math.sqrt(direction.xCoord * direction.xCoord + direction.zCoord * direction.zCoord);
        double horzx = direction.xCoord / horzScale;
        double horzz = direction.zCoord / horzScale;
        this.posX = shootingEntity.posX + direction.xCoord * xoffset - direction.yCoord * horzx * yoffset - horzz * zoffset;
        this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.yCoord * xoffset + (1 - Math.abs(direction.yCoord)) * yoffset;
        this.posZ = shootingEntity.posZ + direction.zCoord * xoffset - direction.yCoord * horzz * yoffset + horzx * zoffset;
        this.setEntityBoundingBox(new AxisAlignedBB(posX - r, posY - r, posZ - r, posX + r, posY + r, posZ + r));
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    public int getMaxLifetime() {
        return 200;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.ticksExisted > this.getMaxLifetime()) {
            this.setDead();
        }
    }

    @Override
    protected void onImpact(RayTraceResult hitMOP) {
        if (hitMOP.typeOfHit == RayTraceResult.Type.BLOCK) {
            World world = this.worldObj;
            if (world == null) {
                return;
            }
            Block block = world.getBlockState(new BlockPos(hitMOP.getBlockPos())).getBlock();
            if (block instanceof IShearable) {
                IShearable target = (IShearable) block;
                if (target.isShearable(this.shootingItem, world, hitMOP.getBlockPos()) && !world.isRemote) {
                    List<ItemStack> drops = target.onSheared(this.shootingItem, world, hitMOP.getBlockPos(),
                            EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), this.shootingItem));
                    Random rand = new Random();

                    for (ItemStack stack : drops) {
                        float f = 0.7F;
                        double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        BlockPos entityPos = hitMOP.getBlockPos().add(d, d1, d2);
                        EntityItem entityitem = new EntityItem(world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), stack);
                        entityitem.setDefaultPickupDelay();
                        world.spawnEntityInWorld(entityitem);
                    }
                    if (this.shootingEntity instanceof EntityPlayer) {
                        ((EntityPlayer) shootingEntity).addStat(StatList.getBlockStats(block), 1);
                    }
                }
                world.destroyBlock(hitMOP.getBlockPos(), true);// Destroy block and drop item
            } else { // Block hit was not IShearable
                this.kill();
            }
        } else if (hitMOP.typeOfHit == RayTraceResult.Type.ENTITY && hitMOP.entityHit != this.shootingEntity) {
            if (hitMOP.entityHit instanceof IShearable) {
                IShearable target = (IShearable) hitMOP.entityHit;
                Entity entity = hitMOP.entityHit;
                if (target.isShearable(this.shootingItem, entity.worldObj, entity.getPosition())) {
                    ArrayList<ItemStack> drops = (ArrayList<ItemStack>) target.onSheared(this.shootingItem, entity.worldObj, entity.getPosition(),
                            EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), this.shootingItem));

                    Random rand = new Random();
                    for (ItemStack drop : drops) {
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
