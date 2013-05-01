package net.machinemuse.powersuits.control;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;

@SideOnly(Side.CLIENT)
public class KeybindKeyHandler extends KeyHandler {
    public static KeyBinding openKeybindGUI = new KeyBinding("Open MPS Keybind GUI", Keyboard.KEY_K);
    public static KeyBinding goDownKey = new KeyBinding("Go Down (MPS Flight Control)", Keyboard.KEY_Z);
    public static KeyBinding cycleToolBackward = new KeyBinding("Cycle Tool Backward (MPS)", -1);
    public static KeyBinding cycleToolForward = new KeyBinding("Cycle Tool Forward (MPS)", -1);
    public static KeyBinding zoom = new KeyBinding("Zoom (MPS)", Keyboard.KEY_Y);
    public static KeyBinding openCosmeticGUI = new KeyBinding("Cosmetic (MPS)", Keyboard.KEY_L);
    public static KeyBinding[] keybindArray = new KeyBinding[]{openKeybindGUI, goDownKey, cycleToolBackward, cycleToolForward, zoom, openCosmeticGUI};
    public static boolean[] repeats = new boolean[keybindArray.length];

    public KeybindKeyHandler() {
        super(keybindArray, repeats);
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
        if (kb.equals(openCosmeticGUI)) {
            World world = Minecraft.getMinecraft().theWorld;
            if (Minecraft.getMinecraft().inGameHasFocus) {
                player.openGui(ModularPowersuits.instance, 3, world, 0, 0, 0);
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
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (Minecraft.getMinecraft().thePlayer != null && kb.equals(goDownKey)) {
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
