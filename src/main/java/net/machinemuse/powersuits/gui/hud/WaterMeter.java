package net.machinemuse.powersuits.gui.hud;

import net.machinemuse.numina.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidRegistry;


/**
 * Created by User: Andrew2448
 * 4:30 PM 6/21/13
 */
public class WaterMeter extends HeatMeter {
    public double getAlpha() {
        return 1.0;
    }

    public Colour getColour() {
        return Colour.WHITE;
    }

    public TextureAtlasSprite getTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(FluidRegistry.WATER.getStill().toString());
    }
}
