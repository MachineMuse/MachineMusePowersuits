package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.scala.MuseRegistry;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends MuseRegistry<ModelPartSpec>
{
    public OBJModel model;
    public String[] textures;
    public Vec3d offset;
    public Vec3d rotation;
    public static String filename;

    public ModelSpec(OBJModel model, String[] textures, Vec3d offset, Vec3d rotation, String filename) {
        this.model = model;
        this.textures = textures;
        this.offset = offset;
        this.rotation = rotation;
        this.filename = filename;
    }
    
    public void applyOffsetAndRotation() {
    }

    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }
}