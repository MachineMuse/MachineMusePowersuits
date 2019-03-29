package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class RailgunModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    public RailgunModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 6));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addBasePropertyDouble(MPSModuleConstants.RAILGUN_TOTAL_IMPULSE, 500, "Ns");
        addBasePropertyDouble(MPSModuleConstants.RAILGUN_ENERGY_COST, 5000, "RF");
        addBasePropertyDouble(MPSModuleConstants.RAILGUN_HEAT_EMISSION, 2, "");
        addTradeoffPropertyDouble(MPSModuleConstants.VOLTAGE, MPSModuleConstants.RAILGUN_TOTAL_IMPULSE, 2500);
        addTradeoffPropertyDouble(MPSModuleConstants.VOLTAGE, MPSModuleConstants.RAILGUN_ENERGY_COST, 25000);
        addTradeoffPropertyDouble(MPSModuleConstants.VOLTAGE, MPSModuleConstants.RAILGUN_HEAT_EMISSION, 10);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_RAILGUN__DATANAME;
    }

    public void drawParticleStreamTo(EntityPlayer source, World world, double x, double y, double z) {
        Vec3d direction = source.getLookVec().normalize();
        double xoffset = 1.3f;
        double yoffset = -.2;
        double zoffset = 0.3f;
        Vec3d horzdir = direction.normalize();
        horzdir = new Vec3d(horzdir.x, 0, horzdir.z);
        horzdir = horzdir.normalize();
        double cx = source.posX + direction.x * xoffset - direction.y * horzdir.x * yoffset - horzdir.z * zoffset;
        double cy = source.posY + source.getEyeHeight() + direction.y * xoffset + (1 - Math.abs(direction.y)) * yoffset;
        double cz = source.posZ + direction.z * xoffset - direction.y * horzdir.z * yoffset + horzdir.x * zoffset;
        double dx = x - cx;
        double dy = y - cy;
        double dz = z - cz;
        double ratio = Math.sqrt(dx * dx + dy * dy + dz * dz);

        while (Math.abs(cx - x) > Math.abs(dx / ratio)) {
            world.spawnParticle(EnumParticleTypes.TOWN_AURA, cx, cy, cz, 0.0D, 0.0D, 0.0D);
            cx += dx * 0.1 / ratio;
            cy += dy * 0.1 / ratio;
            cz += dz * 0.1 / ratio;
        }
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            double range = 64;
            double timer = MuseItemUtils.getDoubleOrZero(itemStackIn, MPSModuleConstants.TIMER);
            double energyConsumption = getEnergyUsage(itemStackIn);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption && timer == 0) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, (int) energyConsumption);
                MuseItemUtils.setDoubleOrRemove(itemStackIn, MPSModuleConstants.TIMER, 10);
                MuseHeatUtils.heatPlayer(playerIn, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.RAILGUN_HEAT_EMISSION));
                RayTraceResult hitMOP = MusePlayerUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);
                // TODO: actual railgun sound
                worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                double damage = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.RAILGUN_TOTAL_IMPULSE) / 100.0;
                double knockback = damage / 20.0;
                Vec3d lookVec = playerIn.getLookVec();
                if (hitMOP != null) {
                    switch (hitMOP.typeOfHit) {
                        case ENTITY:
                            drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.x, hitMOP.hitVec.y, hitMOP.hitVec.z);
                            DamageSource damageSource = DamageSource.causePlayerDamage(playerIn);
                            if (hitMOP.entityHit.attackEntityFrom(damageSource, (int) damage)) {
                                hitMOP.entityHit.addVelocity(lookVec.x * knockback, Math.abs(lookVec.y + 0.2f) * knockback, lookVec.z * knockback);
                            }
                            break;
                        case BLOCK:
                            drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.x, hitMOP.hitVec.y, hitMOP.hitVec.z);
                            break;
                        default:
                            break;
                    }
                    playerIn.addVelocity(-lookVec.x * knockback, Math.abs(-lookVec.y + 0.2f) * knockback, -lookVec.z * knockback);

//                    worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                    // FIxme: testing different sound
                    worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                }
                playerIn.setActiveHand(hand);
                return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
            }
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.RAILGUN_ENERGY_COST);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack stack) {
        double timer = MuseItemUtils.getDoubleOrZero(stack, MPSModuleConstants.TIMER);
        if (timer > 0) MuseItemUtils.setDoubleOrRemove(stack, MPSModuleConstants.TIMER, timer - 1 > 0 ? timer - 1 : 0);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.railgun;
    }
}