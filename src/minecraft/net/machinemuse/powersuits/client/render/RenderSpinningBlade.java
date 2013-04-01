package net.machinemuse.powersuits.client.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class RenderSpinningBlade extends Render {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialticktime) {

		glPushMatrix();
		glTranslated(x, y, z);
		glRotatef(yaw, (float) -entity.motionZ, 0.0f, (float) entity.motionX);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		glBegin(GL_QUADS);

		glVertex3d(x + 1, y, z + 1);
		glVertex3d(x + 1, y, z - 1);
		glVertex3d(x - 1, y, z - 1);
		glVertex3d(x - 1, y, z + 1);

		glEnd();
		glEnable(GL_TEXTURE_2D);
		glPopMatrix();
	}

}
