package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Objects;
import net.machinemuse.numina.utils.map.MuseRegistry;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public abstract class SpecBase extends MuseRegistry<PartSpecBase> {
    private final String name;
    private final boolean isDefault;
    private final EnumSpecType specType;

    public SpecBase(String name, boolean isDefault, EnumSpecType specType) {
        this.name = name;
        this.isDefault = isDefault;
        this.specType = specType;
    }

    public abstract String getDisaplayName();

    public Iterable<PartSpecBase> getPartSpecs() {
        return this.elems();
    }

    public String getName() {
        return name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public EnumSpecType getSpecType() {
        return specType;
    }

    public String getOwnName() {
        String name = ModelRegistry.getInstance().getName(this);
        return (name != null) ? name : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecBase spec = (SpecBase) o;
        return isDefault() == spec.isDefault() &&
                Objects.equal(getName(), spec.getName()) &&
                getSpecType() == spec.getSpecType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), isDefault(), getSpecType());
    }
}