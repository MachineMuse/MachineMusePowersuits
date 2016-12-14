package net.machinemuse.powersuits.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:31 PM, 8/3/13
 */
public abstract class MuseRender extends Render {
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
