package machinemuse.powersuits.gui;

import java.util.List;

import machinemuse.general.geometry.Point2D;
import net.minecraft.client.renderer.RenderEngine;

/**
 * Defines a generic clickable item for a MuseGui.
 * 
 * @author MachineMuse
 */
public abstract class Clickable {
	protected Point2D position;

	public Clickable() {
		position = new Point2D(0, 0);
	}

	public Clickable(Point2D point) {
		position = point;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public abstract void draw(RenderEngine engine, MuseGui gui);

	public abstract boolean hitBox(int x, int y, MuseGui gui);

	public abstract List<String> getToolTip();
}
