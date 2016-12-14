package net.machinemuse.utils.render;

import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:00 PM, 5/15/13
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class MuseStencilManager {
    final int stencilMask = 0x10;

    public void stencilOn() {
        GL11.glStencilMask(stencilMask);
        GL11.glEnable(GL11.GL_STENCIL);
    }

    public void stencilOff() {
    }
}
