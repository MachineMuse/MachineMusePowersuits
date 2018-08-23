package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.DrawableMuseRect;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected int totalsize;
    protected int currentscrollpixels;
    protected final int buttonsize = 5;
    protected boolean scrollbarPicked = false;
    protected boolean scrolldownPicked = false;
    protected boolean scrollupPicked = false;
    protected int lastdWheel = Mouse.getDWheel();

    protected DrawableMuseRect border;

    public ScrollableFrame(MusePoint2D topleft, MusePoint2D bottomright,
                           Colour borderColour, Colour insideColour) {
        border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour);
    }

    protected double getScrollAmount() {
        return 8;
    }

    @Override
    public void update(double x, double y) {
        if (border.containsPoint(x, y)) {
            int dscroll = (lastdWheel - Mouse.getDWheel()) / 15;
            lastdWheel = Mouse.getDWheel();
            if (Mouse.isButtonDown(0)) {
                if ((y - border.top()) < buttonsize && currentscrollpixels > 0) {
                    dscroll -= getScrollAmount();
                } else if ((border.bottom() - y) < buttonsize) {
                    dscroll += getScrollAmount();
                }
            }
            currentscrollpixels = (int) MuseMathUtils.clampDouble(currentscrollpixels + dscroll, 0, getMaxScrollPixels());
        }
    }

    public void preDraw() {
        border.draw();
        RenderState.glowOn();
        RenderState.texturelessOn();
        GL11.glBegin(GL11.GL_TRIANGLES);
        Colour.LIGHTBLUE.doGL();
        // Can scroll down
        if (currentscrollpixels + border.height() < totalsize) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.bottom(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.bottom() - 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.bottom() - 4, 1);
        }
        // Can scroll up
        if (currentscrollpixels > 0) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.top(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.top() + 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.top() + 4, 1);
        }
        Colour.WHITE.doGL();
        GL11.glEnd();
        RenderState.texturelessOff();
        RenderState.scissorsOn(border.left() + 4, border.top() + 4, border.width() - 8, border.height() - 8);
    }
    public void postDraw() {
        RenderState.scissorsOff();
        RenderState.glowOff();
    }

    @Override
    public void draw() {
        preDraw();
        postDraw();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }
}
