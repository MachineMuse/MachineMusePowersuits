package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.Point2D;

/**
 * Defines a generic clickable item for a MuseGui.
 * 
 * @author MachineMuse
 */
public abstract class Clickable implements IClickable {
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
}
