package net.machinemuse.general.geometry;

import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.util.Icon;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.BufferOverflowException;
import java.nio.DoubleBuffer;

/**
 * A curved indicator bar. Probably not gonna use this actually...
 *
 * @author MachineMuse
 */
public class RadialIndicator {
    protected DoubleBuffer vertices;
    protected DoubleBuffer textures;
    protected DoubleBuffer background;
    protected DoubleBuffer foreground;
    protected Icon icon;
    protected String textureFile;

    public RadialIndicator(double innerRadius, double outerRadius, double startangle, double endangle, Colour backgroundColour,
                           Colour foregroundColour,
                           Icon icon, String texturefile) {
        this.icon = icon;
        this.textureFile = texturefile;
        DoubleBuffer arcPoints1 = MuseRenderer.getArcPoints(startangle, endangle, outerRadius, 0, 0, 1);
        DoubleBuffer arcPoints2 = MuseRenderer.getArcPoints(startangle, endangle, innerRadius, 0, 0, 1);
        int numpoints = (arcPoints1.limit() + arcPoints2.limit()) / 3;
        // MuseLogger.logDebug("Limit1: " + arcPoints1.limit() + " - Limit2: " + arcPoints2.limit() + " - " + numpoints);
        vertices = BufferUtils.createDoubleBuffer(numpoints * 3);
        while (arcPoints1.hasRemaining() && arcPoints2.hasRemaining()) {
            vertices.put(arcPoints1.get());
            vertices.put(arcPoints1.get());
            vertices.put(arcPoints1.get());
            vertices.put(arcPoints2.get());
            vertices.put(arcPoints2.get());
            vertices.put(arcPoints2.get());
        }
        vertices.flip();

        float u1 = icon.getMinU();
        float v1 = icon.getMinV();
        float u2 = icon.getMaxU();
        float v2 = icon.getMaxV();

        textures = BufferUtils.createDoubleBuffer(numpoints * 2);
        int numsegments = (numpoints / 4);
        for (double i = 0; i < numsegments; i++) {
            double offset = (v2 - v1) * i / numsegments;
            textures.put(u1);
            textures.put(v1 + offset);
            textures.put(u2);
            textures.put(v1 + offset);
        }
        for (double i = 0; i < numsegments; i++) {
            double offset = (v2 - v1) * i / numsegments;
            textures.put(u1);
            textures.put(v1 + offset);
            textures.put(u2);
            textures.put(v1 + offset);
        }
        textures.flip();

        background = BufferUtils.createDoubleBuffer(numpoints * 4);
        try {
            while (true) {
                background.put(backgroundColour.asArray());
            }
        } catch (BufferOverflowException e) {
        }
        background.flip();

        foreground = BufferUtils.createDoubleBuffer(numpoints * 4);
        try {
            while (true) {
                foreground.put(foregroundColour.asArray());
            }
        } catch (BufferOverflowException e) {
        }
        foreground.flip();

    }

    public void draw(double x, double y, double percent) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        MuseRenderer.texturelessOff();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        MuseRenderer.blendingOn();
        GL11.glTranslated(x, y, 0);

        MuseRenderer.arraysOff();
        int pointsToDraw = (int) (percent * textures.limit() / 2);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glVertexPointer(3, 0, vertices);
        GL11.glColorPointer(4, 0, background);

        // Draw background
        GL11.glDrawArrays(GL11.GL_QUAD_STRIP, 0, textures.limit() / 2);

        MuseRenderer.pushTexture(textureFile);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL11.glColorPointer(4, 0, foreground);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glTexCoordPointer(2, 0, textures);
        GL11.glDrawArrays(GL11.GL_QUAD_STRIP, 0, pointsToDraw);

        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        MuseRenderer.arraysOff();
        MuseRenderer.popTexture();

        MuseRenderer.blendingOff();
        GL11.glEnable(GL11.GL_CULL_FACE);
        // GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }
}
