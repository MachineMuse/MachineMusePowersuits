package net.machinemuse.general.gui.frame;

import java.util.*;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.powermodule.GenericModule;

public class ModuleSelectionFrame extends ScrollableFrame {
	protected ItemSelectionFrame target;
	protected Map<String, ModuleSelectionSubFrame> categories;
	protected List<ClickableModule> moduleButtons;
	protected int selectedModule = -1;

	public ModuleSelectionFrame(Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		super(topleft, bottomright, borderColour, insideColour);
		this.target = target;

		moduleButtons = new ArrayList();
		categories = new HashMap();
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
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 4);
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
		ClickableItem clickie = target.getSelectedItem();
		if (clickie != null) {
			double centerx = (topleft.x() + bottomright.x()) / 2;
			double centery = (topleft.y() + bottomright.y()) / 2;

			moduleButtons = new ArrayList();
			categories = new HashMap();

			List<GenericModule> workingModules = ItemUtils
					.getValidModulesForItem(null,
							clickie.getItem());
			if (workingModules.size() > 0) {
				List<Point2D> points = MuseRenderer.pointsInLine(
						workingModules.size(),
						new Point2D(centerx, topleft.y()),
						new Point2D(centerx, bottomright.y()));
				Iterator<Point2D> pointiter = points.iterator();
				for (GenericModule module : workingModules) {
					ModuleSelectionSubFrame frame = getOrCreateCategory(module
							.getCategory());
					moduleButtons.add(frame.addModule(module));
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
					new Point2D(topleft.x() + 4, topleft.y() + 4 + 30
							* categories.size()),
					new Point2D(bottomright.x() - 4, bottomright.y() + 34 + 30
							* categories.size()));

			categories.put(category, frame);
			return frame;
		}
	}

	@Override
	public void onMouseDown(int x, int y) {
		loadModules();
		int i = 0;
		for (ClickableModule module : moduleButtons) {
			if (module.hitBox(x, y)) {
				selectedModule = i;
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
