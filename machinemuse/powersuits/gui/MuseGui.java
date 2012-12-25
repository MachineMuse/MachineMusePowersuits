package machinemuse.powersuits.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import machinemuse.general.geometry.Colour;
import machinemuse.general.geometry.MuseRenderer;
import machinemuse.general.geometry.FlyFromMiddlePoint2D;
import machinemuse.general.geometry.Point2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * I got fed up with Minecraft's gui system so I wrote my own (to some extent.
 * Still based on GuiScreen). This class contains a variety of helper functions
 * to draw geometry and various other prettifications. Note that MuseGui is
 * heavily geometry-based as opposed to texture-based.
 * 
 * @author MachineMuse
 * 
 */
public class MuseGui extends GuiScreen {
	protected static RenderItem renderItem;
	protected final boolean usePretty = true;
	protected static final Tessellator tesselator = Tessellator.instance;
	protected Point2D ul, br;
	protected long creationTime;
	protected int xSize;
	protected int ySize;

	public MuseGui() {
		super();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		creationTime = System.currentTimeMillis();

		int xpadding = (width - getxSize()) / 2;
		int ypadding = (height - ySize) / 2;
		ul = new FlyFromMiddlePoint2D(-1, -1, 150);
		br = new FlyFromMiddlePoint2D(1, 1, 150);
	}

	/**
	 * Draws the gradient-rectangle background you see in the TinkerTable gui.
	 */
	public void drawRectangularBackground() {
		MuseRenderer.drawGradientRect(
				absX(ul.x()), absY(ul.y()),
				absX(br.x()), absY(br.y()),
				new Colour(0.1F, 0.9F, 0.1F, 0.8F),
				new Colour(0.0F, 0.2F, 0.0F, 0.8F),
				(double) this.zLevel);
	}

	/**
	 * Draws all clickables in a list!
	 */
	public void drawClickables(List<? extends Clickable> list) {
		if (list == null) {
			return;
		}
		Iterator<? extends Clickable> iter = list.iterator();
		Clickable clickie;
		while (iter.hasNext())
		{
			clickie = iter.next();
			clickie.draw(Minecraft.getMinecraft().renderEngine, this);
		}
	}

	/**
	 * Returns the first ID in the list that is hit by a click
	 * 
	 * @return
	 */
	public int hitboxClickables(int x, int y,
			List<? extends Clickable> list) {
		if (list == null) {
			return -1;
		}
		Clickable clickie;
		for (int i = 0; i < list.size(); i++)
		{
			clickie = list.get(i);
			if (clickie.hitBox(x, y, this)) {
				// MuseLogger.logDebug("Hit!");
				return i;
			}
		}
		return -1;
	}

	/**
	 * Creates a list of points linearly interpolated between points a and b
	 * inclusive.
	 * 
	 * @return A list of num points
	 */
	public List<Point2D> pointsInLine(int num, Point2D a, Point2D b) {
		List<Point2D> points = new ArrayList<Point2D>();
		if (num < 1) {
			return null;
		} else if (num < 2) {
			points.add(b.minus(a).times(0.5F).plus(a));
		} else {
			Point2D step = b.minus(a).times(1.0F / (num + 1));
			for (int i = 1; i < num + 2; i++) {
				points.add(a.plus(step.times(i)));
			}
		}

		return points;
	}

	/**
	 * Whether or not this gui pauses the game in single player.
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Singleton pattern for the RenderItem
	 * 
	 * @return the static renderItem instance
	 */
	public static RenderItem getRenderItem() {
		if (renderItem == null) {
			renderItem = new RenderItem();
		}
		return renderItem;
	}

	/**
	 * Singleton pattern for FontRenderer
	 */
	public static FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	/**
	 * Singleton pattern for RenderEngine
	 */
	public static RenderEngine getRenderEngine() {
		return Minecraft.getMinecraft().renderEngine;
	}

	/**
	 * Returns absolute screen coordinates (int 0 to width) from a relative
	 * coordinate (float -1.0F to +1.0F)
	 * 
	 * @param relx
	 *            Relative X coordinate
	 * @return Absolute X coordinate
	 */
	public int absX(float relx) {
		int absx = (int) ((relx + 1) * getxSize() / 2);
		int xpadding = (width - getxSize()) / 2;
		return absx + xpadding;
	}

	/**
	 * Returns relative coordinate (float -1.0F to +1.0F) from absolute
	 * coordinates (int 0 to width)
	 * 
	 */
	public int relX(float absx) {
		int padding = (width - getxSize()) / 2;
		int relx = (int) ((absx - padding) * 2 / getxSize() - 1);
		return relx;
	}

	/**
	 * Returns absolute screen coordinates (int 0 to width) from a relative
	 * coordinate (float -1.0F to +1.0F)
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
	 * Returns relative coordinate (float -1.0F to +1.0F) from absolute
	 * coordinates (int 0 to width)
	 * 
	 */
	public int relY(float absy) {
		int padding = (height - ySize) / 2;
		int rely = (int) ((absy - padding) * 2 / ySize - 1);
		return rely;
	}

	/**
	 * @return the xSize
	 */
	public int getxSize() {
		return xSize;
	}

	/**
	 * @param xSize
	 *            the xSize to set
	 */
	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	/**
	 * @return the ySize
	 */
	public int getySize() {
		return ySize;
	}

	/**
	 * @param ySize
	 *            the ySize to set
	 */
	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	/**
	 * @param x
	 * @param y
	 */
	protected void drawToolTip() {
		int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getEventY() * this.height
				/ this.mc.displayHeight - 1;
		List<String> tooltip = getToolTip(x, y);
		if (tooltip != null) {
			int strwidth = 0;
			for (String s : tooltip) {
				int currstrwidth = getFontRenderer().getStringWidth(s);
				if (currstrwidth > strwidth) {
					strwidth = currstrwidth;
				}
			}
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			MuseRenderer.drawGradientRect(
					x, y - 10 * tooltip.size() - 5,
					x + 10 + strwidth, y + 5,
					new Colour(0.2F, 0.6F, 0.9F, 0.7F),
					new Colour(0.1F, 0.3F, 0.4F, 0.7F),
					0.0F);
			Colour fontcolour = new Colour(0.9F, 0.6F, 0.2F, 1.0F);
			for (int i = 0; i < tooltip.size(); i++) {
				this.getFontRenderer().drawStringWithShadow(tooltip.get(i),
						x + 5,
						y - 10 * (tooltip.size() - i), fontcolour.getInt());
			}
		}
	}

	/**
	 * @return
	 */
	protected List<String> getToolTip(int x, int y) {
		return null;
	}

	/**
	 * 
	 */
	public void refresh() {
	}
}
