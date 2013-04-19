package net.machinemuse.powersuits.client.render;

import net.machinemuse.general.MuseRenderer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArmorPartRenderer extends ModelRenderer {
	protected WavefrontObject model;
	protected String[] normalparts;
	protected String[] glowyparts;
	protected ArmorModel modelBase;

	public ArmorPartRenderer(ArmorModel modelBase, WavefrontObject model, String normalparts, String glowyparts) {
		super(modelBase);
		this.modelBase = modelBase;
		this.model = model;
		this.normalparts = normalparts.split(";");
		this.glowyparts = glowyparts.split(";");
	}

	private void renderParts() {
		GL11.glPushMatrix();
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glTranslated(0, -1.625, 0);
		double scale = 0.0625;
		GL11.glScaled(scale, scale, scale);
		if (modelBase.normalcolour != null) {
			modelBase.normalcolour.doGL();
		}
		this.model.renderOnly(normalparts);
		MuseRenderer.glowOn();
		if (modelBase.glowcolour != null) {
			modelBase.glowcolour.doGL();
		}
		this.model.renderOnly(glowyparts);
		MuseRenderer.glowOff();
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void render(float par1)
	{
		if (!this.isHidden && this.showModel)
		{
			GL11.glTranslatef(this.field_82906_o, this.field_82908_p, this.field_82907_q);
			GL11.glPushMatrix();
			GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);

			if (this.rotateAngleZ != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
			}

			if (this.rotateAngleY != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
			}

			if (this.rotateAngleX != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
			}

			GL11.glTranslatef(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
			renderParts();

			GL11.glPopMatrix();

			GL11.glTranslatef(-this.field_82906_o, -this.field_82908_p, -this.field_82907_q);
		}

	}

}
