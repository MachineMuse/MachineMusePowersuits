package net.machinemuse.powersuits.control;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Created by leon on 7/4/16.
 */
public class KeyBindingHelper {
    static KeyBindingMap hash;

    static {
        new KeyBindingHelper();
    }

    static KeyBindingMap getKeyBindingMap() {
        try {
            if (hash == null) {
                if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
                    hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "HASH");
                else
                    hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "field_74514_b");
            }
        } catch (Exception e) {
            return null;
        }
        return hash;
    }

    public boolean keyBindingHasKey(int key) {
        try {
            return (getKeyBindingMap() != null) ? (getKeyBindingMap().lookupActive(key) != null) : false;
        } catch (Exception ignored) {

        }
        return false;
    }

    public void removeKey(int key) {
        try {
            if (getKeyBindingMap() != null)
                hash.removeKey(hash.lookupActive(key));
        } catch (Exception ignored) {

        }
    }

    public void removeKey(KeyBinding key) {
        try {
            if (getKeyBindingMap() != null)
                hash.removeKey(key);
        } catch (Exception ignored) {

        }
    }
}