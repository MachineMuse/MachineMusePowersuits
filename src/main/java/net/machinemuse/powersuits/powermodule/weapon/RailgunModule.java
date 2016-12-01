package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
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

import java.util.List;

public class RailgunModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    // private List<String> particles;
    // private Iterator<String> iterator;
    public static final String MODULE_RAILGUN = "Railgun";
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

    @Override
    public String getDescription() {
        return "An assembly which accelerates a projectile to supersonic speeds using magnetic force. Heavy recoil.";
    }

    public void drawParticleStreamTo(EntityPlayer source, World world, double x, double y, double z) {
        Vec3d direction = source.getLookVec().normalize();
        double scale = 1.0;
        double xoffset = 1.3f;
        double yoffset = -.2;
        double zoffset = 0.3f;
        Vec3d horzdir = direction.normalize();
        horzdir = new Vec3d(horzdir.xCoord, 0, horzdir.zCoord);
        horzdir = horzdir.normalize();
        double cx = source.posX + direction.xCoord * xoffset - direction.yCoord * horzdir.xCoord * yoffset - horzdir.zCoord * zoffset;
        double cy = source.posY + source.getEyeHeight() + direction.yCoord * xoffset + (1 - Math.abs(direction.yCoord)) * yoffset;
        double cz = source.posZ + direction.zCoord * xoffset - direction.yCoord * horzdir.zCoord * yoffset + horzdir.xCoord * zoffset;
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        double range = 64;
        double timer = MuseItemUtils.getDoubleOrZero(itemStackIn, TIMER);
        double energyConsumption = ModuleManager.computeModularProperty(itemStackIn, ENERGY);
        if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption && timer == 0) {
            ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
            MuseItemUtils.setDoubleOrRemove(itemStackIn, TIMER, 10);
            MuseHeatUtils.heatPlayer(playerIn, ModuleManager.computeModularProperty(itemStackIn, HEAT));
            RayTraceResult hitMOP = MusePlayerUtils.doCustomRayTrace(playerIn.worldObj, playerIn, true, range);
            // TODO: actual railgun sound
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            double damage = ModuleManager.computeModularProperty(itemStackIn, IMPULSE) / 100.0;
            double knockback = damage / 20.0;
            Vec3d lookVec = playerIn.getLookVec();
            if (hitMOP != null) {
                switch (hitMOP.typeOfHit) {
                    case ENTITY:
                        drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        DamageSource damageSource = DamageSource.causePlayerDamage(playerIn);
                        if (hitMOP.entityHit.attackEntityFrom(damageSource, (int) damage)) {
                            hitMOP.entityHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord
                                    * knockback);
                        }
                        break;
                    case BLOCK:
                        drawParticleStreamTo(playerIn, worldIn, hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        break;
                    default:
                        break;
                }
                playerIn.addVelocity(-lookVec.xCoord * knockback, Math.abs(-lookVec.yCoord + 0.2f) * knockback, -lookVec.zCoord * knockback);

                worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            }
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
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

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.railgun;
    }
}