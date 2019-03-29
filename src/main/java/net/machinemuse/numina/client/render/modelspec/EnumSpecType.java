package net.machinemuse.numina.client.render.modelspec;

import java.util.Arrays;

public enum EnumSpecType {
    ARMOR_MODEL("ARMORMODEL"),
    ARMOR_SKIN("ARMORSKIN"),
    WIELDABLE("WIELDABLE"),
    NONE("NONE"); // for broken stuff

    String name;

    EnumSpecType(String name) {
        this.name = name;
    }

    public static EnumSpecType getTypeFromName(String nameIn) {
        String finalNameIn = nameIn.toUpperCase().replaceAll("\\s", "");
        return Arrays.stream(values()).filter(spec -> spec.getName().equals(finalNameIn)).findAny().orElse(NONE);
    }

    public String getName() {
        return this.name;
    }
}
