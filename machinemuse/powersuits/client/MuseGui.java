package machinemuse.powersuits.client;

import java.util.List;

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

	public static final double theta = (2 * Math.PI) / numSegments;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
	}

	public void drawRectangularBackground() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.8f);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(width - 10, 0);
		GL11.glVertex2d(width - 10, height - 10);
		GL11.glVertex2d(0, height - 10);
		GL11.glEnd();
	}

	public void drawItemsOnVerticalLine(List<ItemStack> items, Point2Df offset,
			float lineheight, RenderEngine engine) {
		if (items.size() < 1) {
			return;
		} else if (items.size() < 2) {
			drawItemAt(offset, engine, items.get(0));
		} else {
			int top = (int) (offset.getAbsY(height) - height * lineheight / 2);
			int bottom = (int) (offset.getAbsY(height) + height * lineheight
					/ 2);
			Point2Df position = new Point2Df(offset.x, top * 2.0f / height - 1);
			int step = (top - bottom) / (items.size() - 1);
			for (int i = 0; i < items.size(); i++) {
				drawItemAt(position, engine, items.get(i));
				position.y += step;
			}
		}
	}

	public void drawItemAt(Point2Df p, RenderEngine engine,
			ItemStack item) {
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, engine,
				item, p.getAbsX(width) - xcenter, p.getAbsY(height) - ycenter);
	}

	public void drawCircleAround(float xoffset, float yoffset, float radius) {
		int start = (int) (System.currentTimeMillis() / 4 % 360);
		double x = radius * Math.sin(theta * start);
		double y = radius * Math.cos(theta * start);
		double tf = Math.tan(theta);
		double rf = Math.cos(theta);
		double tx;
		double ty;
		Colour c = new Colour(0.0f, 1.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
	}

	public static class Colour {
		public float r;
		public float g;
		public float b;
		public float a;

		public Colour(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}

		public Colour(float r, float g, float b) {
			this(r, g, b, 1.0F);
		}

		public Colour(int c) {
			this.r = (c >> 16 & 255) / 255.0F;
			this.g = (c >> 8 & 255) / 255.0F;
			this.b = (c & 255) / 255.0F;
			this.a = 1.0F;
		}
	}

	public static class Point2Df {
		/*
		 * point representation where [-1,-1] is the top left corner of the
		 * screen and [1,1] is the bottom right.
		 */
		public float x;
		public float y;

		public Point2Df(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public int getAbsX(int width) {
			return (int) ((x + 1) * width / 2);
		}

		public int getAbsY(int height) {
			return (int) ((y + 1) * height / 2);
		}

	}
}
