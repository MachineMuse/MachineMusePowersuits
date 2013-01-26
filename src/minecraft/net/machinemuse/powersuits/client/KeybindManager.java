package net.machinemuse.powersuits.client;

import java.util.List;

import net.minecraft.client.settings.KeyBinding;

public class KeybindManager {

	public List<KeyBinding> getKeybindings() {
		// IntHashMap map = KeyBinding.hash;
		// Set<Integer> keys = map.getKeySet();
		// List<KeyBinding> bindings = new ArrayList();
		// for (Integer key : keys) {
		// bindings.add((KeyBinding) map.lookup(key));
		// }
		return KeyBinding.keybindArray;
	}

	public void addKeybinding(KeyBinding binding) {

	}

}
