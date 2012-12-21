package machinemuse.general.geometry;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import machinemuse.powersuits.gui.MuseGui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Contains a bunch of random OpenGL-related functions, accessed statically.
 * 
 * @author MachineMuse
 * 
 */
public abstract class Doodler {

	/**
	 * Mostly for placeholder graphics, this function draws a 3x3 grid of swirly
	 * circles over a 16x16 square.
	 */
	public static void draw3x3placeholder(boolean a, boolean b, boolean c,
			boolean d,
			boolean e, boolean f, boolean g, boolean h, boolean i) {
		if (a)
			drawCircleAround(3, 3, 2);
		if (b)
			drawCircleAround(8, 3, 2);
		if (c)
			drawCircleAround(13, 3, 2);

		if (d)
			drawCircleAround(3, 8, 2);
		if (e)
			drawCircleAround(8, 8, 2);
		if (f)
			drawCircleAround(13, 8, 2);

		if (g)
			drawCircleAround(3, 13, 2);
		if (h)
			drawCircleAround(8, 13, 2);
		if (i)
			drawCircleAround(13, 13, 2);
	}

	/**
	 * Draws a swirly green circle at the specified coordinates in the current
	 * reference frame.
	 * 
	 * @param xoffset
	 * @param yoffset
	 * @param radius
	 */
	public static void drawCircleAround(float xoffset, float yoffset,
			float radius) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDisable(GL11.GL_LIGHTING);

		int numSegments = 360;
		double theta = (2 * Math.PI) / numSegments;

		int start = (int) (System.currentTimeMillis() / 4 % numSegments);
		double x = radius * Math.sin(theta * start);
		double y = radius * Math.cos(theta * start);
		double tf = Math.tan(theta);
		double rf = Math.cos(theta);
		double tx;
		double ty;
		Colour c = new Colour(0.0f, 1.0f, 0.0f, 0.0f);

		texturelessOn();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < numSegments; i++) {
			GL11.glColor4f(c.r, c.g, c.b, c.a);
			GL11.glVertex2d(x + xoffset, y + yoffset);
			tx = y;
			ty = -x;
			x += tx * tf;
			y += ty * tf;
			x *= rf;
			y *= rf;
			c.r += theta / 7;
			c.b += theta / 7;
			c.a += theta / 2;
		}
		GL11.glEnd();

		texturelessOff();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_LIGHTING);

	}

	/**
	 * Draws a swirly green circle at the specified coordinates in the current
	 * reference frame.
	 * 
	 * @param xoffset
	 * @param yoffset
	 * @param radius
	 */
	public static void drawTriangles(float[] v, float[] c,
			int[] i) {
		arraysOn();
		texturelessOn();
		on2D();

		// float subdivisions = 5f;
		// float radius = 0.5f;

		// GL11.glPushMatrix();
		// GL11.glTranslatef(-radius, -radius, 0);
		// for (int i1 = 0; i1 <= subdivisions * 2; i1++) {
		// for (int i2 = 0; i2 <= subdivisions * 2; i2++) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(v.length);
		vertices.put(v);
		vertices.flip();

		FloatBuffer colours = BufferUtils.createFloatBuffer(c.length);
		colours.put(c);
		colours.flip();

		IntBuffer indices = BufferUtils.createIntBuffer(i.length);
		indices.put(i);
		indices.flip();

		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		GL11.glVertexPointer(3, 0, vertices);
		GL11.glColorPointer(4, 0, colours);

		GL11.glDrawElements(GL11.GL_TRIANGLES, indices);

		// GL11.glTranslatef(0, radius / subdivisions, 0);
		// }
		// GL11.glTranslatef(radius / subdivisions, -radius * 2, 0);
		// }
		// GL11.glPopMatrix();

		off2D();
		texturelessOff();
		arraysOff();

	}

	/**
	 * 2D rendering mode on/off
	 */

	public static void on2D() {

		// GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		// attempt at fake antialiasing
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
		// GL11.glColorMask(false, false, false, true);
		// GL11.glClearColor(0, 0, 0, 0);
		// GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// GL11.glColorMask(true, true, true, true);

		// GL11.glHint(GL11.GL_POINT_SMOOTH, GL11.GL_NICEST);
		// GL11.glHint(GL11.GL_LINE_SMOOTH, GL11.GL_NICEST);
		// GL11.glHint(GL11.GL_POLYGON_SMOOTH, GL11.GL_NICEST);
		// GL11.glDepthFunc(GL11.GL_GREATER);
	}

	public static void off2D() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	/**
	 * Arrays on/off
	 */

	public static void arraysOn() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		// GL11.glEnableClientState(GL11.GL_INDEX_ARRAY);
	}

	public static void arraysOff() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		// GL11.glDisableClientState(GL11.GL_INDEX_ARRAY);

	}

	/**
	 * Call before doing any pure geometry (ie. with colours rather than
	 * textures).
	 */
	public static void texturelessOn() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// GL11.glDisable(GL11.GL_ALPHA_TEST);

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Call after doing pure geometry (ie. with colours) to go back to the
	 * texture mode (default).
	 */
	public static void texturelessOff() {
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(int left, int top, int right,
			int bottom, Colour c1, Colour c2, double zLevel)
	{
		texturelessOn();
		on2D();

		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(c1.r, c1.g, c1.b, c1.a);
		tessellator.addVertex((double) right, (double) top,
				zLevel);
		tessellator
				.addVertex((double) left, (double) top, zLevel);

		tessellator.setColorRGBA_F(c2.r, c2.g, c2.b, c2.a);
		tessellator.addVertex((double) left, (double) bottom,
				zLevel);
		tessellator.addVertex((double) right, (double) bottom,
				zLevel);
		tessellator.draw();

		off2D();
		texturelessOff();
	}

	public static void drawItemAt(int x, int y, MuseGui gui, ItemStack item) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDisable(GL11.GL_LIGHTING);

		gui.getRenderItem().zLevel = 100.0F;
		gui.getRenderItem().renderItemAndEffectIntoGUI(
				gui.getFontRenderer(), gui.getRenderEngine(), item, x, y);
		gui.getRenderItem().zLevel = 0.0F;

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_LIGHTING);

	}
}
