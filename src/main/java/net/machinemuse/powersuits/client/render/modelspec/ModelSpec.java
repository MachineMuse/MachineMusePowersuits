package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.scala.MuseRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends MuseRegistry<ModelPartSpec> {
    private IBakedModel model;
    public Vec3d offset;
    public Vec3d rotation;
    public static String filename;

    public ModelSpec(IBakedModel model, Vec3d offset, Vec3d rotation, String filename) {
        this.model = model;
        this.offset = offset;
        this.rotation = rotation;
        this.filename = filename;
    }

    public IBakedModel getModel() {
        return model;
    }

    public void applyOffsetAndRotation() {
    }

    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }
}