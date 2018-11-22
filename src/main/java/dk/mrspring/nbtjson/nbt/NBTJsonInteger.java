package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagInt;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonInteger extends NBTJsonBase<NBTTagInt>
{
    int value;

    public NBTJsonInteger(int value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagInt makeNBTTag()
    {
        return new NBTTagInt(value);
    }
}
