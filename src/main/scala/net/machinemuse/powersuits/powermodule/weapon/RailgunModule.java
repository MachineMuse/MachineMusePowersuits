package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
    public void onRightClick(EntityPlayer player, World world, ItemStack itemStack) {

        double range = 64;
        double timer = MuseItemUtils.getDoubleOrZero(itemStack, TIMER);
        double energyConsumption = ModuleManager.computeModularProperty(itemStack, ENERGY);
        if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption && timer == 0) {
            ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
            MuseItemUtils.setDoubleOrRemove(itemStack, TIMER, 10);
            MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(itemStack, HEAT));
            RayTraceResult hitMOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
            // TODO: actual railgun sound
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            double damage = ModuleManager.computeModularProperty(itemStack, IMPULSE) / 100.0;
            double knockback = damage / 20.0;
            Vec3d lookVec = player.getLookVec();
            if (hitMOP != null) {
                switch (hitMOP.typeOfHit) {
                    case ENTITY:
                        drawParticleStreamTo(player, world, hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        DamageSource damageSource = DamageSource.causePlayerDamage(player);
                        if (hitMOP.entityHit.attackEntityFrom(damageSource, (int) damage)) {
                            hitMOP.entityHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord
                                    * knockback);
                        }
                        break;
                    case BLOCK:
                        drawParticleStreamTo(player, world, hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        break;
                    default:
                        break;
                }
                player.addVelocity(-lookVec.xCoord * knockback, Math.abs(-lookVec.yCoord + 0.2f) * knockback, -lookVec.zCoord * knockback);

                world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            }
        }
        // FIXME
//        player.setItemInUse(itemStack, 10);
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }


    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
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
