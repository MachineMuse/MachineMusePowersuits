package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagShort;

/**
 * Created by Konrad on 15-07-2015.
 */
public class NBTJsonShort extends NBTJsonBase<NBTTagShort>
{
    short value;

    public NBTJsonShort(short value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagShort makeNBTTag()
    {
        return new NBTTagShort(value);
    }
}
