package net.machinemuse.powersuits.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.powersuits.common.MuseLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Loader;

public class KeybindManager {
	// only stores keybindings relevant to us!!
	protected Set<ClickableKeybinding> keybindings;
	protected static KeybindManager instance;

	protected KeybindManager() {
		keybindings = new HashSet();
	}

	public static KeybindManager getInstance() {
		if (instance == null) {
			instance = new KeybindManager();
		}
		return instance;
	}

	public static Set<ClickableKeybinding> getKeybindings() {
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

	public static void writeOutKeybinds() {
		try {
			File file = new File(Loader.instance().getConfigDir(), "powersuits-keybinds.cfg");
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			List<IPowerModule> modulesToWrite = MuseItemUtils.getPlayerInstalledModules(Minecraft.getMinecraft().thePlayer);
			for (ClickableKeybinding keybinding : getInstance().keybindings) {
				writer.write(keybinding.getKeyBinding().keyCode + ":" + keybinding.getPosition().x() + ":" + keybinding.getPosition().y() + "\n");
				for (ClickableModule module : keybinding.getBoundModules()) {
					writer.write(module.getModule().getName() + "~" + module.getPosition().x() + "~" + module.getPosition().y() + "\n");
				}
			}
			writer.close();
		} catch (Exception e) {
			MuseLogger.logError("Problem writing out keyconfig :(");
			e.printStackTrace();
		}
	}

	public static void readInKeybinds() {
		try {
			File file = new File(Loader.instance().getConfigDir(), "powersuits-keybinds.cfg");
			if (!file.exists()) {
				MuseLogger.logError("No powersuits keybind file found.");
				return;
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			ClickableKeybinding workingKeybinding = null;
			while (reader.ready()) {
				String line = reader.readLine();
				if (line.contains(":")) {
					String[] exploded = line.split(":");
					int id = Integer.parseInt(exploded[0]);
					if (!KeyBinding.hash.containsItem(id)) {
						MusePoint2D position = new MusePoint2D(Double.parseDouble(exploded[1]), Double.parseDouble(exploded[2]));
						workingKeybinding = new ClickableKeybinding(new KeyBinding(Keyboard.getKeyName(id), id), position);
						getInstance().keybindings.add(workingKeybinding);
					} else {
						workingKeybinding = null;
					}

				} else if (line.contains("~") && workingKeybinding != null) {
					String[] exploded = line.split("~");
					MusePoint2D position = new MusePoint2D(Double.parseDouble(exploded[1]), Double.parseDouble(exploded[2]));
					IPowerModule module = ModuleManager.getModule(exploded[0]);
					if (module != null) {
						ClickableModule cmodule = new ClickableModule(module, position);
						workingKeybinding.bindModule(cmodule);
					}

				}
			}
			reader.close();
		} catch (Exception e) {
			MuseLogger.logError("Problem reading in keyconfig :(");
			e.printStackTrace();
		}
	}
}
