package net.machinemuse.general.gui.frame;

import java.util.*;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRect;
import net.machinemuse.general.geometry.MuseRelativeRect;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableModule;

import org.lwjgl.opengl.GL11;

public class ModuleSelectionFrame extends ScrollableFrame {
	protected ItemSelectionFrame target;
	protected Map<String, ModuleSelectionSubFrame> categories;
	protected List<ClickableModule> moduleButtons;
	protected int selectedModule = -1;
	protected IPowerModule prevSelection;
	protected MuseRect lastPosition;

	public ModuleSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		super(topleft, bottomright, borderColour, insideColour);
		this.target = target;

		moduleButtons = new ArrayList();
		categories = new HashMap();
	}

	@Override
	public void update(double mousex, double mousey) {
		super.update(mousex, mousey);
	}

	@Override
	public void draw() {
		if (target.getSelectedItem() != null) {
			loadModules();
			for (ModuleSelectionSubFrame frame : categories.values()) {
				totalsize = (int) Math.max(frame.border.bottom() - this.border.top(), totalsize);
			}
			drawBackground();
			GL11.glPushMatrix();
			GL11.glTranslatef(0, -currentscrollpixels, 0);
			drawItems();
			drawSelection();
			GL11.glPopMatrix();
		}
	}

	private void drawBackground() {
		super.draw();
	}

	private void drawItems() {
		for (ModuleSelectionSubFrame frame : categories.values()) {
			frame.drawPartial((int) (this.currentscrollpixels + border.top()), (int) (this.currentscrollpixels + border.top() + border.height()));
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
		this.lastPosition = null;
		ClickableItem selectedItem = target.getSelectedItem();
		if (selectedItem != null) {
			double centerx = (border.left() + border.right()) / 2;
			double centery = (border.top() + border.bottom()) / 2;

			moduleButtons = new ArrayList();
			categories = new HashMap();

			List<IPowerModule> workingModules = MuseItemUtils.getValidModulesForItem(null, selectedItem.getItem());

			// Prune the list of disallowed modules, if not installed on this
			// item.
			for (Iterator<IPowerModule> it = workingModules.iterator(); it.hasNext();) {
				IPowerModule module = it.next();
				if (module.isAllowed() == false &&
						MuseItemUtils.itemHasModule(selectedItem.getItem(), module.getName()) == false) {
					it.remove();
				}
			}

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
					// Indicate installed modules
					if (module.isAllowed() == false) {
						// If a disallowed module made it to the list, indicate
						// it as disallowed
						moduleClickable.setAllowed(false);
					} else if (MuseItemUtils.itemHasModule(selectedItem.getItem(), module.getName())) {
						moduleClickable.setInstalled(true);
					}
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
			MuseRelativeRect position = new MuseRelativeRect(
					border.left() + 4,
					border.top() + 4 + 30 * categories.size(),
					border.right() - 4,
					border.top() + 34 + 30 * categories.size());
			if (!categories.isEmpty()) {
				position.setBelow(lastPosition);
				lastPosition = position;
			}

			ModuleSelectionSubFrame frame = new ModuleSelectionSubFrame(
					category,
					position);

			categories.put(category, frame);
			return frame;
		}
	}

	@Override
	public void onMouseDown(double x, double y, int button) {
		if (border.left() < x && border.right() > x && border.top() < y && border.bottom() > y) {
			y += currentscrollpixels;
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
	}

	@Override
	public List<String> getToolTip(int x, int y) {
		if (border.left() < x && border.right() > x && border.top() < y && border.bottom() > y) {
			y += currentscrollpixels;
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
			}
		}
		return null;
	}

}
