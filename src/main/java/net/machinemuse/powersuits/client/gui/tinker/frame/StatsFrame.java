package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.powersuits.utils.nbt.NBTTagAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Set;

public class StatsFrame extends ScrollableFrame {
    protected NBTTagCompound properties;
    protected ItemStack stack;
    protected Set<String> propertiesToList;

    public StatsFrame(MusePoint2D topleft, MusePoint2D bottomright,
                      Colour borderColour, Colour insideColour, ItemStack stack) {
        super(topleft, bottomright, borderColour, insideColour);
        this.stack = stack;
        this.properties = MuseNBTUtils.getMuseItemTag(stack);
        this.propertiesToList = NBTTagAccessor.getMap(properties).keySet();
    }

    @Override
    public void draw() {
        GL11.glPushMatrix();
        super.draw();
        int xoffset = 8;
        int yoffset = 8;
        int i = 0;
        for (String propName : propertiesToList) {
            double propValue = MuseItemUtils.getDoubleOrZero(properties, propName);
            String propValueString = String.format("%.2f", propValue);
            double strlen = MuseRenderer.getStringWidth(propValueString);
            MuseRenderer.drawString(propName, border.left() + xoffset, border.top() + yoffset + i * 10);
            MuseRenderer.drawString(propValueString, border.bottom() - xoffset - strlen - 40, border.top() + yoffset + i * 10);
            i++;
        }
        GL11.glPopMatrix();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }
}
