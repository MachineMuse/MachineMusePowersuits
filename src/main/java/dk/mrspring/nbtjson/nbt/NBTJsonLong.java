package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagLong;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonLong extends NBTJsonBase<NBTTagLong>
{
    long value;

    public NBTJsonLong(long value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagLong makeNBTTag()
    {
        return new NBTTagLong(value);
    }
}
