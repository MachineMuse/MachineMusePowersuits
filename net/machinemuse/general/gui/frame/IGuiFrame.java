package net.machinemuse.general.gui.frame;

import java.util.List;

public interface IGuiFrame {

	public abstract void onMouseDown(int x, int y);

	public abstract void onMouseMove(int x, int y);

	public abstract void onMouseUp(int x, int y);

	public abstract void draw();

	public abstract List<String> getToolTip(int x, int y);
}
