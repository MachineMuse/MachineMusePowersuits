package net.machinemuse.general.gui.clickable;

import net.machinemuse.numina.geometry.MusePoint2D;

import java.util.List;

public interface IClickable {

	public abstract void draw();

	public abstract void move(double x, double y);

	public abstract MusePoint2D getPosition();

	public abstract boolean hitBox(double x, double y);

	public abstract List<String> getToolTip();
}
