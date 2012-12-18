package machinemuse.powersuits.gui;

import java.util.ArrayList;

import machinemuse.general.geometry.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class MuseGui extends GuiScreen {
	protected static RenderItem itemRenderer = new RenderItem();
	private final boolean usePretty = true;
	private static final int numSegments = 360;
	private static final int xcenter = 8;
	private static final int ycenter = 8;
	private static final Tessellator tesselator = Tessellator.instance;
	private long creationTime;
	int xSize, ySize;

	public static final double theta = (2 * Math.PI) / numSegments;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		creationTime = System.currentTimeMillis();
	}

	public void drawRectangularBackground() {
		int xpadding = (width - xSize) / 2;
		int ypadding = (height - ySize) / 2;

		drawGradientRect(
				xpadding, ypadding,
				xpadding + xSize, ypadding + ySize,
				Colour.getGreyscale(0.8f, 0.8f),
				Colour.getGreyscale(0.3f, 0.8f));

		// GL11.glEnable(GL11.GL_BLEND);
		// GL11.glEnable(GL11.GL_LINE_SMOOTH);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GL11.glBegin(GL11.GL_QUADS);
		// GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.8f);
		// GL11.glVertex2d(0, 0);
		// GL11.glVertex2d(width - 10, 0);
		// GL11.glVertex2d(width - 10, height - 10);
		// GL11.glVertex2d(0, height - 10);
		// GL11.glEnd();
	}

	public void drawItemsOnVerticalLine(ArrayList<ItemStack> items,
			float xoffset,
			float yoffset,
			float lineheight, RenderEngine engine) {
		if (items.size() < 1) {
			return;
		} else if (items.size() < 2) {
			drawItemAt(xoffset, yoffset, engine, items.get(0));
		} else {
			float step = 2 * lineheight / (items.size() - 1);
			for (int i = 0; i < items.size(); i++) {
				drawItemAt(xoffset, yoffset - lineheight + i * step, engine,
						items.get(i));
			}
		}
	}

	/**
	 * Returns absolute screen coordinates (0 to width) from a relative
	 * coordinate (-1.0F to +1.0F)
	 * 
	 * @param relx
	 *            Relative X coordinate
	 * @return Absolute X coordinate
	 */
	public int absX(float relx) {
		int absx = (int) ((relx + 1) * xSize / 2);
		int xpadding = (width - xSize) / 2;
		return absx + xpadding;
	}

	/**
	 * Returns absolute screen coordinates (0 to width) from a relative
	 * coordinate (-1.0F to +1.0F)
	 * 
	 * @param relx
	 *            Relative Y coordinate
	 * @return Absolute Y coordinate
	 */
	public int absY(float rely) {
		int absy = (int) ((rely + 1) * ySize / 2);
		int ypadding = (height - ySize) / 2;
		return absy + ypadding;
	}

	/**
	 * Draws the specified itemstack at the *relative* coordinates x,y. Used
	 * mainly in clickables.
	 */
	public void drawItemAt(float x, float y, RenderEngine engine,
			ItemStack item) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDisable(GL11.GL_LIGHTING);

		itemRenderer.zLevel = 100.0F;
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, engine,
				item, absX(x) - xcenter, absY(y) - ycenter);
		Minecraft.getMinecraft().fontRenderer.drawString(
				item.getItem().getItemDisplayName(item).substring(12, 16),
				absX(x) - xcenter, absY(y) - ycenter,
				Colour.getGreyscale(1.0F, 1.0F).getInt());
		// drawCircleAround(absX(x), absY(y), 8);
		itemRenderer.zLevel = 0.0F;
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	public static void drawCircleAround(float xoffset, float yoffset,
			float radius) {
		int start = (int) (System.currentTimeMillis() / 4 % 360);
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
	}

	/**
	 * Call before doing any pure geometry (ie. with colours rather than
	 * textures).
	 */
	public static void texturelessOn() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
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
	protected void drawGradientRect(int left, int top, int right, int bottom,
			Colour c1, Colour c2)
	{
		texturelessOn();

		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(c1.r, c1.g, c1.b, c1.a);
		tessellator.addVertex((double) right, (double) top,
				(double) this.zLevel);
		tessellator
				.addVertex((double) left, (double) top, (double) this.zLevel);

		tessellator.setColorRGBA_F(c2.r, c2.g, c2.b, c2.a);
		tessellator.addVertex((double) left, (double) bottom,
				(double) this.zLevel);
		tessellator.addVertex((double) right, (double) bottom,
				(double) this.zLevel);
		tessellator.draw();

		texturelessOff();
	}
}
