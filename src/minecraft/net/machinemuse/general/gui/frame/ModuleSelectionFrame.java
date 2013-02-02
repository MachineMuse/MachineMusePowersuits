package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRect;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableModule;

public class ModuleSelectionFrame extends ScrollableFrame {
	protected ItemSelectionFrame target;
	protected Map<String, ModuleSelectionSubFrame> categories;
	protected List<ClickableModule> moduleButtons;
	protected int selectedModule = -1;
	protected IPowerModule prevSelection;

	public ModuleSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		super(topleft, bottomright, borderColour, insideColour);
		this.target = target;

		moduleButtons = new ArrayList();
		categories = new HashMap();
	}

	@Override
	public void update(double mousex, double mousey) {

	}

	@Override
	public void draw() {
		if (target.getSelectedItem() != null) {
			loadModules();
			drawBackground();
			drawItems();
			drawSelection();
		}
	}

	private void drawBackground() {
		super.draw();
	}

	private void drawItems() {
		for (ModuleSelectionSubFrame frame : categories.values()) {
			frame.draw();
		}
	}

	private void drawSelection() {
		ClickableModule module = getSelectedModule();
		if (module != null) {
			MuseRenderer.drawCircleAround(
					moduleButtons.get(selectedModule).getPosition().x(),
					moduleButtons.get(selectedModule).getPosition().y(),
					10);

		}

	}

	public ClickableModule getSelectedModule() {
		if (moduleButtons.size() > selectedModule && selectedModule != -1) {
			return moduleButtons.get(selectedModule);
		} else {
			return null;
		}
	}

	public void loadModules() {
		ClickableItem selectedItem = target.getSelectedItem();
		if (selectedItem != null) {
			double centerx = (border.left() + border.right()) / 2;
			double centery = (border.top() + border.bottom()) / 2;

			moduleButtons = new ArrayList();
			categories = new HashMap();

			List<IPowerModule> workingModules = MuseItemUtils.getValidModulesForItem(null, selectedItem.getItem());
			if (workingModules.size() > 0) {
				List<MusePoint2D> points = MuseRenderer.pointsInLine(
						workingModules.size(),
						new MusePoint2D(centerx, border.top()),
						new MusePoint2D(centerx, border.bottom()));
				this.selectedModule = -1;
				Iterator<MusePoint2D> pointiter = points.iterator();
				for (IPowerModule module : workingModules) {
					ModuleSelectionSubFrame frame = getOrCreateCategory(module.getCategory());
					ClickableModule moduleClickable = frame.addModule(module);
					if (moduleClickable.getModule().equals(this.prevSelection)) {
						this.selectedModule = moduleButtons.size();
					}
					moduleButtons.add(moduleClickable);
				}
			}
		}
	}

	private ModuleSelectionSubFrame getOrCreateCategory(String category) {
		if (categories.containsKey(category)) {
			return categories.get(category);
		} else {
			ModuleSelectionSubFrame frame = new ModuleSelectionSubFrame(
					category,
					new MuseRect(
							border.left() + 4,
							border.top() + 4 + 30 * categories.size(),
							border.right() - 4,
							border.bottom() + 34 + 30 * categories.size()));

			categories.put(category, frame);
			return frame;
		}
	}

	@Override
	public void onMouseDown(double x, double y, int button) {
		loadModules();
		int i = 0;
		for (ClickableModule module : moduleButtons) {
			if (module.hitBox(x, y)) {
				selectedModule = i;
				prevSelection = module.getModule();
				break;
			} else {
				i++;
			}
		}
	}

	@Override
	public List<String> getToolTip(int x, int y) {
		if (moduleButtons != null) {
			int moduleHover = -1;
			int i = 0;
			for (ClickableModule module : moduleButtons) {
				if (module.hitBox(x, y)) {
					moduleHover = i;
					break;
				} else {
					i++;
				}
			}
			if (moduleHover > -1) {
				return moduleButtons.get(moduleHover).getToolTip();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
