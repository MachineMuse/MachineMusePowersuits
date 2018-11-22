package dk.mrspring.nbtjson;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.Set;

/**
 * Created by Konrad on 21-07-2015.
 */
public class NBTJsonCompile
{
    public static Object createJsonFromObject(NBTTagCompound compound)
    {
        Map<String, NBTJsonBaseWrapper> map = new LinkedTreeMap<String, NBTJsonBaseWrapper>();
        for (String tagName : (Set<String>) compound.getKeySet())
        {
            NBTBase tagValue = compound.getTag(tagName);
            NBTJsonBaseWrapper wrapper = compileTag(tagValue);
            if (wrapper != null) map.put(tagName, wrapper);
        }
        return map;
    }

    public static NBTJsonBaseWrapper compileTag(NBTBase tag)
    {
        int typeId = tag.getId();
        NBTType type = NBTType.fromId(typeId);
        return type.makeWrapper(tag);
    }

    public static String createJsonStringFromCompound(NBTTagCompound compound)
    {
        Object o = createJsonFromObject(compound);
        Gson gson = new Gson();
        return gson.toJson(o);
    }
}
