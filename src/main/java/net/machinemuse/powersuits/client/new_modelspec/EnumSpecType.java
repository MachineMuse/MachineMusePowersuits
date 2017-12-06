package net.machinemuse.powersuits.client.new_modelspec;

import java.util.Arrays;

public enum EnumSpecType {
    ARMOR_MODEL("ARMORMODEL"),
    ARMOR_SKIN("ARMORSKIN"),
    POWER_FIST("POWERFIST");

    String name;
    EnumSpecType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumSpecType getTypeFromName(String nameIn) {
        String finalNameIn = nameIn.toUpperCase().replaceAll("\\s","");
        return Arrays.stream(values()).filter(spec -> spec.getName().equals(finalNameIn)).findAny().orElse(null);
    }
}
