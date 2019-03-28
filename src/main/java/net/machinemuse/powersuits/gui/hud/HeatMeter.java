package net.machinemuse.powersuits.gui.hud;

import net.machinemuse.numina.client.render.MuseIconUtils;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;

public class HeatMeter {
    final int xsize = 32;
    final int ysize = 8;
    double meterStart, meterLevel, alpha;

    public double getAlpha() {
        return 0.7;
    }

    public Colour getColour() {
        return Colour.RED;
    }

    public TextureAtlasSprite getTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/snow");
    }

    public void draw(double xpos, double ypos, double value) {
        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        RenderState.blendingOn();
        RenderState.on2D();
        drawFluid(xpos, ypos, value, getTexture());
        drawGlass(xpos, ypos);
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
    }

    public void drawFluid(double xpos, double ypos, double value, TextureAtlasSprite icon) {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);

        // New: Horizontal, fill from left.
        meterStart = xpos;
        meterLevel = (xpos + xsize * value);

        while (meterStart + 8 < meterLevel) {
            MuseIconUtils.drawIconAt(meterStart * 2, ypos * 2, icon, getColour().withAlpha(getAlpha()));
            meterStart += 8;
        }
        MuseIconUtils.drawIconPartial(meterStart * 2, ypos * 2, icon, getColour().withAlpha(getAlpha()), 0, 0, (meterLevel - meterStart) * 2, 16);
        GL11.glPopMatrix();
    }

    public void drawGlass(double xpos, double ypos) {
        MuseTextureUtils.pushTexture(MPSResourceConstants.GLASS_TEXTURE);
        Colour.WHITE.doGL();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(xpos, ypos);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(xpos + xsize, ypos + ysize);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glEnd();
        MuseTextureUtils.popTexture();
    }
}