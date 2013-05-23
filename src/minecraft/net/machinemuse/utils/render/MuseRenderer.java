package net.machinemuse.utils.render;

import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.SwirlyMuseCircle;
import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.general.gui.clickable.IClickable;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseMathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Contains a bunch of random OpenGL-related functions, accessed statically.
 *
 * @author MachineMuse
 */
public abstract class MuseRenderer {

    protected static RenderItem renderItem;

    protected static SwirlyMuseCircle selectionCircle;

    private static String TEXTURE_MAP = "/gui/items.png";
    private static Stack<String> texturestack = new Stack<String>();

    public static final String ITEM_TEXTURE_QUILT = "/gui/items.png";
    public static final String BLOCK_TEXTURE_QUILT = "/terrain.png";
    public static final String ICON_PREFIX = "mmmPowersuits:";

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

    public static void pushTexture(String filename) {
        texturestack.push(TEXTURE_MAP);
        TEXTURE_MAP = filename;
        getRenderEngine().bindTexture(TEXTURE_MAP);
    }

    public static void popTexture() {
        TEXTURE_MAP = texturestack.pop();
        getRenderEngine().bindTexture(TEXTURE_MAP);
    }

    /**
     * Creates a list of points linearly interpolated between points a and b noninclusive.
     *
     * @return A list of num points
     */
    public static List<MusePoint2D> pointsInLine(int num, MusePoint2D a, MusePoint2D b) {
        List<MusePoint2D> points = new ArrayList<MusePoint2D>();
        if (num < 1) {
            return points;
        } else if (num < 2) {
            points.add(b.minus(a).times(0.5F).plus(a));
        } else {
            MusePoint2D step = b.minus(a).times(1.0F / (num + 1));
            for (int i = 1; i < num + 1; i++) {
                points.add(a.plus(step.times(i)));
            }
        }

        return points;
    }

    /**
     * Returns a DoubleBuffer full of colours that are gradually interpolated
     *
     * @param c1
     * @param c2
     * @param numsegments
     * @return
     */
    public static DoubleBuffer getColourGradient(Colour c1, Colour c2, int numsegments) {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(numsegments * 4);
        for (double i = 0; i < numsegments; i++) {
            Colour c3 = c1.interpolate(c2, i / numsegments);
            buffer.put(c3.r);
            buffer.put(c3.g);
            buffer.put(c3.b);
            buffer.put(c3.a);
        }
        buffer.flip();
        return buffer;
    }

    /**
     * Efficient algorithm for drawing circles and arcs in pure opengl!
     *
     * @param startangle Start angle in radians
     * @param endangle   End angle in radians
     * @param radius     Radius of the circle (used in calculating number of segments to draw as well as size of the arc)
     * @param xoffset    Convenience parameter, added to every vertex
     * @param yoffset    Convenience parameter, added to every vertex
     * @param zoffset    Convenience parameter, added to every vertex
     * @return
     */
    public static DoubleBuffer getArcPoints(double startangle, double endangle, double radius, double xoffset, double yoffset, double zoffset) {
        // roughly 8 vertices per Minecraft 'pixel' - should result in at least
        // 2 vertices per real pixel on the screen.
        int numVertices = (int) Math.ceil(Math.abs((endangle - startangle) * 16 * Math.PI));
        double theta = (endangle - startangle) / numVertices;
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(numVertices * 3);

        double x = radius * Math.sin(startangle);
        double y = radius * Math.cos(startangle);
        double tf = Math.tan(theta); // precompute tangent factor: how much to
        // move along the tangent line each
        // iteration
        double rf = Math.cos(theta); // precompute radial factor: how much to
        // move back towards the origin each
        // iteration
        double tx;
        double ty;

        for (int i = 0; i < numVertices; i++) {
            buffer.put(x + xoffset);
            buffer.put(y + yoffset);
            buffer.put(zoffset);
            tx = y; // compute tangent lines
            ty = -x;
            x += tx * tf; // add tangent line * tangent factor
            y += ty * tf;
            x *= rf;
            y *= rf;
        }
        buffer.flip();
        return buffer;
    }

    /**
     * 2D rendering mode on/off
     */

    public static void on2D() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);

        // attempt at fake antialiasing
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
        // GL11.glColorMask(false, false, false, true);
        // GL11.glClearColor(0, 0, 0, 0);
        // GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // GL11.glColorMask(true, true, true, true);

        // GL11.glHint(GL11.GL_POINT_SMOOTH, GL11.GL_NICEST);
        // GL11.glHint(GL11.GL_LINE_SMOOTH, GL11.GL_NICEST);
        // GL11.glHint(GL11.GL_POLYGON_SMOOTH, GL11.GL_NICEST);
        // GL11.glDepthFunc(GL11.GL_GREATER);
    }

    public static void off2D() {
        GL11.glPopAttrib();
    }

    /**
     * Arrays on/off
     */

    public static void arraysOnC() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        // GL11.glEnableClientState(GL11.GL_INDEX_ARRAY);
    }

    public static void arraysOnT() {
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
            // GL11.glEnable(GL11.GL_LINE_SMOOTH);
            // GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Call after doing anything with alpha blending
     */
    public static void blendingOff() {
        GL11.glPopAttrib();
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void scissorsOn(double x, double y, double w, double h) {
//        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();

        int dw = mc.displayWidth;
        int dh = mc.displayHeight;
        ScaledResolution res = new ScaledResolution(mc.gameSettings, dw, dh);

        double newx = x * res.getScaleFactor();
        double newy = dh - h * res.getScaleFactor() - y * res.getScaleFactor();
        double neww = w * res.getScaleFactor();
        double newh = h * res.getScaleFactor();

//        GL11.glTranslated(-x, -y, 0);
//        GL11.glScaled(dw/(double)neww, dh/(double)newh, 1);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) newx, (int) newy, (int) neww, (int) newh);
    }

    public static void scissorsOff() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    /**
     * Makes the appropriate openGL calls and draws an item and overlay using the default icon
     */
    public static void drawItemAt(double x, double y, ItemStack item) {
        on2D();

        getRenderItem().renderItemAndEffectIntoGUI(getFontRenderer(), getRenderEngine(), item, (int) x, (int) y);
        getRenderItem().renderItemOverlayIntoGUI(getFontRenderer(), getRenderEngine(), item, (int) x, (int) y);

        off2D();
    }

    /**
     * Draws a MuseIcon
     *
     * @param x
     * @param y
     * @param icon
     * @param colour
     */
    public static void drawIconAt(double x, double y, Icon icon, Colour colour) {
        drawIconPartial(x, y, icon, colour, 0, 0, 16, 16);
    }


    public static void drawIconPartialOccluded(double x, double y, Icon icon, Colour colour, double left, double top, double right, double bottom) {
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
    public static void drawIconPartial(double x, double y, Icon icon, Colour colour, double left, double top, double right, double bottom) {
        if (icon == null) {
            return;
        }
        GL11.glPushMatrix();
        on2D();
        blendingOn();

        if (colour != null) {
            colour.doGL();
        }
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        float u1 = icon.getMinU();
        float v1 = icon.getMinV();
        float u2 = icon.getMaxU();
        float v2 = icon.getMaxV();
        double xoffset1 = left * (u2 - u1) / 16.0f;
        double yoffset1 = top * (v2 - v1) / 16.0f;
        double xoffset2 = right * (u2 - u1) / 16.0f;
        double yoffset2 = bottom * (v2 - v1) / 16.0f;

        tess.addVertexWithUV(x + left, y + top, 0, u1 + xoffset1, v1 + yoffset1);
        tess.addVertexWithUV(x + left, y + bottom, 0, u1 + xoffset1, v1 + yoffset2);
        tess.addVertexWithUV(x + right, y + bottom, 0, u1 + xoffset2, v1 + yoffset2);
        tess.addVertexWithUV(x + right, y + top, 0, u1 + xoffset2, v1 + yoffset1);
        tess.draw();

        MuseRenderer.blendingOff();
        off2D();
        GL11.glPopMatrix();
    }

    /**
     * Switches to relative coordinate frame (where -1,-1 is top left of the working area, and 1,1 is the bottom right)
     */
    public static void relativeCoords(MuseGui gui) {
        GL11.glPushMatrix();
        GL11.glTranslatef(gui.width / 2, gui.height / 2, 0);
        GL11.glScalef(gui.getxSize(), gui.getySize(), 0);
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
        blendingOn();
        on2D();
        if (Config.useCustomFonts()) {
            try {
                SlickFont.apply(x, y, s, c);
            } catch (Throwable e) {
                if (!messagedAboutSlick) {
                    MuseLogger.logError("Slick-Util failed or was disabled in config!");
                    e.printStackTrace();
                    messagedAboutSlick = true;
                }
                getFontRenderer().drawStringWithShadow(s, (int) x, (int) y, c.getInt());
            }
        } else {
            getFontRenderer().drawStringWithShadow(s, (int) x, (int) y, c.getInt());
        }

        off2D();
        blendingOff();
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
        try {
            if (!Config.useCustomFonts()) throw new UnsupportedOperationException();
            val = SlickFont.getStringWidth(s);
        } catch (Throwable e) {
            val = getFontRenderer().getStringWidth(s);
        } finally {
            GL11.glPopAttrib();
        }
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
        arraysOnT();
        texturelessOff();
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
        texturelessOff();
        arraysOff();
    }


    private static float lightmapLastX;
    private static float lightmapLastY;

    public static void glowOn() {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        lightmapLastX = OpenGlHelper.lastBrightnessX;
        lightmapLastY = OpenGlHelper.lastBrightnessY;
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void glowOff() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapLastX, lightmapLastY);
        GL11.glPopAttrib();
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
    public static RenderEngine getRenderEngine() {
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
        on2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        pushTexture(Config.LIGHTNING_TEXTURE);
        blendingOn();
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

            int index = (int) Math.random() * 50;

            drawLightningBetweenPointsFast(ax, ay, az, bx, by, bz, index);
        }
        GL11.glEnd();
        blendingOff();
        off2D();
    }

    public static void drawLightningBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2, int index) {
        pushTexture(Config.LIGHTNING_TEXTURE);
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
        popTexture();
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
        texturelessOn();
        blendingOn();
        on2D();
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
        off2D();
        blendingOff();
        texturelessOff();

    }

    private static float pythag(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static void unRotate() {
        FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matrix);
        float scalex = pythag(matrix.get(0), matrix.get(1), matrix.get(2));
        float scaley = pythag(matrix.get(4), matrix.get(5), matrix.get(6));
        float scalez = pythag(matrix.get(8), matrix.get(9), matrix.get(10));
        for (int i = 0; i < 12; i++) {
            matrix.put(i, 0);
        }
        matrix.put(0, scalex);
        matrix.put(5, scaley);
        matrix.put(10, scalez);
        GL11.glLoadMatrix(matrix);
    }

}