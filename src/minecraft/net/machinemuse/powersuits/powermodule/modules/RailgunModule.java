package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.MusePlayerUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RailgunModule extends PowerModuleBase implements IRightClickModule {
	// private List<String> particles;
	// private Iterator<String> iterator;
	private static String IMPULSE = "Railgun Total Impulse";
	private static String ENERGY = "Railgun Energy Cost";

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
		addTradeoffProperty("Voltage", IMPULSE, 2500);
		addTradeoffProperty("Voltage", ENERGY, 2500);
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.CLAW_LASER;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_WEAPON;
	}

	@Override
	public String getName() {
		return "Railgun";
	}

	@Override
	public String getDescription() {
		return "An assembly which accelerates a projectile to supersonic speeds using magnetic force. Heavy recoil.";
	}

	public void drawParticleStreamTo(EntityPlayer source, World world, double x, double y, double z) {
		Vec3 direction = source.getLookVec().normalize();
		double scale = 1.0;
		double xoffset = 1.3f;
		double yoffset = -.2;
		double zoffset = 0.3f;
		Vec3 horzdir = direction.normalize();
		horzdir.yCoord = 0;
		horzdir = horzdir.normalize();
		double cx = source.posX + direction.xCoord * xoffset - direction.yCoord * horzdir.xCoord * yoffset - horzdir.zCoord * zoffset;
		double cy = source.posY + source.getEyeHeight() + direction.yCoord * xoffset + (1 - Math.abs(direction.yCoord)) * yoffset;
		double cz = source.posZ + direction.zCoord * xoffset - direction.yCoord * horzdir.zCoord * yoffset + horzdir.xCoord * zoffset;
		double dx = x - cx;
		double dy = y - cy;
		double dz = z - cz;
		double ratio = Math.sqrt(dx * dx + dy * dy + dz * dz);

		while (Math.abs(cx - x) > Math.abs(dx / ratio)) {
			world.spawnParticle("townaura", cx, cy, cz, 0.0D, 0.0D, 0.0D);
			cx += dx * 0.1 / ratio;
			cy += dy * 0.1 / ratio;
			cz += dz * 0.1 / ratio;
		}
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack itemStack) {

		double range = 64;
		double energyConsumption = ModuleManager.computeModularProperty(itemStack, ENERGY);
		if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
			ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
			MovingObjectPosition hitMOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));

			double damage = ModuleManager.computeModularProperty(itemStack, IMPULSE) / 100.0;
			double knockback = damage / 20.0;
			Vec3 lookVec = player.getLookVec();
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
				case TILE:
					drawParticleStreamTo(player, world, hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
					break;
				default:
					break;
				}
				player.addVelocity(-lookVec.xCoord * knockback, Math.abs(-lookVec.yCoord + 0.2f) * knockback, -lookVec.zCoord * knockback);

				world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
			}
		}
	}

}
