package net.machinemuse.general.gui.frame;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IGuiFrame {
	
	void onMouseDown(double x, double y, int button);
	
	void onMouseUp(double x, double y, int button);
	
	void update(double mousex, double mousey);
	
	void draw();
	
	List<String> getToolTip(int x, int y);
}