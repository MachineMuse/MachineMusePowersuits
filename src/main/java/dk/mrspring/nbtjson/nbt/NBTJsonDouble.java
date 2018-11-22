package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagDouble;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonDouble extends NBTJsonBase<NBTTagDouble>
{
    double value;

    public NBTJsonDouble(double value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagDouble makeNBTTag()
    {
        return new NBTTagDouble(value);
    }
}
