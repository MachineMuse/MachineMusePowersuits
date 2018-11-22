package dk.mrspring.nbtjson.nbt;

import dk.mrspring.nbtjson.NBTJsonDecompile;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonList extends NBTJsonBase<NBTTagList>
{
    List value;

    public NBTJsonList(List value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagList makeNBTTag()
    {
        NBTTagList list = new NBTTagList();
        for (Object object : value)
        {
            NBTJsonBase base = NBTJsonDecompile.createBase(object);
            if (base != null) list.appendTag(base.makeNBTTag());
        }
        return list;
    }
}
