package dk.mrspring.nbtjson;

/**
 * Created by Konrad on 15-07-2015.
 */
public class NBTJsonBaseWrapper
{
    Object nbt_type = -1;
    Object value = null;

    public NBTJsonBaseWrapper(Object type, Object value)
    {
        this.nbt_type = type;
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    public Object getType()
    {
        return nbt_type;
    }
}
