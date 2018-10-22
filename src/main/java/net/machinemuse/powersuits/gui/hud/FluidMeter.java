package net.machinemuse.powersuits.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;

public class FluidMeter extends HeatMeter {
    Fluid fluid;

    public FluidMeter(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());
    }
}
