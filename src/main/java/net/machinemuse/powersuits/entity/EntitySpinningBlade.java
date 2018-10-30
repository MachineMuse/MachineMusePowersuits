package net.machinemuse.powersuits.entity;

import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.List;
import java.util.Random;

public class EntitySpinningBlade extends EntityThrowable {
    public static final int SIZE = 24;
    public double damage;
    public Entity shootingEntity;
    public ItemStack shootingItem = ItemStack.EMPTY;

    public EntitySpinningBlade(World world) {
        super(world);
    }

    public EntitySpinningBlade(World par1World, EntityLivingBase shootingEntity) {
        super(par1World, shootingEntity);
        this.shootingEntity = shootingEntity;
        if (shootingEntity instanceof EntityPlayer) {
            this.shootingItem = ((EntityPlayer) shootingEntity).inventory.getCurrentItem();
            if (!this.shootingItem.isEmpty()) {
                this.damage = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(shootingItem, MPSModuleConstants.BLADE_DAMAGE);
            }
        }
        Vec3d direction = shootingEntity.getLookVec().normalize();
        double speed = 1.0;
        double scale = 1;
        this.motionX = direction.x * speed;
        this.motionY = direction.y * speed;
        this.motionZ = direction.z * speed;
        double r = 1;
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
    protected void onImpact(RayTraceResult hitResult) {
        if (hitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            World world = this.world;
            if (world == null) {
                return;
            }
            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
            if (block instanceof IShearable) {
                IShearable target = (IShearable) block;
                if (target.isShearable(this.shootingItem, world, hitResult.getBlockPos()) && !world.isRemote) {
                    List<ItemStack> drops = target.onSheared(this.shootingItem, world, hitResult.getBlockPos(),
                            EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), this.shootingItem));
                    Random rand = new Random();

                    for (ItemStack stack : drops) {
                        float f = 0.7F;
                        double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        EntityItem entityitem = new EntityItem(world, hitResult.getBlockPos().getX() + d, hitResult.getBlockPos().getY() + d1, hitResult.getBlockPos().getZ() + d2, stack);
                        entityitem.setPickupDelay(10);
                        world.spawnEntity(entityitem);
                    }
                    if (this.shootingEntity instanceof EntityPlayer) {
                        ((EntityPlayer) shootingEntity).addStat(StatList.getBlockStats(block), 1);
                    }
                }
                world.destroyBlock(hitResult.getBlockPos(), true);// Destroy block and drop item
            } else { // Block hit was not IShearable
                this.setDead();
            }
        } else if (hitResult.typeOfHit == RayTraceResult.Type.ENTITY && hitResult.entityHit != this.shootingEntity) {
            if (hitResult.entityHit instanceof IShearable) {
                IShearable target = (IShearable) hitResult.entityHit;
                Entity entity = hitResult.entityHit;
                if (target.isShearable(this.shootingItem, entity.world, entity.getPosition())) {
                    List<ItemStack> drops = target.onSheared(this.shootingItem, entity.world,
                            entity.getPosition(),
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
                hitResult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), (int) damage);
            }
        }
    }
}