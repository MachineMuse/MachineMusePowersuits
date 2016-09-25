package net.machinemuse.powersuits.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:31 PM, 8/3/13
 */
public class MuseEntityRender<T extends Entity> extends Render<T> {
    protected MuseEntityRender(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
