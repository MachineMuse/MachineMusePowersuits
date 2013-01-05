package net.machinemuse.powersuits.gui.frame;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.gui.clickable.ClickableModule;
import net.machinemuse.powersuits.powermodule.GenericModule;

public class ModuleSelectionSubFrame {
	protected List<ClickableModule> moduleButtons;
	protected Point2D topleft;
	protected Point2D bottomright;
	protected String category;

	public ModuleSelectionSubFrame(String category, Point2D topleft,
			Point2D bottomright) {
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

	public ClickableModule addModule(GenericModule module) {
		double x = topleft.x() + 8 + 16 * moduleButtons.size();
		double y = topleft.y() + 16;
		ClickableModule clickie = new ClickableModule(module, new Point2D(x, y));
		this.moduleButtons.add(clickie);
		return clickie;

	}
}
