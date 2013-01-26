package net.machinemuse.powersuits.client;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class KeybindManager {
	// only stores keybindings relevant to us!!
	protected List<ClickableKeybinding> keybindings;
	protected static KeybindManager instance;

	protected KeybindManager() {
		keybindings = new ArrayList();
	}

	public static KeybindManager getInstance() {
		if (instance == null) {
			instance = new KeybindManager();
		}
		return instance;
	}

	public static List<ClickableKeybinding> getKeybindings() {
		return getInstance().keybindings;
	}

	public static KeyBinding addKeybinding(String keybindDescription, int keycode, MusePoint2D position) {
		KeyBinding kb = new KeyBinding(keybindDescription, keycode);
		getInstance().keybindings.add(new ClickableKeybinding(kb, position));
		return kb;
	}

	public static String parseName(KeyBinding keybind) {
		if (keybind.keyCode < 0) {
			return "Mouse" + (keybind.keyCode + 100);
		} else {
			return Keyboard.getKeyName(keybind.keyCode);
		}
	}
}
