package dk.mrspring.nbtjson.nbt;

import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.nbtjson.NBTJsonDecompile;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * Created by Konrad on 16-07-2015.
 */
public class NBTJsonCompound extends NBTJsonBase<NBTTagCompound>
{
    Map<String, Object> value;

    public NBTJsonCompound(LinkedTreeMap<String, Object> value)
    {
        super();
        this.value = value;
    }

    @Override
    public NBTTagCompound makeNBTTag()
    {
        return NBTJsonDecompile.createFromJsonObject(this.value);
    }
}
