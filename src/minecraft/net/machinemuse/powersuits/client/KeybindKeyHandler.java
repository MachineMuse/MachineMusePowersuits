package net.machinemuse.powersuits.client;

import java.util.EnumSet;

import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeybindKeyHandler extends KeyHandler {
	public static KeyBinding openKeybindGUI = new KeyBinding("Open Muse Keybind GUI", Keyboard.KEY_K);

	public KeybindKeyHandler() {
		super(new KeyBinding[] { openKeybindGUI }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "machineMuseKeybinds";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (kb.equals(openKeybindGUI)) {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			World world = Minecraft.getMinecraft().theWorld;
			if (Minecraft.getMinecraft().inGameHasFocus) {
				player.openGui(ModularPowersuits.instance, 1, world, 0, 0, 0);
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// TODO Auto-generated method stub

	}

	public void addKeybind(KeyBinding kb) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
