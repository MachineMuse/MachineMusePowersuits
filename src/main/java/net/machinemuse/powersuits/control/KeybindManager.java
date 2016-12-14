package net.machinemuse.powersuits.control;

import cpw.mods.fml.common.Loader;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        KeyBinding kb = new KeyBinding(keybindDescription, keycode, KeybindKeyHandler.mps);
        boolean free = !KeyBinding.hash.containsItem(keycode);
        getInstance().keybindings.add(new ClickableKeybinding(kb, position, free, false));
        return kb;
    }

    public static String parseName(KeyBinding keybind) {
        if (keybind.getKeyCode() < 0) {
            return "Mouse" + (keybind.getKeyCode() + 100);
        } else {
            return Keyboard.getKeyName(keybind.getKeyCode());
        }
    }

    public static void writeOutKeybinds() {
        BufferedWriter writer = null;
        try {
            File file = new File(Loader.instance().getConfigDir() + "/machinemuse/", "powersuits-keybinds.cfg");
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            List<IPowerModule> modulesToWrite = MuseItemUtils.getPlayerInstalledModules(Minecraft.getMinecraft().thePlayer);
            for (ClickableKeybinding keybinding : getInstance().keybindings) {
                writer.write(keybinding.getKeyBinding().getKeyCode() + ":" + keybinding.getPosition().x() + ':' + keybinding.getPosition().y() + ':' + keybinding.displayOnHUD + ':' + keybinding.toggleval + '\n');
                for (ClickableModule module : keybinding.getBoundModules()) {
                    writer.write(module.getModule().getDataName() + '~' + module.getPosition().x() + '~' + module.getPosition().y() + '\n');
                }
            }
        } catch (Exception e) {
            MuseLogger.logError("Problem writing out keyconfig :(");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Throwable ignored) {
            }
        }
    }

    public static void readInKeybinds() {
        try {
            File file = new File(Loader.instance().getConfigDir() + "/machinemuse/", "powersuits-keybinds.cfg");
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
                        boolean free = !KeyBinding.hash.containsItem(id);
                        boolean displayOnHUD = false;
                        boolean toggleval = false;
                        if(exploded.length > 3) {
                            displayOnHUD = Boolean.parseBoolean(exploded[3]);
                        }
                        if(exploded.length > 4) {
                            toggleval = Boolean.parseBoolean(exploded[4]);
                        }
                        workingKeybinding = new ClickableKeybinding(new KeyBinding(Keyboard.getKeyName(id), id, KeybindKeyHandler.mps), position, free, displayOnHUD);
                        workingKeybinding.toggleval = toggleval;
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
