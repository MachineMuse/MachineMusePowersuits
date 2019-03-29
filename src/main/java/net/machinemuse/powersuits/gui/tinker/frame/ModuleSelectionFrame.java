package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.math.geometry.MuseRect;
import net.machinemuse.numina.math.geometry.MuseRelativeRect;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableItem;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableModule;
import net.minecraft.util.SoundCategory;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ModuleSelectionFrame extends ScrollableFrame {
    protected ItemSelectionFrame target;
    protected Map<String, ModuleSelectionSubFrame> categories = new LinkedHashMap<>();
    protected List<ClickableModule> moduleButtons = new LinkedList<>();
    protected int selectedModule = -1;
    protected IPowerModule prevSelection;
    protected ClickableItem lastItem;
    protected MuseRect lastPosition;

    public ModuleSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright, Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
        super(topleft, bottomright, borderColour, insideColour);
        this.target = target;
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
            this.totalsize = 0;
            for (ModuleSelectionSubFrame frame : categories.values()) {
                totalsize = (int) Math.max(frame.border.bottom() - this.border.top(), totalsize);
            }
            this.currentscrollpixels = Math.min(currentscrollpixels, getMaxScrollPixels());

            super.preDraw();
            GL11.glPushMatrix();
            GL11.glTranslatef(0, -currentscrollpixels, 0);
            drawItems();
            drawSelection();
            GL11.glPopMatrix();
            super.postDraw();
        }
    }

    private void drawItems() {
        for (ModuleSelectionSubFrame frame : categories.values()) {
            frame.drawPartial((int) (this.currentscrollpixels + border.top() + 4),
                    (int) (this.currentscrollpixels + border.top() + border.height() - 4));
        }
    }

    private void drawSelection() {
        ClickableModule module = getSelectedModule();
        if (module != null) {
            MusePoint2D pos = moduleButtons.get(selectedModule).getPosition();
            if (pos.getY() > this.currentscrollpixels + border.top() + 4 && pos.getY() < this.currentscrollpixels + border.top() + border.height() - 4) {
                MuseRenderer.drawCircleAround(pos.getX(), pos.getY(), 10);
            }
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
            moduleButtons = new LinkedList<>();
            categories = new LinkedHashMap<>();

            List<IPowerModule> workingModules = ModuleManager.INSTANCE.getValidModulesForItem(selectedItem.getItem());

            // Prune the list of disallowed modules, if not installed on this item.
            for (Iterator<IPowerModule> it = workingModules.iterator(); it.hasNext(); ) {
                IPowerModule module = it.next();
                if (!module.isAllowed() && !ModuleManager.INSTANCE.itemHasModule(selectedItem.getItem(), module.getDataName())) {
                    it.remove();
                }
            }

            if (workingModules.size() > 0) {
                this.selectedModule = -1;
                for (IPowerModule module : workingModules) {
                    ModuleSelectionSubFrame frame = getOrCreateCategory(module.getCategory().getName());
                    ClickableModule moduleClickable = frame.addModule(module);
                    // Indicate installed modules
                    if (!module.isAllowed()) {
                        // If a disallowed module made it to the list, indicate
                        // it as disallowed
                        moduleClickable.setAllowed(false);
                    } else if (ModuleManager.INSTANCE.itemHasModule(selectedItem.getItem(), module.getDataName())) {
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
        super.onMouseDown(x, y, button);
        if (border.left() < x && border.right() > x && border.top() < y && border.bottom() > y) {
            y += currentscrollpixels;
            // loadModules();
            int i = 0;
            for (ClickableModule module : moduleButtons) {
                if (module.hitBox(x, y)) {
                    Musique.playClientSound(SoundDictionary.SOUND_EVENT_GUI_SELECT, SoundCategory.BLOCKS, 1, null);
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