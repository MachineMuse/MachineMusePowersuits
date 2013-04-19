package net.machinemuse.powersuits.client.render;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class RenderLuxCapacitorEntity extends Render {
	protected static WavefrontObject lightmodel;
	protected static WavefrontObject framemodel;

	public static WavefrontObject getLightModel() {
		if (lightmodel == null) {
			lightmodel = (WavefrontObject) AdvancedModelLoader.loadModel(Config.RESOURCE_PREFIX + "models/lightCore.obj");
		}
		return lightmodel;
	}

	public static WavefrontObject getFrameModel() {
		if (framemodel == null) {
			framemodel = (WavefrontObject) AdvancedModelLoader.loadModel(Config.RESOURCE_PREFIX + "models/lightBase.obj");
		}
		return framemodel;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		MuseRenderer.getRenderEngine().bindTexture(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
		glPushMatrix();
		glTranslated(x + 0.5, y + 0.5, z + 0.5);
		double scale = 0.0625;
		glScaled(scale, scale, scale);
		getFrameModel().renderAll();
		MuseRenderer.glowOn();
		getLightModel().renderAll();
		MuseRenderer.glowOff();
		glPopMatrix();
	}

}
