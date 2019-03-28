package net.machinemuse.numina.math.geometry;

import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.math.Colour;
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

    @Override
    public DrawableMuseRect copyOf() {
        return new DrawableMuseRect(super.left(), super.top(), super.right(), super.bottom(),
                this.lastRect != null, insideColour, outsideColour);
    }

    @Override
    public DrawableMuseRect setLeft(double value) {
        super.setLeft(value);
        return this;
    }

    @Override
    public DrawableMuseRect setRight(double value) {
        super.setRight(value);
        return this;
    }

    @Override
    public DrawableMuseRect setTop(double value) {
        super.setTop(value);
        return this;
    }

    @Override
    public DrawableMuseRect setBottom(double value) {
        super.setBottom(value);
        return this;
    }

    @Override
    public DrawableMuseRect setWidth(double value) {
        super.setWidth(value);
        return this;
    }

    @Override
    public DrawableMuseRect setHeight(double value) {
        super.setHeight(value);
        return this;
    }

    public void draw() {
        if (vertices == null || coloursInside == null || coloursOutside == null
                || (lastRect != null && !lastRect.equals(this))) {
            this.lastRect = new MuseRect(left(), top(), right(), bottom());
            double cornerradius = 3;
            double zLevel = 1;

            DoubleBuffer corner = GradientAndArcCalculator.getArcPoints(Math.PI,
                    3.0 * Math.PI / 2.0, cornerradius, left() + cornerradius,
                    top() + cornerradius, zLevel);

            vertices = BufferUtils.createDoubleBuffer(corner.limit() * 4);
            vertices.put(corner);
            corner = GradientAndArcCalculator.getArcPoints(3.0 * Math.PI / 2.0,
                    2.0 * Math.PI, cornerradius, left() + cornerradius,
                    bottom() - cornerradius, zLevel);
            vertices.put(corner);
            corner = GradientAndArcCalculator.getArcPoints(0, Math.PI / 2.0, cornerradius,
                    right() - cornerradius, bottom() - cornerradius, zLevel);
            vertices.put(corner);
            corner = GradientAndArcCalculator.getArcPoints(Math.PI / 2.0, Math.PI,
                    cornerradius, right() - cornerradius, top() + cornerradius,
                    zLevel);
            vertices.put(corner);
            vertices.flip();
            coloursInside = GradientAndArcCalculator.getColourGradient(outsideColour,
                    outsideColour, vertices.limit() * 4 / 3 + 8);
            coloursOutside = GradientAndArcCalculator.getColourGradient(insideColour,
                    insideColour, vertices.limit() * 4 / 3 + 8);

        }

        RenderState.blendingOn();
        RenderState.on2D();
        RenderState.texturelessOn();

        RenderState.arraysOnColor();

        GL11.glColorPointer(4, 0, coloursInside);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertices.limit() / 3);

        GL11.glColorPointer(4, 0, coloursOutside);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, vertices.limit() / 3);

        RenderState.texturelessOff();
        RenderState.off2D();
        RenderState.blendingOff();
        RenderState.arraysOff();
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
