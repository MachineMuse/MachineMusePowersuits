package net.machinemuse.powersuits.control;

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
	public static KeyBinding goDownKey = new KeyBinding("Go Down (Flight Control)", Keyboard.KEY_Z);

	public KeybindKeyHandler() {
		super(new KeyBinding[] { openKeybindGUI, goDownKey }, new boolean[] { false, false });
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
		if (kb.equals(goDownKey)) {
			PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.username).downKey = true;
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if (kb.equals(goDownKey)) {
			PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.username).downKey = false;
		}
	}

	public void addKeybind(KeyBinding kb) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
