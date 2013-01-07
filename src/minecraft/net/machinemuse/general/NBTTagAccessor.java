/**
 * 
 */
package net.machinemuse.general;

import java.lang.reflect.Method;
import java.util.Map;

import net.machinemuse.powersuits.common.MuseLogger;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Workaround class to access static NBTTagCompound.getTagMap()
 * 
 * @author MachineMuse
 * 
 */
public class NBTTagAccessor extends NBTTagCompound {
	public static Method mTagAccessor;

	/**
	 * Accesses the package-visible
	 * 
	 * <pre>
	 * Map NBTTagCompound.getTagMap(NBTTagCompound tag)
	 * </pre>
	 * 
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
}
