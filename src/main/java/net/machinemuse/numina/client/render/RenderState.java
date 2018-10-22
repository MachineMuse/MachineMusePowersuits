package net.machinemuse.numina.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:41 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/23/16.
 */
public final class RenderState {
    /**
     * Used primarily for model rendering to make a surface "glow"
     */
    private static float lightmapLastX = .0f;
    private static float lightmapLastY = .0f;

    /**
     * 2D rendering mode on/off
     */
    public static void on2D() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public static void off2D() {
        GL11.glPopAttrib();
    }

    /**
     * Arrays on/off
     */
    public static void arraysOnColor() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    public static void arraysOnTexture() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public static void arraysOff() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * Call before doing any pure geometry (ie. with colours rather than textures).
     */
    public static void texturelessOn() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Call after doing pure geometry (ie. with colours) to go back to the texture mode (default).
     */
    public static void texturelessOff() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Call before doing anything with alpha blending
     */
    public static void blendingOn() {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
        if (Minecraft.isFancyGraphicsEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Call after doing anything with alpha blending
     */
    public static void blendingOff() {
        GL11.glPopAttrib();
    }

    public static void scissorsOn(double x, double y, double w, double h) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_SCISSOR_BIT);
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        int dw = mc.displayWidth;
        int dh = mc.displayHeight;
        ScaledResolution res = new ScaledResolution(mc);
        double newx = x * res.getScaleFactor();
        double newy = dh - h * res.getScaleFactor() - y * res.getScaleFactor();
        double neww = w * res.getScaleFactor();
        double newh = h * res.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) newx, (int) newy, (int) neww, (int) newh);
    }

    public static void scissorsOff() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void glowOn() {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        lightmapLastX = OpenGlHelper.lastBrightnessX;
        lightmapLastY = OpenGlHelper.lastBrightnessY;
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void glowOff() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapLastX, lightmapLastY);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopAttrib();
    }
}