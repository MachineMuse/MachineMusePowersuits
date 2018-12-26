package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.gui.clickable.ClickableSlider;
import net.machinemuse.numina.utils.math.MuseMathUtils;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableTinkerSlider extends ClickableSlider {
    public NBTTagCompound moduleTag;

    public ClickableTinkerSlider(MusePoint2D topmiddle, double width, NBTTagCompound moduleTag, String id, String label) {
        super(topmiddle, width, id, label);
        this.moduleTag = moduleTag;
    }

    @Override
    public double getValue() {
        return (moduleTag.hasKey(this.id())) ? moduleTag.getDouble(id()) : 0;
    }

    @Override
    public void setValueByX(double x) {
        super.setValueByX(x);
        moduleTag.setDouble(id(), super.getValue());
    }
}