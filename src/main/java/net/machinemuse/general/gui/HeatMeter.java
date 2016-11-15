package net.machinemuse.general.gui;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseIconUtils;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class HeatMeter {
    final int xsize = 8;
    final int ysize = 32;

    public void draw(double xpos, double ypos, double value) {
        MuseTextureUtils.pushTexture(MuseTextureUtils.BLOCK_TEXTURE_QUILT);
        RenderState.blendingOn();
        RenderState.on2D();
        IIcon icon = Blocks.lava.getIcon(0, 0);
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
    }

    public void drawFluid(double xpos, double ypos, double value, IIcon icon) {

        double bottomY = (ypos + ysize);
        double topY = (ypos + ysize * (1 - value));
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        while (bottomY - 8 > topY) {
            MuseIconUtils.drawIconAt(xpos * 2, (bottomY - 8) * 2, icon, Colour.WHITE);
            bottomY -= 8;
        }
        MuseIconUtils.drawIconPartial(xpos * 2, (bottomY - 8) * 2, icon, Colour.WHITE, 0, (topY - bottomY + 8) * 2, 16, 16);
        GL11.glPopMatrix();
    }

    public void drawGlass(double xpos, double ypos) {
        MuseTextureUtils.pushTexture(Config.GLASS_TEXTURE);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(xpos, ypos);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(xpos + xsize, ypos + ysize);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glEnd();
        MuseTextureUtils.popTexture();
    }
}
