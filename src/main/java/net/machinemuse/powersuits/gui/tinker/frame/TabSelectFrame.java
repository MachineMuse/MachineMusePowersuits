package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.client.gui.frame.IGuiFrame;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MachineMuse
 * <p>
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
    List<String> toolTip = new ArrayList<>();

    public TabSelectFrame(EntityPlayer p, MusePoint2D topleft, MusePoint2D bottomright, int worldx, int worldy, int worldz) {
        this.p = p;
        this.topleft = topleft;
        this.bottomright = bottomright;
        this.worldx = worldx;
        this.worldy = worldy;
        this.worldz = worldz;

        this.buttons.put(new ClickableButton(I18n.format("gui.powersuits.tab.tinker"), topleft.midpoint(bottomright).minus(100, 0), worldy < 256 && worldy > 0), 0);
        this.buttons.put(new ClickableButton(I18n.format("gui.powersuits.tab.keybinds"), topleft.midpoint(bottomright), true), 1);
        this.buttons.put(new ClickableButton(I18n.format("gui.powersuits.tab.visual"), topleft.midpoint(bottomright).plus(100, 0), true), 3);
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        for (ClickableButton b : buttons.keySet()) {
            if (b.isEnabled() && b.hitBox(x, y)) {
                p.openGui(ModularPowersuits.getInstance(), buttons.get(b), p.world, worldx, worldy, worldz);
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

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
