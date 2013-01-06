package net.machinemuse.general.gui.clickable;

import java.util.List;

public interface IClickable {

	public abstract void draw();

	public abstract boolean hitBox(double x, double y);

	public abstract List<String> getToolTip();
}
