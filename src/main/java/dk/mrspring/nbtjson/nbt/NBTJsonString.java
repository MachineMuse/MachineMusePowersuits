package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagString;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonString extends NBTJsonBase<NBTTagString>
{
    String value;

    public NBTJsonString(String value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagString makeNBTTag()
    {
        return new NBTTagString(value);
    }
}
