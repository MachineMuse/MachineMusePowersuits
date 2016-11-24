package net.machinemuse.powersuits.control;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Created by leon on 7/4/16.
 */
public class KeyBindingHelper {
    public boolean keyBindingHasKey(int key) {
        try {
            KeyBindingMap hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "field_74514_b");
            return hash.lookupActive(key)!=null;
        }
        catch (Exception e) {
        }
        return false;
    }

    public void removeKey(int key) {
        try {
            KeyBindingMap hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "field_74514_b");
            hash.removeKey(hash.lookupActive(key));
        }
        catch (Exception e) {
        }
    }
}