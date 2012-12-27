/**
 * 
 */
package net.machinemuse.powersuits.gui;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;

/**
 * @author MachineMuse
 * 
 */
public class ClickableButton extends Clickable {
	protected String label;
	protected Point2D radius;
	protected boolean enabled;

	public ClickableButton(String label, Point2D position, Point2D radius,
			boolean enabled) {
		this.label = label;
		this.position = position;
		this.radius = radius;
		this.enabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * machinemuse.powersuits.gui.Clickable#draw(net.minecraft.client.renderer
	 * .RenderEngine, machinemuse.powersuits.gui.MuseGui)
	 */
	@Override
	public void draw(RenderEngine engine, MuseGui gui) {
		Colour topcolour;
		Colour bottomcolour;
		Colour fontcolour;
		if (enabled) {
			topcolour = new Colour(0.5F, 0.6F, 0.8F, 1);
			bottomcolour = new Colour(0.3F, 0.3F, 0.3F, 1);
			fontcolour = new Colour(0.9F, 0.9F, 0.9F, 1);
		} else {
			topcolour = new Colour(0.3F, 0.3F, 0.3F, 1);
			bottomcolour = new Colour(0.5F, 0.6F, 0.8F, 1);
			fontcolour = new Colour(0.4F, 0.4F, 0.4F, 1);
		}
		MuseRenderer.drawGradientRect(
				gui.absX(position.x() - radius.x()),
				gui.absY(position.y() - radius.y()),
				gui.absX(position.x() + radius.x()),
				gui.absY(position.y() + radius.y()),
				topcolour,
				bottomcolour,
				0.0F);
		RenderHelper.disableStandardItemLighting();
		gui.drawCenteredString(gui.getFontRenderer(),
				this.label,
				gui.absX(position.x()),
				gui.absY(position.y()) - 4,
				fontcolour.getInt());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see machinemuse.powersuits.gui.Clickable#hitBox(int, int,
	 * machinemuse.powersuits.gui.MuseGui)
	 */
	@Override
	public boolean hitBox(int x, int y, MuseGui gui) {
		boolean hitx = Math.abs(x - gui.absX(getPosition().x())) < gui.xSize
				* radius.x();
		boolean hity = Math.abs(y - gui.absY(getPosition().y())) < gui.ySize
				* radius.y();
		return hitx && hity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see machinemuse.powersuits.gui.Clickable#getToolTip()
	 */
	@Override
	public List<String> getToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

}
