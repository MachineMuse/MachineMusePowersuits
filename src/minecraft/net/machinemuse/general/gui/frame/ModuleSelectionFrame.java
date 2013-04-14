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
	protected ClickableItem lastItem;
	protected MuseRect lastPosition;

	public ModuleSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		super(topleft, bottomright, borderColour, insideColour);
		this.target = target;

		moduleButtons = new ArrayList<ClickableModule>();
		categories = new HashMap<String, ModuleSelectionSubFrame>();
	}

	@Override
	public void update(double mousex, double mousey) {
		super.update(mousex, mousey);
	}

	@Override
	public void draw() {
		for (ModuleSelectionSubFrame frame : categories.values()) {
			frame.refreshButtonPositions();
		}
		if (target.getSelectedItem() != null) {
			if (lastItem != target.getSelectedItem()) {
				loadModules();
			}
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
			moduleButtons = new ArrayList<ClickableModule>();
			categories = new HashMap<String, ModuleSelectionSubFrame>();

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
				this.selectedModule = -1;
				for (IPowerModule module : workingModules) {
					ModuleSelectionSubFrame frame = getOrCreateCategory(module.getCategory());
					ClickableModule moduleClickable = frame.addModule(module);
					// Indicate installed modules
					if (!module.isAllowed()) {
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
			for (ModuleSelectionSubFrame frame : categories.values()) {
				frame.refreshButtonPositions();
			}
		}
	}

	private ModuleSelectionSubFrame getOrCreateCategory(String category) {
		if (categories.containsKey(category)) {
			return categories.get(category);
		} else {
			MuseRelativeRect position = new MuseRelativeRect(
					border.left() + 4,
					border.top() + 4,
					border.right() - 4,
					border.top() + 32);
			position.setBelow(lastPosition);
			lastPosition = position;
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
