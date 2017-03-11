package net.machinemuse.numina.geometry;

import net.machinemuse.numina.render.RenderState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;


public class SwirlyMuseCircle {
    public static final double detail = 4;
    protected DoubleBuffer points;
    protected DoubleBuffer colour;

    int numsegments;

    int renderpass = 0;

    public SwirlyMuseCircle(Colour c1, Colour c2) {
        if (points == null) {
            points = GradientAndArcCalculator.getArcPoints(0, Math.PI * 2 + 0.0001, detail, 0, 0, 0);
        }
        numsegments = points.limit() / 3;

//        colour = GradientAndArcCalculator.getColourGradient(c1, c2, points.limit() / 3);
        colour = GradientAndArcCalculator.getColourGradient(c2, c1, points.limit() / 3);
    }

    public void draw(double radius, double x, double y) {
//        DrawCircle((float)x, (float)y, (float) radius, numsegments);


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
        RenderState.arraysOnC();
        RenderState.texturelessOn();
        RenderState.blendingOn();
//        GL11.glColorPointer(4, 0, colour);
//        GL11.glVertexPointer(3, 0, points);
//        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, length / 3);

        GL11.glColorPointer(4, 0, colour);
        GL11.glVertexPointer(3, 0, points);
        GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, points.limit() / 3);



        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        RenderState.blendingOff();
        RenderState.texturelessOff();
        RenderState.arraysOff();
        GL11.glPopMatrix();
    }


    // ===========================================================
    // http://slabode.exofire.net/circle_draw.shtml
    //======================================================

    private float sinf(float f) {
        return (float) Math.sin(f);
    }

    private float cosf(float f) {
        return (float) Math.cos(f);
    }

    private float tanf(float f) {
        return (float) Math.tan(f);
    }

    void DrawCircle(float cx, float cy, float r, int num_segments)
    {
        float theta = (float) (2 * Math.PI / (float)num_segments);
        float c = cosf(theta);//precalculate the sine and cosine
        float s = sinf(theta);
        float t;

        float x = r;//we start at angle = 0
        float y = 0;

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for(int ii = 0; ii < num_segments; ii++)
        {
            GL11.glVertex2f(x + cx, y + cy);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        GL11.glEnd();
    }


    void DrawArc(float cx, float cy, float r, float start_angle, float arc_angle, int num_segments)
    {
        float theta = arc_angle / (float)(num_segments - 1);//theta is now calculated from the arc angle instead, the - 1 bit comes from the fact that the arc is open

        float tangetial_factor = tanf(theta);

        float radial_factor = cosf(theta);


        float x = r * cosf(start_angle);//we now start at the start angle
        float y = r * sinf(start_angle);

        GL11.glBegin(GL11.GL_LINE_STRIP);//since the arc is not a closed curve, this is a strip now
        for(int ii = 0; ii < num_segments; ii++)
        {
            GL11.glVertex2f(x + cx, y + cy);

            float tx = -y;
            float ty = x;

            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            x *= radial_factor;
            y *= radial_factor;
        }
        GL11.glEnd();
    }




}