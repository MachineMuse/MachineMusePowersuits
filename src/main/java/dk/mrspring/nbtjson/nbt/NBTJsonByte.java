package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagByte;

/**
 * Created by Konrad on 15-07-2015.
 */
public class NBTJsonByte extends NBTJsonBase<NBTTagByte>
{
    byte value;

    public NBTJsonByte(byte value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagByte makeNBTTag()
    {
        return new NBTTagByte(value);
    }
}
