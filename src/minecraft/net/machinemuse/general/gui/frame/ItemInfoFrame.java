package net.machinemuse.general.gui.frame;

import net.machinemuse.api.IModularItem;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class ItemInfoFrame extends ScrollableFrame {
    public static final double SCALEFACTOR = 1;
    protected ItemSelectionFrame target;
    protected EntityPlayer player;
    protected List<String> info;

    public ItemInfoFrame(EntityPlayer player, MusePoint2D topleft,
                         MusePoint2D bottomright,
                         Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
        super(topleft.times(1.0 / SCALEFACTOR), bottomright.times(1.0 / SCALEFACTOR), borderColour, insideColour);
        this.target = target;
        this.player = player;
    }

    @Override
    public void update(double mousex, double mousey) {
        ClickableItem selected = target.getSelectedItem();
        if (selected != null) {
            IModularItem item = MuseItemUtils.getAsModular(selected.getItem().getItem());
            info = item.getLongInfo(player, selected.getItem());
        } else {
            info = null;
        }
    }

    @Override
    public void draw() {
        if (info != null) {
            GL11.glPushMatrix();
            GL11.glScaled(SCALEFACTOR, SCALEFACTOR, SCALEFACTOR);
            super.draw();
            int xoffset = 8;
            int yoffset = 8;
            int i = 0;
            for (String infostring : info) {
                String[] str = infostring.split("\t");
                MuseRenderer.drawStringsJustified(Arrays.asList(str),
                        border.left()
                                + xoffset, border.right() - xoffset,
                        border.top() + yoffset
                                + i * 10);

                i++;
            }
            GL11.glPopMatrix();
        }

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
