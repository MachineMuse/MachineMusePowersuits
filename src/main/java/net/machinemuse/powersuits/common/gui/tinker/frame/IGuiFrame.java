package net.machinemuse.powersuits.common.gui.tinker.frame;

import java.util.List;

public interface IGuiFrame {
	
	void onMouseDown(double x, double y, int button);
	
	void onMouseUp(double x, double y, int button);
	
	void update(double mousex, double mousey);
	
	void draw();
	
	List<String> getToolTip(int x, int y);
}