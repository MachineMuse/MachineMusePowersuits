package net.machinemuse.powersuits.world;

import net.minecraft.world.WorldType;

public class MPSCosmeticPresetWorld extends WorldType {
    /**
     * Creates a new world type, the ID is hidden and should not be referenced by modders.
     * It will automatically expand the underlying workdType array if there are no IDs left.
     */
    public MPSCosmeticPresetWorld() {
        super("MPSArmorCosmeticPresetFactory");
    }


    @Override
    public boolean canBeCreated() {
        return super.canBeCreated();
    }
}
