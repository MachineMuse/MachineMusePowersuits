package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.client.render.entity.EntityRenderLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRenderPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRenderSpinningBlade;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * This is here because of scala
 */
public class RegEntityRenderers {
    public static void Reg() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRenderPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRenderSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRenderLuxCapacitorEntity::new);
    }
}
