package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.scala.MuseRegistry;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.WavefrontObject;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends MuseRegistry<ModelPartSpec>
{
    public WavefrontObject model;
    public String[] textures;
    public Vec3 offset;
    public Vec3 rotation;
    public static String filename;

    public ModelSpec(WavefrontObject model, String[] textures, Vec3 offset, Vec3 rotation, String filename) {
        this.model = model;
        this.textures = textures;
        this.offset = offset;
        this.rotation = rotation;
        ModelSpec.filename = filename;
    }
    
    public void applyOffsetAndRotation() {
    }

    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }
}