package net.machinemuse.powersuits.control;

import java.util.EnumSet;

import net.machinemuse.api.MuseItemUtils;
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
	public static KeyBinding cycleToolBackward = new KeyBinding("Cycle Tool Backward", -1);
	public static KeyBinding cycleToolForward = new KeyBinding("Cycle Tool Forward", -1);

	public KeybindKeyHandler() {
		super(new KeyBinding[] { openKeybindGUI, goDownKey, cycleToolBackward, cycleToolForward }, new boolean[] { false, false, false, false });
	}

	@Override
	public String getLabel() {
		return "machineMuseKeybinds";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		// Only activate if there is a player to work with and it is the start
		// tick
		if (player == null || tickEnd) {
			return;
		}
		if (kb.equals(openKeybindGUI)) {
			World world = Minecraft.getMinecraft().theWorld;
			if (Minecraft.getMinecraft().inGameHasFocus) {
				player.openGui(ModularPowersuits.instance, 1, world, 0, 0, 0);
			}
		}
		if (kb.equals(goDownKey)) {
			PlayerInputMap.getInputMapFor(player.username).downKey = true;
		}
		if (kb.equals(cycleToolBackward)) {
			Minecraft.getMinecraft().playerController.updateController();
			MuseItemUtils.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, 1);
		}
		if (kb.equals(cycleToolForward)) {
			Minecraft.getMinecraft().playerController.updateController();
			MuseItemUtils.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, -1);
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
