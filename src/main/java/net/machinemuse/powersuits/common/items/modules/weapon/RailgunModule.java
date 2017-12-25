package net.machinemuse.powersuits.common.items.modules.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.*;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_RAILGUN;

public class RailgunModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    // private List<String> particles;
    // private Iterator<String> iterator;

    public static final String IMPULSE = "Railgun Total Impulse";
    public static final String ENERGY = "Railgun Energy Cost";
    public static final String HEAT = "Railgun Heat Emission";
    public static final String TIMER = "cooldown";

    public RailgunModule(List<IModularItem> validItems) {
        super(validItems);
        // particles = Arrays.asList("smoke", "snowballpoof", "portal",
        // "splash", "bubble", "townaura",
        // "hugeexplosion", "flame", "heart", "crit", "magicCrit", "note",
        // "enchantmenttable", "lava", "footstep", "reddust", "dripWater",
        // "dripLava", "slime");
        // iterator = particles.iterator();
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 6));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addBaseProperty(IMPULSE, 500, "Ns");
        addBaseProperty(ENERGY, 500, "J");
        addBaseProperty(HEAT, 2, "");
        addTradeoffProperty("Voltage", IMPULSE, 2500);
        addTradeoffProperty("Voltage", ENERGY, 2500);
        addTradeoffProperty("Voltage", HEAT, 10);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_RAILGUN;
    }

    @Override
    public String getUnlocalizedName() {
        return "railgun";
    }

    public void drawParticleStreamTo(EntityPlayer source, World world, double x, double y, double z) {
        Vec3d direction = source.getLookVec().normalize();
        double scale = 1.0;
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
            double timer = MuseItemUtils.getDoubleOrZero(itemStackIn, TIMER);
            double energyConsumption = ModuleManager.computeModularProperty(itemStackIn, ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption && timer == 0) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
                MuseItemUtils.setDoubleOrRemove(itemStackIn, TIMER, 10);
                MuseHeatUtils.heatPlayer(playerIn, ModuleManager.computeModularProperty(itemStackIn, HEAT));
                RayTraceResult hitMOP = MusePlayerUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);
                // TODO: actual railgun sound
                worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                double damage = ModuleManager.computeModularProperty(itemStackIn, IMPULSE) / 100.0;
                double knockback = damage / 20.0;
                Vec3d lookVec = playerIn.getLookVec();
                if (hitMOP != null) {
                    switch (hitMOP.typeOfHit) {
                        case ENTITY:
                            drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.x, hitMOP.hitVec.y, hitMOP.hitVec.z);
                            DamageSource damageSource = DamageSource.causePlayerDamage(playerIn);
                            if (hitMOP.entityHit.attackEntityFrom(damageSource, (int) damage)) {
                                hitMOP.entityHit.addVelocity(lookVec.x * knockback, Math.abs(lookVec.y + 0.2f) * knockback, lookVec.z
                                        * knockback);
                            }
                            break;
                        case BLOCK:
                            drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.x, hitMOP.hitVec.y, hitMOP.hitVec.z);
                            break;
                        default:
                            break;
                    }
                    playerIn.addVelocity(-lookVec.x * knockback, Math.abs(-lookVec.y + 0.2f) * knockback, -lookVec.z * knockback);

                    worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
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
    public void onPlayerTickActive(EntityPlayer player, ItemStack stack) {
        double timer = MuseItemUtils.getDoubleOrZero(stack, TIMER);
        if (timer > 0) MuseItemUtils.setDoubleOrRemove(stack, TIMER, timer - 1 > 0 ? timer - 1 : 0);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.railgun;
    }
}
