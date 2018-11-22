package dk.mrspring.nbtjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.nbtjson.nbt.NBTJsonBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * Created by Konrad on 15-07-2015.
 */
public class NBTJsonDecompile
{
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson = builder.create();

    public static NBTTagCompound createFromJsonObject(Object json)
    {
//        System.out.println(json.getClass().getName());
        if (!(json instanceof LinkedTreeMap)) return new NBTTagCompound();
        Map<String, Object> map = (LinkedTreeMap<String, Object>) json;
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            String name = entry.getKey();
            NBTJsonBase jsonBase = createBase(entry.getValue());
            NBTBase tag = jsonBase != null ? jsonBase.makeNBTTag() : null;
            if (tag != null) compound.setTag(name, tag);
        }
        return compound;
    }

    public static void rebuildGson()
    {
        gson = builder.create();
    }

    public static GsonBuilder getBuilder()
    {
        return builder;
    }

    public static NBTTagCompound createFromRawJson(String json)
    {
        return new NBTTagCompound();
    }

    public static NBTJsonBaseWrapper createWrapper(Object jsonObject)
    {
        if (jsonObject instanceof NBTJsonBaseWrapper) return (NBTJsonBaseWrapper) jsonObject;
        else if (jsonObject instanceof LinkedTreeMap)
        {
            Map<String, Object> map = (LinkedTreeMap<String, Object>) jsonObject;
            Object value = map.get("value");
            Object typeObject = map.get("nbt_type");
            return new NBTJsonBaseWrapper(typeObject, value);
        } else return null;
    }

    public static NBTJsonBase fromWrapper(NBTJsonBaseWrapper wrapper)
    {
        Object typeObject = wrapper.getType();
        NBTType type = null;
        if (typeObject instanceof String) type = NBTType.valueOf((String) typeObject);
        else if (typeObject instanceof Double)
        {
            int id = ((Double) typeObject).intValue();
            type = NBTType.fromId(id);
        }
        if (type == null) type = NBTType.UNKNOWN;
        return type.makeNBT(wrapper.getValue());
    }

    public static NBTJsonBase createBase(Object jsonObject)
    {
        NBTJsonBaseWrapper wrapper = createWrapper(jsonObject);
        return wrapper != null ? fromWrapper(wrapper) : null;
    }
}
