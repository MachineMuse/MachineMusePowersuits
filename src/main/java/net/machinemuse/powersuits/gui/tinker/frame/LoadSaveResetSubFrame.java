package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.DrawableMuseRect;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRect;
import net.machinemuse.powersuits.common.config.CosmeticPresetSaveLoad;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableButton;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class LoadSaveResetSubFrame implements IGuiFrame {
    public ItemSelectionFrame itemSelector;
    public DrawableMuseRect border;
    protected ClickableButton load;
    protected ClickableButton save;
    protected ClickableButton reset;

    public LoadSaveResetSubFrame(MuseRect borderRef, Colour insideColour, Colour borderColour, ItemSelectionFrame itemSelector) {
        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);
        double sizex = border.right() - border.left();
        double sizey = border.bottom() - border.top();
        this.itemSelector = itemSelector;

        this.load = new ClickableButton(
                I18n.format("gui.powersuits.load"),
                new MusePoint2D(border.left() + sizex * 2.5/ 12.0, border.bottom() - sizey / 2.0), true);
        this.save = new ClickableButton(
                I18n.format("gui.powersuits.save"),
                new MusePoint2D(border.right() - sizex * 2.5 / 12.0, border.top() + sizey / 2.0), true);

        this.reset = new ClickableButton(
                I18n.format("gui.powersuits.reset"),
                new MusePoint2D(border.left() + sizex / 2.0, border.top() + sizey / 2.0), true);
    }


    @Override
    public void onMouseDown(double x, double y, int button) {
        if (save.hitBox(x, y) && !itemSelector.getSelectedItem().getItem().isEmpty())
            CosmeticPresetSaveLoad.savePreset("defaultArmorSkin", itemSelector.getSelectedItem().getItem());




    }

    @Override
    public void onMouseUp(double x, double y, int button) {

    }

    @Override
    public void update(double mousex, double mousey) {
        load.setEnabled(true);
        save.setEnabled(true);
        reset.setEnabled(true);
    }

    @Override
    public void draw() {
        border.draw();
        load.draw();
        save.draw();
        reset.draw();
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
