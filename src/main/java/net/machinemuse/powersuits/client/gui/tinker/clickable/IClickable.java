package net.machinemuse.general.gui.clickable;


import net.machinemuse.numina.geometry.MusePoint2D;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IClickable {

    void draw();

    void move(double x, double y);

    MusePoint2D getPosition();

    boolean hitBox(double x, double y);

    List getToolTip();
}