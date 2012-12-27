/**
 * 
 */
package net.machinemuse.powersuits.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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
	 */
	public static Method getTagAccessor() {
		if (mTagAccessor == null) {
			try {
				mTagAccessor = NBTTagCompound.class.getDeclaredMethod(
						"getTagMap", NBTTagCompound.class);
				mTagAccessor.setAccessible(true);
				return mTagAccessor;
			} catch (NoSuchMethodException e) {
				MuseLogger.logError("4");
				try {
					mTagAccessor = NBTTagCompound.class.getDeclaredMethod(
							"a", NBTTagCompound.class);
					mTagAccessor.setAccessible(true);
					return mTagAccessor;
				} catch (NoSuchMethodException e1) {
					MuseLogger.logError("1");
					e1.printStackTrace();
				} catch (SecurityException e1) {
					MuseLogger.logError("2");
					e1.printStackTrace();
				}
			} catch (SecurityException e) {
				MuseLogger.logError("3");
				e.printStackTrace();
			}

		}
		MuseLogger.logError("Error 1: Unable to access nbt tag map!");
		return null;
	}

	public static Map getMap(NBTTagCompound nbt) {
		try {
			return (Map) getTagAccessor().invoke(null, nbt);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		MuseLogger.logError("Error 2: Unable to access nbt tag map!");
		return null;
	}
}
