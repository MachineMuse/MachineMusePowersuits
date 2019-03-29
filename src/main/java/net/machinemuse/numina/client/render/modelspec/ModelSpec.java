package net.machinemuse.numina.client.render.modelspec;

import net.machinemuse.numina.client.model.obj.MuseOBJModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Objects;
import java.util.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends SpecBase {
    private final MuseOBJModel.MuseOBJBakedModel model;
    private final IModelState modelState;

    public ModelSpec(final MuseOBJModel.MuseOBJBakedModel model, final IModelState state, final String name, final boolean isDefault, final EnumSpecType specType) {
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

    @Override
    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }

    public MuseOBJModel.MuseOBJBakedModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModelSpec modelSpec = (ModelSpec) o;
        return Objects.equals(model, modelSpec.model) &&
                Objects.equals(modelState, modelSpec.modelState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), model, modelState);
    }
}