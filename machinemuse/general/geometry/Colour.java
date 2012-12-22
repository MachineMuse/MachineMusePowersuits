package machinemuse.general.geometry;

/**
 * A class representing an RGBA colour and various helper functions. Mainly to
 * improve readability elsewhere.
 * 
 * @author MachineMuse
 */
public class Colour {
	/**
	 * The RGBA values are stored as floats from 0.0F (nothing) to 1.0F (full
	 * saturation/opacity)
	 */
	public float r, g, b, a;

	/**
	 * Constructor. Just sets the RGBA values to the parameters.
	 */
	public Colour(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Secondary constructor. Sets RGB accordingly and sets alpha to 1.0F (full
	 * opacity)
	 */
	public Colour(float r, float g, float b) {
		this(r, g, b, 1.0F);
	}

	/**
	 * Takes colours in the integer format that Minecraft uses, and converts.
	 */
	public Colour(int c) {
		this.a = (c >> 24 & 255) / 255.0F;
		this.r = (c >> 16 & 255) / 255.0F;
		this.g = (c >> 8 & 255) / 255.0F;
		this.b = (c & 255) / 255.0F;
	}

	/**
	 * Returns this colour as an int in Minecraft's format (I think)
	 * 
	 * @return int value of this colour
	 */
	public int getInt() {
		int val = 0;
		val = val | ((int) (a * 255) << 24);
		val = val | ((int) (r * 255) << 16);
		val = val | ((int) (g * 255) << 8);
		val = val | ((int) (b * 255));

		return val;
	}

	/**
	 * Returns a colour with RGB set to the same value ie. a shade of grey.
	 */

	public static Colour getGreyscale(float value, float alpha) {
		return new Colour(value, value, value, alpha);
	}

	/**
	 * Returns a colour at interval interval along a linear gradient from this
	 * to target
	 */

	public Colour interpolate(Colour target, float interval) {
		float complement = 1 - interval;
		return new Colour(
				this.r * complement + target.r * interval,
				this.g * complement + target.g * interval,
				this.b * complement + target.b * interval,
				this.a * complement + target.a * interval);
	}
}