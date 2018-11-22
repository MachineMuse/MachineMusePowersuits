package dk.mrspring.nbtjson.nbt;

import net.minecraft.nbt.NBTTagIntArray;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonIntegers extends NBTJsonBase<NBTTagIntArray>
{
    int[] value;

    public NBTJsonIntegers(Integer[] value)
    {
        super();
        this.value = ArrayUtils.toPrimitive(value);
    }

    public NBTJsonIntegers(List<Double> value)
    {
        super();
        int[] ints = new int[value.size()];
        for (int i = 0; i < value.size(); i++) ints[i] = value.get(i).intValue();
        this.value = ints;
    }

    @Override
    public NBTTagIntArray makeNBTTag()
    {
        return new NBTTagIntArray(value);
    }
}
