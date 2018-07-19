package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EntityRendererSpinningBlade extends MuseEntityRenderer<EntitySpinningBlade> {
    public EntityRendererSpinningBlade(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySpinningBlade entity) {
        return new ResourceLocation(ModularPowersuits.MODID, "modules/spinningblade.png");
    }

    @Override
    public void doRender(EntitySpinningBlade entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        MuseTextureUtils.pushTexture(MPSResourceConstants.TEXTURE_PREFIX + "modules/spinningblade.png");
        GL11.glTranslated(x, y, z);
        double motionscale = Math.sqrt(entity.motionZ * entity.motionZ + entity.motionX * entity.motionX);
        GL11.glRotatef(90, 1, 0, 0);
        GL11.glRotatef(-entity.rotationPitch, (float) (entity.motionZ /
                motionscale), 0.0f, (float) (-entity.motionX / motionscale));
        int time = (int) System.currentTimeMillis() % 360;
        GL11.glRotatef(time / 2, 0, 0, 1);
        double scale = 0.5;
        GL11.glScaled(scale, scale, scale);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex3d(-1, -1, 0);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex3d(-1, 1, 0);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex3d(1, 1, 0);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex3d(1, -1, 0);

        GL11.glEnd();
        MuseTextureUtils.popTexture();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}