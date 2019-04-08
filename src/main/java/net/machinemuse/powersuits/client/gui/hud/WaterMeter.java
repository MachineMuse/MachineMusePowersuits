package net.machinemuse.powersuits.client.gui.hud;

import net.machinemuse.numina.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Fluids;


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
        return Minecraft.getInstance().getTextureMap().getAtlasSprite(Fluids.WATER.getStillFluid().toString());
    }
}
