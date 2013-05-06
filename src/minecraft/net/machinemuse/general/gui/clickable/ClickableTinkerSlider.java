package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;

public class ClickableTinkerSlider extends ClickableSlider {
    protected NBTTagCompound moduleTag;

    public ClickableTinkerSlider(MusePoint2D topmiddle, double width, NBTTagCompound moduleTag, String name) {
        super(topmiddle, width, name);
        this.moduleTag = moduleTag;
    }


    @Override
    public double value() {
        double val = 0;
        if (moduleTag.hasKey(name())) {
            val = moduleTag.getDouble(name());
        }
        return val;
    }

    public void moveSlider(double x, double y) {
        double xval = position.x() - x;
        double xratio = 0.5 - (xval / width());
        xratio = Math.max(Math.min(xratio, 1.0), 0.0); // Clamp
        moduleTag.setDouble(name(), xratio);

    }

}
