package net.machinemuse.general.geometry;

import net.machinemuse.utils.render.MuseRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class DrawableMuseCircle {
    public static final double detail = 4;
    protected static DoubleBuffer points;
    protected DoubleBuffer colour;

    public DrawableMuseCircle(Colour c1, Colour c2) {
        if (points == null) {
            DoubleBuffer arcPoints = MuseRenderer.getArcPoints(0, Math.PI * 2 + 0.0001, detail, 0, 0, 0);
            points = BufferUtils.createDoubleBuffer(arcPoints.limit() + 6);
            points.put(new double[]{0, 0, 0});
            points.put(arcPoints);
            arcPoints.rewind();
            points.put(arcPoints.get());
            points.put(arcPoints.get());
            points.put(arcPoints.get());
            points.flip();
        }
        DoubleBuffer colourPoints = MuseRenderer.getColourGradient(c1, c1, points.limit() / 3);
        colour = BufferUtils.createDoubleBuffer(colourPoints.limit() + 4);
        colour.put(c2.asArray());
        colour.put(colourPoints);
        colour.flip();
    }

    public void draw(double radius, double x, double y) {
        points.rewind();
        colour.rewind();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(radius / detail, radius / detail, 1.0);
        MuseRenderer.on2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        MuseRenderer.arraysOnC();
        MuseRenderer.texturelessOn();
        MuseRenderer.blendingOn();
        GL11.glColorPointer(4, 0, colour);
        GL11.glVertexPointer(3, 0, points);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, points.limit() / 3);
        MuseRenderer.blendingOff();
        MuseRenderer.texturelessOff();
        MuseRenderer.arraysOff();
        MuseRenderer.off2D();
        GL11.glPopMatrix();
    }
}
