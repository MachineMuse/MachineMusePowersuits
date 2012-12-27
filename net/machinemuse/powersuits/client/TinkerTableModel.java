package net.machinemuse.powersuits.client;

import java.util.Random;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class TinkerTableModel extends Render {
	// public float onGround;
	// public boolean isRiding = false;
	protected ModelBase model = new ModelBase() {
	};

	private static final Random random = new Random();
	/**
	 * This is a list of all the boxes (ModelRenderer.class) in the current
	 * model.
	 */
	// public List boxList = new ArrayList();
	// public boolean isChild = true;

	/** A mapping for all texture offsets */
	// private Map modelTextureMap = new HashMap();
	// public int textureWidth = 64;
	// public int textureHeight = 32;

	protected ModelRenderer slab;
	protected ModelRenderer legs;
	protected ModelRenderer cube;

	/**
     * 
     */
	public TinkerTableModel() {
		model.textureWidth = 64;
		model.textureHeight = 40;
		slab = new ModelRenderer(model, 0, 0);
		slab.mirror = true;
		slab.addBox(
				0, 12, 0, // xyz offset of the model
				16, 4, 16); // xyz size
		// renderer.setRotationPoint(0,0,0);
		legs = new ModelRenderer(model, 32, 20);
		legs.mirror = true;
		legs.addBox(
				1, 0, 1,
				4, 12, 4);
		legs.addBox(
				1, 0, 11,
				4, 12, 4);
		legs.addBox(
				11, 0, 1,
				4, 12, 4);
		legs.addBox(
				11, 0, 11,
				4, 12, 4);
		cube = new ModelRenderer(model, 0, 20);
		cube.mirror = true;
		cube.addBox(-4, -4, -4, 8, 8, 8);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f,
			float f1) {
		int timestep = (int) ((System.currentTimeMillis()) % 10000);
		double angle = timestep * Math.PI / 5000.0;
		slab.render(0.0625f);
		legs.render(0.0625f);
		GL11.glPushMatrix();
		MuseRenderer.smoothingOn();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				240.0F, 240.0F);
		RenderHelper.disableStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslated(0.5f, 1.05f, 0.5f);
		GL11.glTranslated(0, 0.02f * Math.sin(angle * 3), 0);
		// GLRotate uses degrees instead of radians for some reason grr
		GL11.glRotatef((float) (angle * 57.2957795131), 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(45f, 1.0f, 0.0f, 0.0f);
		// arctangent of 0.5.
		GL11.glRotatef(35.2643897f, 0, 1, 1);
		// cube.render(0.0625f);
		cube.render(0.015625f);
		// cube.render(0.016000f);
		GL11.glPopMatrix();

		for (int i = 0; i < 2; i++) {
			drawScanLine(angle);
		}
		GL11.glDisable(GL11.GL_CULL_FACE);
		MuseRenderer.drawGradientRect3D(
				Vec3.createVectorHelper(0, 1.2, 0),
				Vec3.createVectorHelper(1, 0, 1),
				new Colour(0.3f, 1.0f, 0.3f, 0.8f),
				new Colour(0.3f, 1.0f, 0.3f, 0.8f));
		GL11.glEnable(GL11.GL_CULL_FACE);

		// MuseRenderer.off2D();
		RenderHelper.enableStandardItemLighting();
		MuseRenderer.smoothingOff();
		GL11.glPopMatrix();
	}

	private void drawScanLine(double angle) {
		float xtarg = random.nextFloat();
		float ytarg = random.nextFloat();
		GL11.glLineWidth(2);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor4d(0.0, 1.0, 0.2, 1.0);
		GL11.glVertex3d(0.5, 1.05 + 0.02f * Math.sin(angle * 3), 0.5);
		GL11.glVertex3d(xtarg, 1.2f, ytarg);
		GL11.glEnd();

	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity par1Entity, float par2, float par3, float par4,
			float par5, float par6, float par7) {
		model.render(par1Entity, par2, par3, par4, par5, par6, par7);
	}
	// @Override
	// protected void setTextureOffset(String par1Str, int par2, int par3)
	// {
	// this.modelTextureMap.put(par1Str, new TextureOffset(par2, par3));
	// }
	//
	// @Override
	// public TextureOffset getTextureOffset(String par1Str)
	// {
	// return (TextureOffset) this.modelTextureMap.get(par1Str);
	// }
}
