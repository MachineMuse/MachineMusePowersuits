package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.scala.MuseRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends MuseRegistry<ModelPartSpec> {

    // here I want a model that has a quadmap for each part


    private IBakedModel model;
    public String[] textures;
    public Vec3d offset;
    public Vec3d rotation;
    public static String filename;

    public ModelSpec(IBakedModel model, String[] textures, Vec3d offset, Vec3d rotation, String filename) {
        this.model = model;
        this.textures = textures;
        this.offset = offset;
        this.rotation = rotation;
        this.filename = filename;
    }

    public IBakedModel getModel() {
        return model;
    }

    public List<String> getModelpartList() {
        return new ArrayList<>(((OBJModel.OBJBakedModel) model).getModel().getMatLib().getGroups().keySet());
    }

    public void applyOffsetAndRotation() {
    }

    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }
}