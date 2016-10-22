package net.machinemuse.general.gui.clickable;

import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableTinkerSlider extends ClickableSlider {
    private final NBTTagCompound moduleTag;

    public ClickableTinkerSlider(final MusePoint2D topmiddle, final double width, final NBTTagCompound moduleTag, final String name) {
        super(topmiddle, width, name);
        this.moduleTag = moduleTag;
    }

    public NBTTagCompound moduleTag() {
        return this.moduleTag;
    }

    @Override
    public double value() {
        return this.moduleTag().hasKey(super.name()) ? this.moduleTag().getDouble(super.name()) : 0.0;
    }

    public void moveSlider(final double x, final double y) {
        final double xval = this.position.x() - x;
        final double xratio = MuseMathUtils.clampDouble(0.5 - xval / super.width(), 0.0, 1.0);
        this.moduleTag().setDouble(super.name(), xratio);
    }
}