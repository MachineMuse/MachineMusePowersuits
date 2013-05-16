package net.machinemuse.general.geometry;

import net.machinemuse.utils.render.MuseRenderer;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class SwirlyMuseCircle {

    public static final double detail = 4;
    protected DoubleBuffer points;
    protected DoubleBuffer colour;

    public SwirlyMuseCircle(Colour c1, Colour c2) {
        if (points == null) {
            points = MuseRenderer.getArcPoints(0, Math.PI * 2 + 0.0001, detail, 0, 0, 0);
        }
        colour = MuseRenderer.getColourGradient(c1, c2, points.limit() / 3);
    }

    public void draw(double radius, double x, double y) {
        int length = points.limit();
        double ratio = (System.currentTimeMillis() % 2000) / 2000.0;
        colour.rewind();
        points.rewind();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(radius / detail, radius / detail, 1.0);
        GL11.glRotatef((float) (-ratio * 360.0), 0, 0, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        MuseRenderer.arraysOnC();
        MuseRenderer.texturelessOn();
        MuseRenderer.blendingOn();
        GL11.glColorPointer(4, 0, colour);
        GL11.glVertexPointer(3, 0, points);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, length / 3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        MuseRenderer.blendingOff();
        MuseRenderer.texturelessOff();
        MuseRenderer.arraysOff();
        GL11.glPopMatrix();
    }
}
