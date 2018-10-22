package net.machinemuse.numina.api.module;

/**
 * The module categories
 */
public enum EnumModuleCategory {
    CATEGORY_NONE("None"),
    CATEGORY_DEBUG("Debug"),
    CATEGORY_ARMOR("Armor"),
    CATEGORY_ENERGY("Energy"),
    CATEGORY_TOOL("Tool"),
    CATEGORY_WEAPON("Weapon"),
    CATEGORY_MOVEMENT("Movement"),
    CATEGORY_COSMETIC("Cosmetic"),
    CATEGORY_VISION("Vision"),
    CATEGORY_ENVIRONMENTAL("Environment"),
    CATEGORY_SPECIAL("Special");


    private final String name;

    //TODO: add translation stuff
    EnumModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
