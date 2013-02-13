package net.machinemuse.powersuits.client.render;

import java.nio.DoubleBuffer;

import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.DrawableMuseCircle;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlasmaBolt extends Render {
	protected static DrawableMuseCircle circle1;
	protected static DrawableMuseCircle circle2;
	protected static DrawableMuseCircle circle3;
	protected static DrawableMuseCircle circle4;

	public RenderPlasmaBolt() {
		Colour c1 = new Colour(.3, .3, 1, 0.3);
		circle1 = new DrawableMuseCircle(c1, c1);
		c1 = new Colour(.3, .3, 1, 0.6);
		circle2 = new DrawableMuseCircle(c1, c1);
		c1 = new Colour(.3, .3, 1, 1);
		circle3 = new DrawableMuseCircle(c1, c1);
		circle4 = new DrawableMuseCircle(c1, new Colour(1, 1, 1, 1));
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		EntityPlasmaBolt bolt = (EntityPlasmaBolt) entity;
		double size = (bolt.size) / 10.0;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F,
				0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		doRender(size);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public static DoubleBuffer unrotatebuffer;

	public static DoubleBuffer getUnrotateBuffer() {
		if (unrotatebuffer == null) {
			unrotatebuffer = BufferUtils.createDoubleBuffer(16);
			unrotatebuffer.put(new double[] {
					0, 0, 0, 0,
					0, 0, 0, 0,
					0, 0, 0, 0,
					0, 0, 0, 1
			});
			unrotatebuffer.flip();
		}
		unrotatebuffer.rewind();
		return unrotatebuffer;
	}

	public static void unRotate() {
		GL11.glMultMatrix(getUnrotateBuffer());
	}

	public static void doRender(double size) {
		GL11.glPushMatrix();
		// unRotate();
		double scale = size / 16.0;
		GL11.glScaled(scale, scale, scale);
		int millisPerCycle = 500;
		double timeScale = Math.cos((System.currentTimeMillis() % millisPerCycle) * 2.0 / millisPerCycle - 1.0);
		MuseRenderer.glowOn();
		circle1.draw(4, 0, 0);
		GL11.glTranslated(0, 0, 0.001);
		circle2.draw(3 + timeScale / 2, 0, 0);
		GL11.glTranslated(0, 0, 0.001);
		circle3.draw(2 + timeScale, 0, 0);
		GL11.glTranslated(0, 0, 0.001);
		circle4.draw(1 + timeScale, 0, 0);
		for (int i = 0; i < 3; i++) {
			double angle1 = (Math.random() * 2 * Math.PI);
			double angle2 = (Math.random() * 2 * Math.PI);
			MuseRenderer.drawLightning(Math.cos(angle1) * 5, Math.sin(angle1) * 5, 2, Math.cos(angle2) * 5, Math.sin(angle2) * 5, 3, new Colour(1, 1,
					1, 1));
		}
		GL11.glPopMatrix();
	}
}
