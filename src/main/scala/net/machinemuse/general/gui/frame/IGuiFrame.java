package net.machinemuse.general.gui.frame;

import java.util.List;

public interface IGuiFrame {
	
	public abstract void onMouseDown(double x, double y, int button);
	
	public abstract void onMouseUp(double x, double y, int button);
	
	public abstract void update(double mousex, double mousey);
	
	public abstract void draw();
	
	public abstract List<String> getToolTip(int x, int y);
	
}
