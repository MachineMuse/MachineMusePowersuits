package net.machinemuse.general;

import java.util.Random;

public abstract class MuseMathUtils {
	protected static Random random;

	public static Random getRandom() {
		if (random == null) {
			random = new Random();
		}
		return random;
	}

	public static double nextDouble() {
		return getRandom().nextDouble();
	}
}
