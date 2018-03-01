package net.machinemuse.numina.math;

import java.util.Random;

/**
 * Ported to Java by lehjr on 10/22/16.
 */
public final class MuseMathUtils {
    private static Random random = new Random();

    public static Random random() {
        return random;
    }

    public static double nextDouble() {
        return random().nextDouble();
    }

    public static double clampDouble(double value, double min, double max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static double sumsq(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    public static double pythag(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }
}