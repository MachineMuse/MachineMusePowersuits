package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import static org.lwjgl.opengl.GL11.*;

public class RenderSpinningBlade extends Render {

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialticktime) {

        glPushMatrix();
        glPushAttrib(GL_ENABLE_BIT);
        glDisable(GL_CULL_FACE);
        MuseRenderer.pushTexture(Config.TEXTURE_PREFIX + "items/spinningblade.png");
        glTranslated(x, y, z);
        double motionscale = Math.sqrt(entity.motionZ * entity.motionZ + entity.motionX * entity.motionX);
        glRotatef(90, 1, 0, 0);
        glRotatef(-entity.rotationPitch, (float) (entity.motionZ /
                motionscale), 0.0f, (float) (-entity.motionX / motionscale));
        int time = (int) System.currentTimeMillis() % 360;
        glRotatef(time / 2, 0, 0, 1);
        double scale = 0.5;
        glScaled(scale, scale, scale);
        glBegin(GL_QUADS);
        glTexCoord2d(0, 0);
        glVertex3d(-1, -1, 0);
        glTexCoord2d(0, 1);
        glVertex3d(-1, 1, 0);
        glTexCoord2d(1, 1);
        glVertex3d(1, 1, 0);
        glTexCoord2d(1, 0);
        glVertex3d(1, -1, 0);

        glEnd();
        MuseRenderer.popTexture();
        glPopAttrib();
        glPopMatrix();
    }
}
