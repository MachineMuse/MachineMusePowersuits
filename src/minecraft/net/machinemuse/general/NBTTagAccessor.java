/**
 *
 */
package net.machinemuse.general;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                mTagAccessor = NBTTagCompound.class.getDeclaredMethod(
                        "getTagMap", NBTTagCompound.class);
                mTagAccessor.setAccessible(true);
                return mTagAccessor;
            } catch (NoSuchMethodException e) {
                mTagAccessor = NBTTagCompound.class.getDeclaredMethod(
                        "a", NBTTagCompound.class);
                mTagAccessor.setAccessible(true);
                return mTagAccessor;
            }
        } else {
            return mTagAccessor;
        }
    }

    public static Map getMap(NBTTagCompound nbt) {
        try {
            return (Map) getTagAccessor().invoke(nbt, nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MuseLogger.logError("Unable to access nbt tag map!");
        return null;
    }

    public static List<NBTTagInt> getIntValues(NBTTagCompound nbt) {
        ArrayList<NBTTagInt> a = new ArrayList<NBTTagInt>(nbt.getTags().size());
        for (Object o : nbt.getTags()) {
            if (o instanceof NBTTagInt) {
                a.add((NBTTagInt) o);
            }
        }
        return a;
    }

    public static List<NBTTagCompound> getValues(NBTTagCompound nbt) {
        ArrayList<NBTTagCompound> a = new ArrayList<NBTTagCompound>(nbt.getTags().size());
        for (Object o : nbt.getTags()) {
            if (o instanceof NBTTagCompound) {
                a.add((NBTTagCompound) o);
            }
        }
        return a;
    }
}
