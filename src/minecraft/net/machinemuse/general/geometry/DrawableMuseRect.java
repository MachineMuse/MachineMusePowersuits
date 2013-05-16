package net.machinemuse.general.geometry;

import net.machinemuse.utils.render.MuseRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class DrawableMuseRect extends MuseRect {
    MuseRect lastRect;
    Colour insideColour;
    Colour outsideColour;
    DoubleBuffer vertices;
    DoubleBuffer coloursInside;
    DoubleBuffer coloursOutside;

    public DrawableMuseRect(double left, double top, double right,
                            double bottom, boolean growFromMiddle, Colour insideColour,
                            Colour outsideColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.insideColour = insideColour;
        this.outsideColour = outsideColour;
        if (growFromMiddle) {
            this.lastRect = new MuseRect(left, top, right, bottom);
        }

    }

    public DrawableMuseRect(double left, double top, double right,
                            double bottom, Colour insideColour, Colour outsideColour) {
        super(left, top, right, bottom, false);
        this.insideColour = insideColour;
        this.outsideColour = outsideColour;
    }

    public DrawableMuseRect(MusePoint2D ul, MusePoint2D br,
                            Colour insideColour, Colour outsideColour) {
        super(ul, br);
        this.insideColour = insideColour;
        this.outsideColour = outsideColour;
    }

    public DrawableMuseRect(MuseRect ref, Colour insideColour, Colour outsideColour) {
        super(ref.left(), ref.top(), ref.right(), ref.bottom());
        this.insideColour = insideColour;
        this.outsideColour = outsideColour;
    }

    public void draw() {
        if (vertices == null || coloursInside == null || coloursOutside == null
                || (lastRect != null && !lastRect.equals(this))) {
            this.lastRect = new MuseRect(left(), top(), right(), bottom());
            double cornerradius = 3;
            double zLevel = 1;

            DoubleBuffer corner = MuseRenderer.getArcPoints(Math.PI,
                    3.0 * Math.PI / 2.0, cornerradius, left() + cornerradius,
                    top() + cornerradius, zLevel);

            vertices = BufferUtils.createDoubleBuffer(corner.limit() * 4);
            vertices.put(corner);
            corner = MuseRenderer.getArcPoints(3.0 * Math.PI / 2.0,
                    2.0 * Math.PI, cornerradius, left() + cornerradius,
                    bottom() - cornerradius, zLevel);
            vertices.put(corner);
            corner = MuseRenderer.getArcPoints(0, Math.PI / 2.0, cornerradius,
                    right() - cornerradius, bottom() - cornerradius, zLevel);
            vertices.put(corner);
            corner = MuseRenderer.getArcPoints(Math.PI / 2.0, Math.PI,
                    cornerradius, right() - cornerradius, top() + cornerradius,
                    zLevel);
            vertices.put(corner);
            vertices.flip();
            coloursInside = MuseRenderer.getColourGradient(outsideColour,
                    outsideColour, vertices.limit() * 4 / 3 + 8);
            coloursOutside = MuseRenderer.getColourGradient(insideColour,
                    insideColour, vertices.limit() * 4 / 3 + 8);

        }

        MuseRenderer.blendingOn();
        MuseRenderer.on2D();
        MuseRenderer.texturelessOn();

        MuseRenderer.arraysOnC();

        GL11.glColorPointer(4, 0, coloursInside);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertices.limit() / 3);

        GL11.glColorPointer(4, 0, coloursOutside);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, vertices.limit() / 3);

        MuseRenderer.texturelessOff();
        MuseRenderer.off2D();
        MuseRenderer.blendingOff();
        MuseRenderer.arraysOff();
    }

    public DrawableMuseRect setInsideColour(Colour insideColour) {
        this.insideColour = insideColour;
        return this;
    }

    public DrawableMuseRect setOutsideColour(Colour outsideColour) {
        this.outsideColour = outsideColour;
        return this;

    }
}
