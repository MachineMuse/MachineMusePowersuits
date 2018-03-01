package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.client.model.obj.OBJModelPlus;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends Spec {
    private OBJModelPlus.OBJBakedModelPus model;
    private final IModelState modelState;

    public ModelSpec(OBJModelPlus.OBJBakedModelPus model, IModelState state, String name, boolean isDefault, EnumSpecType specType) {
        super(name, isDefault, specType);
        this.modelState = state;
        this.model = model;
    }

    public TRSRTransformation getTransform(ItemCameraTransforms.TransformType transformType) {
        Optional<TRSRTransformation> transformation = modelState.apply(Optional.of(transformType));
        return transformation.orElse(TRSRTransformation.identity());
    }

    @Override
    public String getDisaplayName() {
        return I18n.format(new StringBuilder("model.")
                .append(this.getOwnName())
                .append(".modelName")
                .toString());
    }

    public OBJModelPlus.OBJBakedModelPus getModel() {
        return model;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c==0) nbt.removeTag("colourindex");
        else nbt.setInteger("colourindex", c);
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModelSpec modelSpec = (ModelSpec) o;
        return Objects.equal(getModel(), modelSpec.getModel()) &&
                Objects.equal(modelState, modelSpec.modelState);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getModel(), modelState);
    }
}