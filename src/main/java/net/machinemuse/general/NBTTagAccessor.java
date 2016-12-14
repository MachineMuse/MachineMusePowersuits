/**
 *
 */
package net.machinemuse.general;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Workaround class to access static NBTTagCompound.getTagMap()
 *
 * @author MachineMuse
 */
public class NBTTagAccessor extends NBTTagCompound {
    public static Method mTagAccessor;

    /**
     * Accesses the package-visible
     * <p/>
     * <pre>
     * Map NBTTagCompound.getTagMap(NBTTagCompound tag)
     * </pre>
     * <p/>
     * Will likely need to be updated every time the obfuscation changes.
     *
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Method getTagAccessor() throws NoSuchMethodException,
            SecurityException {
        if (mTagAccessor == null) {
            try {
                mTagAccessor = NBTTagCompound.class.getDeclaredMethod("getTagMap", NBTTagCompound.class);
                mTagAccessor.setAccessible(true);
                return mTagAccessor;
            } catch (NoSuchMethodException e) {
                mTagAccessor = NBTTagCompound.class.getDeclaredMethod("a", NBTTagCompound.class);
                mTagAccessor.setAccessible(true);
                return mTagAccessor;
            }
        } else {
            return mTagAccessor;
        }
    }

    @Nullable
    public static Map getMap(NBTTagCompound nbt) {
        try {
            return (Map) getTagAccessor().invoke(nbt, nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MuseLogger.logError("Unable to access nbt tag map!");
        return null;
    }


    public static List<NBTTagCompound> getValues(NBTTagCompound nbt) {
        Set<String> keyset = (Set<String>) nbt.func_150296_c();
        ArrayList<NBTTagCompound> a = new ArrayList<NBTTagCompound>(keyset.size());
        for (String key : keyset) {
            NBTBase c = nbt.getTag(key);
            if (c instanceof NBTTagCompound) {
                a.add((NBTTagCompound) c);
            }
        }
        return a;
    }
}
