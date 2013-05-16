package net.machinemuse.general.gui.frame;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRect;
import net.machinemuse.general.geometry.MuseRelativeRect;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.utils.render.MuseRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModuleSelectionSubFrame {
    protected List<ClickableModule> moduleButtons;
    protected MuseRelativeRect border;
    protected String category;

    public ModuleSelectionSubFrame(String category, MuseRelativeRect border) {
        this.category = category;
        this.border = border;
        this.moduleButtons = new ArrayList<ClickableModule>();
    }

    // public void draw() {
    // MuseRenderer.drawString(this.category, border.left(), border.top());
    // for (ClickableModule clickie : moduleButtons) {
    // clickie.draw();
    // }
    // }

    public ClickableModule addModule(IPowerModule module) {
        ClickableModule clickie = new ClickableModule(module, new MusePoint2D(0, 0));
        this.moduleButtons.add(clickie);
        // refreshButtonPositions();
        return clickie;
    }

    public void drawPartial(int min, int max) {
        refreshButtonPositions();
        double top = border.top();
            MuseRenderer.drawString(this.category, border.left(), top);
        for (ClickableModule clickie : moduleButtons) {
            clickie.drawPartial(border.left(), min, border.right(), max);
        }
    }

    public void refreshButtonPositions() {
        int i = 0, j = 0;
        for (ClickableModule clickie : moduleButtons) {
            if (i > 4) {
                i = 0;
                j++;
            }
            double x = border.left() + 8 + 16 * i;
            double y = border.top() + 16 + 16 * j;
            clickie.move(x, y);
            i++;
        }
        border.setHeight(28 + 16 * j);
    }

    public MuseRect getBorder() {
        return border;
    }
}
