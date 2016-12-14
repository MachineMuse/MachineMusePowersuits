package net.machinemuse.general.gui.frame;

import net.machinemuse.general.gui.clickable.ClickableButton;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class TabSelectFrame implements IGuiFrame {
    EntityPlayer p;
    MusePoint2D topleft;
    MusePoint2D bottomright;
    int worldx;
    int worldy;
    int worldz;
    Map<ClickableButton, Integer> buttons = new HashMap<>();

    public TabSelectFrame(EntityPlayer p, MusePoint2D topleft, MusePoint2D bottomright, int worldx, int worldy, int worldz) {
        this.p = p;
        this.topleft = topleft;
        this.bottomright = bottomright;
        this.worldx = worldx;
        this.worldy = worldy;
        this.worldz = worldz;

        this.buttons.put(new ClickableButton(StatCollector.translateToLocal("gui.tab.tinker"), topleft.midpoint(bottomright).minus(100, 0), worldy < 256  && worldy > 0), 0);
        this.buttons.put(new ClickableButton(StatCollector.translateToLocal("gui.tab.keybinds"), topleft.midpoint(bottomright), true), 1);
        this.buttons.put(new ClickableButton(StatCollector.translateToLocal("gui.tab.visual"), topleft.midpoint(bottomright).plus(100, 0), true), 3);
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        for (ClickableButton b : buttons.keySet()) {
            if (b.isEnabled() && b.hitBox(x, y)) {
                p.openGui(ModularPowersuits.getInstance(), buttons.get(b), p.worldObj, worldx, worldy, worldz);
            }
        }
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    }

    @Override
    public void update(double mousex, double mousey) {
    }

    @Override
    public void draw() {
        for (ClickableButton b : buttons.keySet())
            b.draw();
    }

    List<String> toolTip = new ArrayList<>();

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
