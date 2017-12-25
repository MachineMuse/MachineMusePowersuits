package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.base.Objects;
import net.machinemuse.powersuits.common.MPSConstants;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

public class ModelPartSpec {
    private int modelHash; // smaller data type than a full resource location
    private final String partName;
    private int colourindex; // NOT A COLOR but an index of a list
    private boolean glow;

    public ModelPartSpec(final int modelHash, final String partName, final Integer colourindex, final Boolean glow) {
        this.modelHash = modelHash;
        this.partName = partName;
        this.colourindex = colourindex != null ? colourindex : MPSConstants.ENUM_COLOUR_WHITE_INDEX;
        this.glow = glow != null ? glow : false;
    }

    public String getModelLocation() {
        return BakedModelRegistry.getInstance().getBakedModleLocation(this.modelHash);
    }

    public int getModelHash() {
        return modelHash;
    }

    public String getPartName() {
        return partName;
    }


    public int getColourindex() {
        return colourindex;
    }

    public boolean isGlow() {
        return glow;
    }

    public boolean samePartSpec(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelPartSpec that = (ModelPartSpec) o;
        return Objects.equal(getModelLocation(), that.getModelLocation()) &&
                Objects.equal(getPartName(), that.getPartName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelPartSpec partSpec = (ModelPartSpec) o;
        return getModelHash() == partSpec.getModelHash() &&
                getColourindex() == partSpec.getColourindex() &&
                isGlow() == partSpec.isGlow() &&
                Objects.equal(getPartName(), partSpec.getPartName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getModelHash(), getPartName(), getColourindex(), isGlow());
    }

    /** From NBT -------------------------------------------------------------------------------- */
    public ModelPartSpec(NBTTagCompound nbt) {
        Set<String> keys = nbt.getKeySet();

        String tempString = "";
        if(!keys.isEmpty()) {
            tempString = keys.iterator().next();
        }
        if (tempString.contains(".")) {
            String[] modelHashDotPartName = tempString.split("\\."); // split uses regex
            if (modelHashDotPartName.length == 2) {
                this.modelHash = Integer.parseInt(modelHashDotPartName[0]);
                this.partName = modelHashDotPartName[1];
                NBTTagCompound nbt2 = nbt.getCompoundTag(tempString);

                this.glow = getNBTGlow(nbt2);
                this.colourindex = getNBTEnumCourIndex(nbt2);

            } else {
                this.modelHash = 0;
                this.partName = "";
            }
        } else {
            // This should never happen but who knows
            this.modelHash = 0;
            this.partName = "";
        }
    }


    public boolean getNBTGlow(NBTTagCompound nbt) {
        return nbt.hasKey("glow") ? nbt.getBoolean("glow") : false;
    }

    public int getNBTEnumCourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? Byte.toUnsignedInt(nbt.getByte("colourindex")) : MPSConstants.ENUM_COLOUR_WHITE_INDEX;
    }


    /** To NBT ------------------------------------------------------------------------------------ */
    NBTTagCompound setNBTGlow(NBTTagCompound nbt) {
        if (this.glow)
            nbt.setBoolean("glow", true);
        return nbt;
    }

    public NBTTagCompound setNBTEnumCourIndex(NBTTagCompound nbt) {
        if (this.colourindex != MPSConstants.ENUM_COLOUR_WHITE_INDEX)
            nbt.setByte("colourindex", (byte)this.colourindex);
        return nbt;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound settingsNBT = new NBTTagCompound();
        settingsNBT = setNBTGlow(settingsNBT);
        settingsNBT = setNBTEnumCourIndex(settingsNBT);
        NBTTagCompound retNBT = new NBTTagCompound();
        retNBT.setTag(new StringBuilder("").append(this.modelHash).append(".").append(this.partName).toString(), settingsNBT);
        return retNBT;
    }
}