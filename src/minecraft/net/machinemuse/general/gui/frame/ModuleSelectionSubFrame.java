package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRect;
import net.machinemuse.general.gui.clickable.ClickableModule;

public class ModuleSelectionSubFrame {
	protected List<ClickableModule> moduleButtons;
	protected MuseRect border;
	protected String category;

	public ModuleSelectionSubFrame(String category, MuseRect border) {
		this.category = category;
		this.border = border;
		this.moduleButtons = new ArrayList<ClickableModule>();
	}

	public void draw() {
		MuseRenderer.drawString(this.category, border.left(), border.top());
		for (ClickableModule clickie : moduleButtons) {
			clickie.draw();
		}
	}

	public ClickableModule addModule(IPowerModule module) {
		double x = border.left() + 8 + 16 * (moduleButtons.size() % 5);
		double y = border.top() + 16 + 16 * (moduleButtons.size() / 5);
		border.setBottom(border.top() + 28 + 16 * (moduleButtons.size() / 5));
		ClickableModule clickie = new ClickableModule(module, new MusePoint2D(x, y));
		this.moduleButtons.add(clickie);
		return clickie;

	}

	public void drawPartial(int min, int max) {
		if (min < border.top() + 4 && max > border.top() - 4) {
			MuseRenderer.drawString(this.category, border.left(), border.top());
		}
		for (ClickableModule clickie : moduleButtons) {
			clickie.drawPartial(border.left(), min, border.right(), max);
		}
	}

	public MuseRect getBorder() {
		return border;
	}
}
