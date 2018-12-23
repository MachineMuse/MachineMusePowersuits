package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.gui.clickable.ClickableSlider;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableTinkerSlider extends ClickableSlider {
    NBTTagCompound moduleTag;

    public ClickableTinkerSlider(MusePoint2D topmiddle, double width, NBTTagCompound moduleTag, String name, String label) {
        super(topmiddle, width, name, label);
        this.moduleTag = moduleTag;
    }

    @Override
    public double getValue() {
        return (moduleTag.hasKey(this.name())) ? moduleTag.getDouble(name()) : 0;
    }

    @Override
    public void setValueByX(double x) {
        super.setValueByX(x);
        moduleTag.setDouble(name(), getValue());
    }
}