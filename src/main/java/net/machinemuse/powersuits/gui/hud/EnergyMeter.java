package net.machinemuse.powersuits.gui.hud;

import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.math.Colour;
import org.lwjgl.opengl.GL11;

public class EnergyMeter extends HeatMeter {
    public Colour getColour() {
        //return Colour.WHITE;
        return Colour.PURPLE;
    }

    public void draw(double xpos, double ypos, double value) {
        super.draw(xpos, ypos, value);

        GL11.glLineWidth(0.5f);
        if (value < 0.0001) {
            Colour.RED.doGL();
        } else if (Math.random() / value < 1) {
            RenderState.texturelessOn();
            MuseRenderer.drawMPDLightning(xpos + xsize * value, ypos + ysize * (Math.random() / 2 + 0.25), 1,
                    xpos, ypos + ysize * (Math.random() / 2 + 0.25), 1, Colour.WHITE, 4, 1);
            RenderState.texturelessOff();
        }
    }
}