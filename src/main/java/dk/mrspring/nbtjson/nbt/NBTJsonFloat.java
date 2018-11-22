package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagFloat;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonFloat extends NBTJsonBase<NBTTagFloat>
{
    float value;

    public NBTJsonFloat(float value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagFloat makeNBTTag()
    {
        return new NBTTagFloat(value);
    }
}
