package machinemuse.powersuits.gui;

import java.util.List;

import machinemuse.general.geometry.Point2D;

/**
 * Defines a generic clickable item for a MuseGui.
 * 
 * @author MachineMuse
 */
public abstract class Clickable {
	private Point2D position;

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

	public abstract boolean hitBox(float x, float y);

	public abstract List<String> getToolTip();
}
