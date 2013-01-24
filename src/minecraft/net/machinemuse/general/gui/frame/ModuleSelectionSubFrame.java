package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.powersuits.powermodule.PowerModule;

public class ModuleSelectionSubFrame {
	protected List<ClickableModule> moduleButtons;
	protected MusePoint2D topleft;
	protected MusePoint2D bottomright;
	protected String category;
	
	public ModuleSelectionSubFrame(String category, MusePoint2D topleft,
			MusePoint2D bottomright) {
		this.category = category;
		this.topleft = topleft;
		this.bottomright = bottomright;
		this.moduleButtons = new ArrayList<ClickableModule>();
	}
	
	public void draw() {
		MuseRenderer.drawString(this.category, topleft.x(), topleft.y());
		for (ClickableModule clickie : moduleButtons) {
			clickie.draw();
		}
	}
	
	public ClickableModule addModule(PowerModule module) {
		double x = topleft.x() + 8 + 16 * moduleButtons.size();
		double y = topleft.y() + 16;
		ClickableModule clickie = new ClickableModule(module, new MusePoint2D(x, y));
		this.moduleButtons.add(clickie);
		return clickie;
		
	}
}
