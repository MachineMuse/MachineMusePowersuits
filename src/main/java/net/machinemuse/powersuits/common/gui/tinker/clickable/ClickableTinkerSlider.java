package net.machinemuse.powersuits.common.gui.tinker.clickable;

import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableTinkerSlider extends ClickableSlider {
    NBTTagCompound moduleTag;
    boolean isDouble;

    public ClickableTinkerSlider(MusePoint2D topmiddle, double width, NBTTagCompound moduleTag, String name) {
        super(topmiddle, width, name);
        this.moduleTag = moduleTag;
    }

    @Override
    public double getValue() {
        return (moduleTag.hasKey(name)) ? moduleTag.getDouble(name) : 0;
    }

    @Override
    public void setValueByX(double x) {
        super.setValueByX(x);
        moduleTag.setDouble(name, valueInternal);
    }
}