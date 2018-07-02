package net.machinemuse.powersuits.client.gui.hud;

import net.machinemuse.numina.geometry.Colour;

/**
 * Created by leon on 4/9/17.
 */
public class PlasmaChargeMeter extends HeatMeter {
/*
  Comment any "get" method to use the method from HeatMeter.
  Uncomment to use the value in this file.
*/
/*
	public double getAlpha() {
		return 0.7;
	}
*/
	public Colour getColour() {
		//return Colour.WHITE;
		return Colour.LIGHTGREEN;
	}
/*
	public TextureAtlasSprite getTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/prismarine_rough");
		//return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/snow");
	}
*/
}