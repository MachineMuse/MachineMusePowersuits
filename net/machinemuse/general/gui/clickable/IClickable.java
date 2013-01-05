package net.machinemuse.general.gui.clickable;

import java.util.List;

public interface IClickable {

	public abstract void draw();

	public abstract boolean hitBox(int x, int y);

	public abstract List<String> getToolTip();
}
