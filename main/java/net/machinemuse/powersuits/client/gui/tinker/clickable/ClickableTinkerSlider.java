package net.machinemuse.powersuits.client.gui.tinker.clickable;

import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

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
        if (moduleTag.hasKey(name, Constants.NBT.TAG_DOUBLE))
            return moduleTag.getDouble(name);
        else if (moduleTag.hasKey(name, Constants.NBT.TAG_INT))
            return moduleTag.getInteger(name) / 1000.0D;
        return 0;
    }

    public void moveSlider(double x, double y) {
        double xval = position.x() - x;
        double xratio = MuseMathUtils.clampDouble(0.5 - (xval / width), 0, 1);

        if (moduleTag.hasKey(name, Constants.NBT.TAG_DOUBLE))
            moduleTag.setDouble(name, xratio);
        else
            moduleTag.setInteger(name, (int) (1000 * xratio));
    }
}