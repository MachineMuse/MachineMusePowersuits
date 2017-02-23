package net.machinemuse.general.gui.clickable;

import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableTinkerSlider extends ClickableSlider {
    NBTTagCompound moduleTag;

    public ClickableTinkerSlider(MusePoint2D topmiddle, double width, NBTTagCompound moduleTag, String name) {
        super(topmiddle, width, name);
        this.moduleTag = moduleTag;
    }

    @Override
    public double value() {
        return (moduleTag.hasKey(name)) ? moduleTag.getDouble(name) : 0;
    }

    public void moveSlider(double x, double y) {
        double xval = position.x() - x;
        double xratio = MuseMathUtils.clampDouble(0.5 - (xval / width), 0, 1);
        moduleTag.setDouble(name, xratio);
    }
}
