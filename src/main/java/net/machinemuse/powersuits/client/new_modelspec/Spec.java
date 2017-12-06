package net.machinemuse.powersuits.client.new_modelspec;

public class Spec implements ISpec {
    private final String name;
    private final boolean isDefault;

    public Spec(String name, boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
