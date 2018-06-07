package net.machinemuse.general.gui;

import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.utils.render.MuseRenderer;
import org.lwjgl.opengl.GL11;

public class EnergyMeter extends HeatMeter {
/*
  Comment any "get" method to use the method from HeatMeter.
  Uncomment to use the value in this file.
*/
/*
	public double getAlpha() {
		return 0.6;
	}
*/
	public Colour getColour() {
		//return Colour.WHITE;
		return Colour.PURPLE;
	}
/*	
	public TextureAtlasSprite getTexture() {
		//return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/portal");
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/snow");
	}
*/
    public void draw(double xpos, double ypos, double value) {
		super.draw(xpos, ypos, value);

        GL11.glLineWidth(0.5f);
        if (value < 0.0001) {
            Colour.RED.doGL();
        } else if (Math.random() / value < 1) {
            RenderState.texturelessOn();
//			MuseRenderer.drawMPDLightning(xpos + xsize * (Math.random() / 2 + 0.25), ypos + ysize * (1 - value), 1, 
//					xpos + xsize * (Math.random() / 2 + 0.25), ypos + ysize, 1, Colour.WHITE, 4, 1);
			MuseRenderer.drawMPDLightning(xpos + xsize * value, ypos + ysize * (Math.random() / 2 + 0.25), 1, 
					xpos, ypos + ysize * (Math.random() / 2 + 0.25), 1, Colour.WHITE, 4, 1);
            RenderState.texturelessOff();
        }
    }
}