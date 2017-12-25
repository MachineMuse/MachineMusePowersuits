package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.base.Objects;

public class Spec {
    private final String name;
    private final boolean isDefault;
    private final EnumSpecType specType;

    public Spec(String name, boolean isDefault, EnumSpecType specType) {
        this.name = name;
        this.isDefault = isDefault;
        this.specType = specType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spec spec = (Spec) o;
        return isDefault() == spec.isDefault() &&
                Objects.equal(getName(), spec.getName()) &&
                getSpecType() == spec.getSpecType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), isDefault(), getSpecType());
    }
}