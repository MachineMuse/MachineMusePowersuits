package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.client.models.obj.OBJModelPlus;
import net.minecraft.client.resources.I18n;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpec extends Spec {
    private OBJModelPlus.OBJBakedModelPus model;

    public ModelSpec(OBJModelPlus.OBJBakedModelPus model, String name, boolean isDefault, EnumSpecType specType) {
        super(name, isDefault, specType);
        this.model = model;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModelSpec modelSpec = (ModelSpec) o;
        return Objects.equal(getModel(), modelSpec.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getModel());
    }
}