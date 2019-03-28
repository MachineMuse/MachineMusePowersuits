package net.machinemuse.numina.client.render;

import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.MuseMathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:37 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
public class MuseIconUtils {
    /**
     * Draws a MuseIcon
     *
     * @param x
     * @param y
     * @param icon
     * @param colour
     */
    public static void drawIconAt(double x, double y, TextureAtlasSprite icon, Colour colour) {
        drawIconPartial(x, y, icon, colour, 0, 0, 16, 16);
    }

    public static void drawIconPartialOccluded(double x, double y, TextureAtlasSprite icon, Colour colour, double left, double top, double right, double bottom) {
        double xmin = MuseMathUtils.clampDouble(left - x, 0, 16);
        double ymin = MuseMathUtils.clampDouble(top - y, 0, 16);
        double xmax = MuseMathUtils.clampDouble(right - x, 0, 16);
        double ymax = MuseMathUtils.clampDouble(bottom - y, 0, 16);
        drawIconPartial(x, y, icon, colour, xmin, ymin, xmax, ymax);
    }

    /**
     * Draws a MuseIcon
     *
     * @param x
     * @param y
     * @param icon
     * @param colour
     */
    public static void drawIconPartial(double x, double y, TextureAtlasSprite icon, Colour colour, double left, double top, double right, double bottom) {
        TextureAtlasSprite icon1 = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        if (icon != null)
            icon1 = icon;

        GL11.glPushMatrix();
        RenderState.on2D();
        RenderState.blendingOn();
        if (colour != null) {
            colour.doGL();
        }

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tess.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float u1 = icon1.getMinU();
        float v1 = icon1.getMinV();
        float u2 = icon1.getMaxU();
        float v2 = icon1.getMaxV();

        double xoffset1 = left * (u2 - u1) / 16.0f;
        double yoffset1 = top * (v2 - v1) / 16.0f;
        double xoffset2 = right * (u2 - u1) / 16.0f;
        double yoffset2 = bottom * (v2 - v1) / 16.0f;

        bufferBuilder.pos(x + left, y + top, 0);
        bufferBuilder.tex(u1 + xoffset1, v1 + yoffset1);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + left, y + bottom, 0);
        bufferBuilder.tex(u1 + xoffset1, v1 + yoffset2);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + right, y + bottom, 0);
        bufferBuilder.tex(u1 + xoffset2, v1 + yoffset2);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + right, y + top, 0);
        bufferBuilder.tex(u1 + xoffset2, v1 + yoffset1);
        bufferBuilder.endVertex();

        tess.draw();
        RenderState.blendingOff();
        RenderState.off2D();
        GL11.glPopMatrix();
    }
}