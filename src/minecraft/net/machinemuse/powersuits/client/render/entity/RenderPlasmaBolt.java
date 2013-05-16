package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.DrawableMuseCircle;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.DoubleBuffer;

public class RenderPlasmaBolt extends Render {
    protected static DrawableMuseCircle circle1;
    protected static DrawableMuseCircle circle2;
    protected static DrawableMuseCircle circle3;
    protected static DrawableMuseCircle circle4;

    public RenderPlasmaBolt() {
        Colour c1 = new Colour(.3, .3, 1, 0.3);
        circle1 = new DrawableMuseCircle(c1, c1);
        c1 = new Colour(.3, .3, 1, 0.6);
        circle2 = new DrawableMuseCircle(c1, c1);
        c1 = new Colour(.3, .3, 1, 1);
        circle3 = new DrawableMuseCircle(c1, c1);
        circle4 = new DrawableMuseCircle(c1, new Colour(1, 1, 1, 1));
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void doRender(T entity, double d, double d1, double d2, float f, float
     * f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        EntityPlasmaBolt bolt = (EntityPlasmaBolt) entity;
        double size = (bolt.size) / 10.0;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        doRender(size);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public static DoubleBuffer unrotatebuffer;

    public static void doRender(double size) {
        GL11.glPushMatrix();
        MuseRenderer.unRotate();
        double scale = size / 16.0;
        GL11.glScaled(scale, scale, scale);
        int millisPerCycle = 500;
        double timeScale = Math.cos((System.currentTimeMillis() % millisPerCycle) * 2.0 / millisPerCycle - 1.0);
        MuseRenderer.glowOn();
        circle1.draw(4, 0, 0);
        GL11.glTranslated(0, 0, 0.001);
        circle2.draw(3 + timeScale / 2, 0, 0);
        GL11.glTranslated(0, 0, 0.001);
        circle3.draw(2 + timeScale, 0, 0);
        GL11.glTranslated(0, 0, 0.001);
        circle4.draw(1 + timeScale, 0, 0);
        for (int i = 0; i < 3; i++) {
            double angle1 = (Math.random() * 2 * Math.PI);
            double angle2 = (Math.random() * 2 * Math.PI);
            MuseRenderer.drawLightning(Math.cos(angle1) * 0.5, Math.sin(angle1) * 0.5, 0, Math.cos(angle2) * 5, Math.sin(angle2) * 5, 1,
                    new Colour(1, 1, 1, 0.9));
        }
        MuseRenderer.glowOff();
        GL11.glPopMatrix();
    }
}
