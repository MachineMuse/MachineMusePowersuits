package net.machinemuse.general.gui;

import net.machinemuse.numina.geometry.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidRegistry;


/**
 * Created by User: Andrew2448
 * 4:30 PM 6/21/13
 */
public class WaterMeter extends HeatMeter {
/*
  Comment any "get" method to use the method from HeatMeter.
  Uncomment to use the value in this file.
*/

	public double getAlpha() {
		return 1.0;
	}

	public Colour getColour() {
		return Colour.WHITE;
		//return Colour.LIGHTBLUE;
	}

	public TextureAtlasSprite getTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(FluidRegistry.WATER.getStill().toString());
		//return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/snow");
	}

}
