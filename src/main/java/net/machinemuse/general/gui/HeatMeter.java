package net.machinemuse.general.gui;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseIconUtils;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class HeatMeter {
//    final int xsize = 8; // width
//    final int ysize = 32; // height

    final int xsize = 32;
    final int ysize = 8;
    private static final ResourceLocation iconLocation = new ResourceLocation("minecraft", "blocks/magma");

    public void draw(double xpos, double ypos, double value) {
        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        RenderState.blendingOn();
        RenderState.on2D();
        TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/magma");
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
    }

    public void drawFluid(double xpos, double ypos, double value, TextureAtlasSprite icon) {
        double meterStart, meterLevel;
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);

    /*
	//Original: Vertical, fill from bottom.
	meterStart = (ypos + ysize);
	meterLevel = (ypos + ysize * (1 - value));
		while (meterStart - 8 > meterLevel) {
			MuseIconUtils.drawIconAt(xpos * 2, (meterStart - 8) * 2, icon, Colour.WHITE);
			meterStart -= 8;
		}
	MuseIconUtils.drawIconPartial(xpos * 2, (meterStart - 8) * 2, icon, Colour.WHITE, 0, (meterLevel - meterStart + 8) * 2, 16, 16);
*/
        // New: Horizontal, fill from left.
        meterStart = xpos;
        meterLevel = (xpos + xsize * value);
        while (meterStart + 8 < meterLevel) {
            MuseIconUtils.drawIconAt(meterStart * 2, ypos * 2, icon, Colour.WHITE);
            meterStart += 8;
        }
        MuseIconUtils.drawIconPartial(meterStart * 2, ypos * 2, icon, Colour.WHITE, 0, 0, (meterLevel - meterStart) * 2, 16);
        GL11.glPopMatrix();
    }

    public void drawGlass(double xpos, double ypos) {
        MuseTextureUtils.pushTexture(Config.GLASS_TEXTURE);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(xpos, ypos);
        GL11.glTexCoord2d(0, 1);
        //      GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(xpos + xsize, ypos + ysize);
        GL11.glTexCoord2d(1, 0);
        //      GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glEnd();
        MuseTextureUtils.popTexture();
    }
}