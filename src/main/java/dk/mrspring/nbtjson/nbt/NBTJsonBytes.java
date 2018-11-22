package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagByteArray;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonBytes extends NBTJsonBase<NBTTagByteArray>
{
    byte[] value;

    public NBTJsonBytes(Byte[] value)
    {
        this(ArrayUtils.toPrimitive(value));
    }

    public NBTJsonBytes(byte[] value)
    {
        super();
        this.value = value;
    }

    public NBTJsonBytes(List<Double> value)
    {
        super();
        byte[] bytes = new byte[value.size()];
        for (int i = 0; i < value.size(); i++) bytes[i] = value.get(i).byteValue();
        this.value = bytes;
    }

    @Override
    public NBTTagByteArray makeNBTTag()
    {
        return new NBTTagByteArray(value);
    }
}
