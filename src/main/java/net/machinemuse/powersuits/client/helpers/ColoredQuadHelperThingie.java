package net.machinemuse.powersuits.client.helpers;

import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/*
 * This is just a helper for creating a map key for Guava cache
 */
public class ColoredQuadHelperThingie {
    private final EnumColour colour;
    private final EnumFacing facing;

    public ColoredQuadHelperThingie(EnumColour colour, @Nullable EnumFacing facing) {
        this.colour = colour;
        this.facing = facing;
    }

    public EnumColour getColour() {
        return colour;
    }

    @Nullable
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ColoredQuadHelperThingie)
            return (((ColoredQuadHelperThingie) o).colour == this.colour) && (((ColoredQuadHelperThingie) o).facing == this.facing);
        return false;
    }

    /*
     * only need 2 bytes here, one for the Enumfacing and one for the EnumColour
     */
    @Override
    public int hashCode() {
        int result = 0;
        result = result | colour.getIndex();
        return result | (((facing != null) ? facing.getIndex() : 6) <<  8);
    }
}