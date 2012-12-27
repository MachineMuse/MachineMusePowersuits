package net.machinemuse.powersuits.item;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

public class TinkerEffect {
	public static Random random = new Random();
	public String propertyAffected;
	public double minimumEffect;
	public double maximumEffect;

	public TinkerEffect(String propertyAffected, double minimumEffect,
			double maximumEffect) {
		super();
		this.propertyAffected = propertyAffected;
		this.minimumEffect = minimumEffect;
		this.maximumEffect = maximumEffect;
	}

	public void applyEffect(NBTTagCompound properties) {
		double prev = ItemUtils.getDoubleOrZero(properties, propertyAffected);
		double effect = (maximumEffect - minimumEffect) * random.nextDouble()
				+ minimumEffect;
		properties.setDouble(propertyAffected, Math.max(0, prev + effect));
	}
}
