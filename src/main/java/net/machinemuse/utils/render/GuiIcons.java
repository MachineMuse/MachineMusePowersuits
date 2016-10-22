package net.machinemuse.utils.render;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseIconUtils;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 AM, 30/04/13
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class GuiIcons {
    private GuiIcons() {
    }

    public GuiIcons INSTANCE = new GuiIcons();


    public interface IGuiIcon {
        double size = 0;
        String filepath = null;

        double x = 0;
        double y = 0;
        Colour c = null;
        double xmin = 0;
        double ymin = 0;
        double xmax = 0;
        double yma = 0;
    }

    public static class GuiIcon implements IGuiIcon {
        final double size;
        final String filepath;

        final double x;
        final double y;
        final Colour c;
        final double xmin;
        final double ymin;
        final double xmax;
        final double ymax;

        public GuiIcon (double size, String filepath, double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            this.size = size;
            this.filepath = filepath;
            this.x = x;
            this.y = y;
            this.c = (c != null) ? c : Colour.WHITE;
            this.xmin = (xmin != null) ? xmin : Integer.MIN_VALUE;
            this.ymin = (ymin != null) ? ymin : Integer.MIN_VALUE;
            this.xmax = (xmax != null) ? xmax : Integer.MAX_VALUE;
            this.ymax = (ymax != null) ? ymax : Integer.MAX_VALUE;

            MuseTextureUtils.pushTexture(filepath);
            GL11.glPushMatrix();
            RenderState.blendingOn();
            double s = size / 16.0;
            GL11.glScaled(s, s, s);

            MuseIconUtils.drawIconPartialOccluded(x / s, y / s, new GuiIconDrawer(), this.c, this.xmin / s, this.ymin / s, this.xmax / s, this.ymax / s);
            RenderState.blendingOff();
            GL11.glPopMatrix();
            MuseTextureUtils.popTexture();
        }
    }

    public static class Checkmark extends GuiIcon {
//        double size = 16.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/checkmark.png";

        public Checkmark(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(16.0, Config.TEXTURE_PREFIX() + "gui/checkmark.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class TransparentArmor extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/transparentarmor.png";

        public TransparentArmor(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/transparentarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class NormalArmor extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/normalarmor.png";

        public NormalArmor(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/normalarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class GlowArmor extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/glowarmor.png";

        public GlowArmor(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/glowarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class SelectedArmorOverlay extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/armordisplayselect.png";

        public SelectedArmorOverlay(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/armordisplayselect.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class ArmourColourPatch extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/colourclicker.png";

        public ArmourColourPatch(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/colourclicker.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class MinusSign extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/minussign.png";

        public MinusSign(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/minussign.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class PlusSign extends GuiIcon {
//        double size = 8.0;
//        String filepath = Config.TEXTURE_PREFIX() + "gui/plussign.png";

        public PlusSign(double x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, Config.TEXTURE_PREFIX() + "gui/plussign.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class GuiIconDrawer implements IIcon {
        @Override
        public int getIconWidth() {
            return 8;
        }

        @Override
        public int getIconHeight() {
            return 8;
        }

        @Override
        public float getMinU() {
            return 0;
        }

        @Override
        public float getMaxU() {
            return 1;
        }

        @Override
        public float getInterpolatedU(double d0) {
            return (float) d0;
        }

        @Override
        public float getMinV() {
            return 0;
        }

        @Override
        public float getMaxV() {
            return 1;
        }

        @Override
        public float getInterpolatedV(double d0) {
            return (float) d0;
        }

        @Override
        public String getIconName() {
            return "GuiIcon";
        }

        public int getOriginX() {
            return 0;
        }

        public int getOriginY() {
            return 0;
        }
    }
}