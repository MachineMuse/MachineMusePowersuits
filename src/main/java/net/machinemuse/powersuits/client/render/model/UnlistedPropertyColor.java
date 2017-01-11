package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.numina.geometry.Colour;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Ported to Java by lehjr on 12/31/16.
 */
public class UnlistedPropertyColor implements IUnlistedProperty<Colour> {
    public static UnlistedPropertyColor INSTANCE = new UnlistedPropertyColor();

    private static Colour colour;

    public UnlistedPropertyColor() {};

    public UnlistedPropertyColor(Colour colour) {
        this.colour = colour;
    }

    @Override
    public String getName() {
        return colour.hexColour();
    }

    @Override
    public boolean isValid(Colour value) {
        return true;
    }

    @Override
    public Class<Colour> getType() {
        return Colour.class;
    }

    @Override
    public String valueToString(Colour value) {
        return value.hexColour();
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour newColour) {
        this.colour = newColour;
    }

    public void setColour(int colourInt) {
        this.colour = new Colour(colourInt);
    }
}