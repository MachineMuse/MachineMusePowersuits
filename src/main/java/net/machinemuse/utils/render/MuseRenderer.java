package net.machinemuse.utils.render;

import net.machinemuse.general.gui.clickable.IClickable;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.geometry.SwirlyMuseCircle;
import net.machinemuse.numina.render.BillboardHelper;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Contains a bunch of random OpenGL-related functions, accessed statically.
 *
 * @author MachineMuse
 */
public abstract class MuseRenderer {

    protected static RenderItem renderItem;

    protected static SwirlyMuseCircle selectionCircle;

    public static final String ICON_PREFIX = "powersuits:";

    /**
     * Does the rotating green circle around the selection, e.g. in GUI.
     *
     * @param xoffset
     * @param yoffset
     * @param radius
     */
    public static void drawCircleAround(double xoffset, double yoffset, double radius) {
        if (selectionCircle == null) {
            selectionCircle = new SwirlyMuseCircle(new Colour(0.0f, 1.0f, 0.0f, 0.0f), new Colour(0.8f, 1.0f, 0.8f, 1.0f));
        }
        selectionCircle.draw(radius, xoffset, yoffset);
    }


    /**
     * Makes the appropriate openGL calls and draws an item and overlay using the default icon
     */
    public static void drawItemAt(double x, double y, ItemStack item) {
        RenderState.on2D();

        getRenderItem().renderItemAndEffectIntoGUI(getFontRenderer(), getRenderEngine(), item, (int) x, (int) y);
        getRenderItem().renderItemOverlayIntoGUI(getFontRenderer(), getRenderEngine(), item, (int) x, (int) y);

        RenderState.off2D();
    }

    static boolean messagedAboutSlick = false;

    public static void drawString(String s, double x, double y) {
        drawString(s, x, y, Colour.WHITE);
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string at the specified coords
     */
    public static void drawString(String s, double x, double y, Colour c) {
        RenderHelper.disableStandardItemLighting();
        RenderState.blendingOn();
        RenderState.on2D();
        getFontRenderer().drawStringWithShadow(s, (int) x, (int) y, c.getInt());
        RenderState.off2D();
        RenderState.blendingOff();
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string such that the xcoord is halfway through the string
     */
    public static void drawCenteredString(String s, double x, double y) {
        drawString(s, x - getStringWidth(s) / 2, y);
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string such that the xcoord is halfway through the string
     */
    public static void drawRightAlignedString(String s, double x, double y) {
        drawString(s, x - getStringWidth(s), y);
    }

    public static double getStringWidth(String s) {
        double val;
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        val = getFontRenderer().getStringWidth(s);
        GL11.glPopAttrib();
        return val;
    }

    public static void drawStringsJustified(List<String> words, double x1, double x2, double y) {
        int totalwidth = 0;
        for (String word : words) {
            totalwidth += getStringWidth(word);
        }

        double spacing = (x2 - x1 - totalwidth) / (words.size() - 1);

        double currentwidth = 0;
        for (String word : words) {
            MuseRenderer.drawString(word, x1 + currentwidth, y);
            currentwidth += getStringWidth(word) + spacing;
        }

    }

    /**
     * Draws a rectangular prism (cube or otherwise orthogonal)
     */
    public static void drawRectPrism(double x, double d, double e, double f, double z, double g, float texturex, float texturey, float texturex2,
                                     float texturey2) {
        RenderState.arraysOnT();
        RenderState.texturelessOff();
        Vec3[] points = {Vec3.createVectorHelper(x, e, z), Vec3.createVectorHelper(d, e, z), Vec3.createVectorHelper(x, f, z),
                Vec3.createVectorHelper(d, f, z), Vec3.createVectorHelper(x, e, g), Vec3.createVectorHelper(d, e, g),
                Vec3.createVectorHelper(x, f, g), Vec3.createVectorHelper(d, f, g)};
        PositionTextureVertex[] va1 = {new PositionTextureVertex(points[0], texturex, texturey2),
                new PositionTextureVertex(points[2], texturex2, texturey2), new PositionTextureVertex(points[3], texturex2, texturey),
                new PositionTextureVertex(points[1], texturex, texturey)

        };
        new TexturedQuad(va1).draw(Tessellator.instance, 1.0F);
        PositionTextureVertex[] va2 = {new PositionTextureVertex(points[2], texturex, texturey2),
                new PositionTextureVertex(points[6], texturex2, texturey2), new PositionTextureVertex(points[7], texturex2, texturey),
                new PositionTextureVertex(points[3], texturex, texturey)

        };
        new TexturedQuad(va2).draw(Tessellator.instance, 1.0F);
        PositionTextureVertex[] va3 = {new PositionTextureVertex(points[6], texturex, texturey2),
                new PositionTextureVertex(points[4], texturex2, texturey2), new PositionTextureVertex(points[5], texturex2, texturey),
                new PositionTextureVertex(points[7], texturex, texturey)

        };
        new TexturedQuad(va3).draw(Tessellator.instance, 1.0F);
        PositionTextureVertex[] va4 = {new PositionTextureVertex(points[4], texturex, texturey2),
                new PositionTextureVertex(points[0], texturex2, texturey2), new PositionTextureVertex(points[1], texturex2, texturey),
                new PositionTextureVertex(points[5], texturex, texturey)

        };
        new TexturedQuad(va4).draw(Tessellator.instance, 1.0F);
        PositionTextureVertex[] va5 = {new PositionTextureVertex(points[1], texturex, texturey2),
                new PositionTextureVertex(points[3], texturex2, texturey2), new PositionTextureVertex(points[7], texturex2, texturey),
                new PositionTextureVertex(points[5], texturex, texturey)

        };
        new TexturedQuad(va5).draw(Tessellator.instance, 1.0F);
        PositionTextureVertex[] va6 = {new PositionTextureVertex(points[0], texturex, texturey2),
                new PositionTextureVertex(points[4], texturex2, texturey2), new PositionTextureVertex(points[6], texturex2, texturey),
                new PositionTextureVertex(points[2], texturex, texturey)

        };
        new TexturedQuad(va6).draw(Tessellator.instance, 1.0F);
        // int[] indices = {
        // 0, 3, 1,
        // 0, 2, 3,
        // 2, 6, 7,
        // 2, 7, 3,
        // 6, 4, 5,
        // 6, 5, 7,
        // 4, 0, 1,
        // 4, 1, 5,
        // 1, 3, 7,
        // 1, 7, 5,
        // 0, 6, 2,
        // 0, 4, 6
        // };
        // drawTriangles3DT(points, textures, indices);
        RenderState.texturelessOff();
        RenderState.arraysOff();
    }

    /**
     * Singleton pattern for FontRenderer
     */
    public static FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    /**
     * Singleton pattern for RenderEngine
     */
    public static TextureManager getRenderEngine() {
        return Minecraft.getMinecraft().renderEngine;
    }

    /**
     * Singleton pattern for the RenderItem
     *
     * @return the static renderItem instance
     */
    public static RenderItem getRenderItem() {
        if (renderItem == null) {
            renderItem = new RenderItem();
        }
        return renderItem;
    }

    public static void drawLineBetween(IClickable firstClickable, IClickable secondClickable, Colour gradientColour) {
        long varia = System.currentTimeMillis() % 2000 - 1000; // ranges from
        // -1000 to 1000
        // and around,
        // period = 2
        // seconds
        double gradientRatio = 1.0 - ((varia + 1000) % 1000) / 1000.0;
        MusePoint2D midpoint = (firstClickable.getPosition().minus(secondClickable.getPosition()).times(Math.abs(varia / 1000.0))
                .plus(secondClickable.getPosition()));
        MusePoint2D firstpoint, secondpoint;
        if (varia < 0) {
            firstpoint = secondClickable.getPosition();
            secondpoint = firstClickable.getPosition();
        } else {
            firstpoint = firstClickable.getPosition();
            secondpoint = secondClickable.getPosition();
        }
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_LINES);
        gradientColour.withAlpha(gradientRatio).doGL();
        GL11.glVertex3d(midpoint.x(), midpoint.y(), 1);
        gradientColour.withAlpha(0.0).doGL();
        GL11.glVertex3d(firstpoint.x(), firstpoint.y(), 1);

        gradientColour.withAlpha(gradientRatio).doGL();
        GL11.glVertex3d(secondpoint.x(), secondpoint.y(), 1);
        Colour.WHITE.withAlpha(1.0).doGL();
        GL11.glVertex3d(midpoint.x(), midpoint.y(), 1);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawLightning(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        drawLightningTextured(x1, y1, z1, x2, y2, z2, colour);
    }

    public static void drawMPDLightning(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour, double displacement,
                                        double detail) {
        if (displacement < detail) {
            colour.doGL();
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x2, y2, z2);
            GL11.glEnd();
        } else {
            double mid_x = (x1 + x2) / 2.0;
            double mid_y = (y1 + y2) / 2.0;
            double mid_z = (z1 + z2) / 2.0;
            mid_x += (Math.random() - 0.5) * displacement;
            mid_y += (Math.random() - 0.5) * displacement;
            mid_z += (Math.random() - 0.5) * displacement;
            drawMPDLightning(x1, y1, z1, mid_x, mid_y, mid_z, colour, displacement / 2, detail);
            drawMPDLightning(mid_x, mid_y, mid_z, x2, y2, z2, colour, displacement / 2, detail);
        }
    }

    public static void drawLightningTextured(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        double tx = x2 - x1, ty = y2 - y1, tz = z2 - z1;

        double ax = 0, ay = 0, az = 0;
        double bx = 0, by = 0, bz = 0;
        double cx = 0, cy = 0, cz = 0;

        double jagfactor = 0.3;
        RenderState.on2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        MuseTextureUtils.pushTexture(Config.LIGHTNING_TEXTURE);
        RenderState.blendingOn();
        colour.doGL();
        GL11.glBegin(GL11.GL_QUADS);
        while (Math.abs(cx) < Math.abs(tx) && Math.abs(cy) < Math.abs(ty) && Math.abs(cz) < Math.abs(tz)) {
            ax = x1 + cx;
            ay = y1 + cy;
            az = z1 + cz;
            cx += Math.random() * tx * jagfactor - 0.1 * tx;
            cy += Math.random() * ty * jagfactor - 0.1 * ty;
            cz += Math.random() * tz * jagfactor - 0.1 * tz;
            bx = x1 + cx;
            by = y1 + cy;
            bz = z1 + cz;

            int index = (int) (Math.random() * 50);

            drawLightningBetweenPointsFast(ax, ay, az, bx, by, bz, index);
        }
        GL11.glEnd();
        RenderState.blendingOff();
        RenderState.off2D();
    }

    public static void drawLightningBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2, int index) {
        MuseTextureUtils.pushTexture(Config.LIGHTNING_TEXTURE);
        double u1 = index / 50.0;
        double u2 = u1 + 0.02;
        double px = (y1 - y2) * 0.125;
        double py = (x2 - x1) * 0.125;
        GL11.glTexCoord2d(u1, 0);
        GL11.glVertex3d(x1 - px, y1 - py, z1);
        GL11.glTexCoord2d(u2, 0);
        GL11.glVertex3d(x1 + px, y1 + py, z1);
        GL11.glTexCoord2d(u1, 1);
        GL11.glVertex3d(x2 - px, y2 - py, z2);
        GL11.glTexCoord2d(u2, 1);
        GL11.glVertex3d(x2 + px, y2 + py, z2);
        MuseTextureUtils.popTexture();
    }

    public static void drawLightningBetweenPointsFast(double x1, double y1, double z1, double x2, double y2, double z2, int index) {

        double u1 = index / 50.0;
        double u2 = u1 + 0.02;
        double px = (y1 - y2) * 0.125;
        double py = (x2 - x1) * 0.125;
        GL11.glTexCoord2d(u1, 0);
        GL11.glVertex3d(x1 - px, y1 - py, z1);
        GL11.glTexCoord2d(u2, 0);
        GL11.glVertex3d(x1 + px, y1 + py, z1);
        GL11.glTexCoord2d(u1, 1);
        GL11.glVertex3d(x2 - px, y2 - py, z2);
        GL11.glTexCoord2d(u2, 1);
        GL11.glVertex3d(x2 + px, y2 + py, z2);
    }

    public static void drawLightningLines(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        double tx = x2 - x1, ty = y2 - y1, tz = z2 - z1, cx = 0, cy = 0, cz = 0;
        double jagfactor = 0.3;
        RenderState.texturelessOn();
        RenderState.blendingOn();
        RenderState.on2D();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        while (Math.abs(cx) < Math.abs(tx) && Math.abs(cy) < Math.abs(ty) && Math.abs(cz) < Math.abs(tz)) {
            colour.doGL();
            // GL11.glLineWidth(1);
            cx += Math.random() * tx * jagfactor - 0.1 * tx;
            cy += Math.random() * ty * jagfactor - 0.1 * ty;
            cz += Math.random() * tz * jagfactor - 0.1 * tz;
            GL11.glVertex3d(x1 + cx, y1 + cy, z1 + cz);
            //
            // GL11.glLineWidth(3);
            // colour.withAlpha(0.5).doGL();
            // GL11.glVertex3d(ox, oy, oz);
            //
            // GL11.glLineWidth(5);
            // colour.withAlpha(0.1).doGL();
            // GL11.glVertex3d(x1 + cx, y1 + cy, z1 + cz);
        }
        GL11.glEnd();
        RenderState.off2D();
        RenderState.blendingOff();
        RenderState.texturelessOff();

    }

    public static void unRotate() {
        BillboardHelper.unRotate();
    }
}
