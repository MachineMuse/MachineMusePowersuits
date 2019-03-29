package net.machinemuse.numina.client.render.modelspec;

import net.machinemuse.numina.map.MuseRegistry;
import net.machinemuse.numina.math.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 */
public abstract class SpecBase extends MuseRegistry<PartSpecBase> {
    private final String name;
    private final boolean isDefault;
    private final EnumSpecType specType;
    private List<Integer> colours = new ArrayList() {{
        add(Colour.WHITE.getInt());
    }};

    public SpecBase(final String name, final boolean isDefault, final EnumSpecType specType) {
        this.name = name;
        this.isDefault = isDefault;
        this.specType = specType;
    }

    public abstract String getDisaplayName();

    public Iterable<PartSpecBase> getPartSpecs() {
        return this.elems();
    }

    /**
     * returns the parent spec id
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public EnumSpecType getSpecType() {
        return specType;
    }

    public List<Integer> getColours() {
        return colours;
    }

    /**
     * Only adds the colour if it doesn't already exist.
     *
     * @param colour
     * @return returns the index of the colour
     */
    public int addColourIfNotExist(Colour colour) {
        int colourInt = colour.getInt();
        if (!colours.contains(colourInt)) {
            colours.add(colourInt);
            return colours.size() - 1; // index of last entry
        } else
            return colours.indexOf(colourInt);
    }

    /**
     * returns the short id of the model. Used for NBT tags
     * Implement on top level classes due to equals and hash checks will make this fail here
     *
     * @return
     */
    public abstract String getOwnName();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecBase specBase = (SpecBase) o;
        return isDefault == specBase.isDefault &&
                Objects.equals(name, specBase.name) &&
                specType == specBase.specType &&
                Objects.equals(colours, specBase.colours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isDefault, specType, colours);
    }
}