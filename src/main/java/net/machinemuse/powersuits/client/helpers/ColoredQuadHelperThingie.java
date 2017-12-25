package net.machinemuse.powersuits.client.helpers;

import com.google.common.base.Objects;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColoredQuadHelperThingie that = (ColoredQuadHelperThingie) o;
        return getColour() == that.getColour() &&
                getFacing() == that.getFacing();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getColour(), getFacing());
    }
}