package net.machinemuse.powersuits.control;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.IModeChangingModularItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeybindKeyHandler {
    public static final String mps = "Modular Powersuits";
    public static KeyBinding openKeybindGUI = new KeyBinding("Open MPS Keybind GUI", -1, mps);
    public static KeyBinding goDownKey = new KeyBinding("Go Down (MPS Flight Control)", Keyboard.KEY_Z, mps);
    public static KeyBinding cycleToolBackward = new KeyBinding("Cycle Tool Backward (MPS)", -1, mps);
    public static KeyBinding cycleToolForward = new KeyBinding("Cycle Tool Forward (MPS)", -1, mps);
    public static KeyBinding zoom = new KeyBinding("Zoom (MPS)", Keyboard.KEY_Y, mps);
    public static KeyBinding openCosmeticGUI = new KeyBinding("Cosmetic (MPS)", -1, mps);
    public static KeyBinding[] keybindArray = new KeyBinding[]{openKeybindGUI, goDownKey, cycleToolBackward, cycleToolForward, zoom, openCosmeticGUI};
    public static boolean[] repeats = new boolean[keybindArray.length];

    public KeybindKeyHandler() {
        for (KeyBinding key : keybindArray) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        int key = Keyboard.getEventKey();
        boolean pressed = Keyboard.getEventKeyState();
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        IModeChangingModularItem modeChangingModularItem = null;
        if (stack != null && stack.getItem() instanceof IModeChangingModularItem)
            modeChangingModularItem = (IModeChangingModularItem)stack.getItem();

        // Only activate if there is a player to work with
        if (player == null) {
            return;
        }
        if (pressed) {
            if (key == openKeybindGUI.getKeyCode()) {
                World world = Minecraft.getMinecraft().theWorld;
                if (Minecraft.getMinecraft().inGameHasFocus) {
                    player.openGui(ModularPowersuits.getInstance(), 1, world, 0, 0, 0);
                }
            }
            if (key == openCosmeticGUI.getKeyCode()) {
                World world = Minecraft.getMinecraft().theWorld;
                if (Minecraft.getMinecraft().inGameHasFocus) {
                    player.openGui(ModularPowersuits.getInstance(), 3, world, 0, 0, 0);
                }
            }
            if (key == goDownKey.getKeyCode()) {
                PlayerInputMap.getInputMapFor(player.getCommandSenderName()).downKey = true;
            }
            if (key == cycleToolBackward.getKeyCode() && modeChangingModularItem != null) {
                Minecraft.getMinecraft().playerController.updateController();
                ((IModeChangingModularItem)modeChangingModularItem).cycleModeForItem(player.inventory.getStackInSlot(player.inventory.currentItem), player, 1);
            }
            if (key == cycleToolForward.getKeyCode() && modeChangingModularItem != null) {
                Minecraft.getMinecraft().playerController.updateController();
                ((IModeChangingModularItem)modeChangingModularItem).cycleModeForItem(player.inventory.getStackInSlot(player.inventory.currentItem), player, -1);
            }
        } else {
            if (Minecraft.getMinecraft().thePlayer != null && key == goDownKey.getKeyCode()) {
                PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.getCommandSenderName()).downKey = false;
            }
        }
    }
}
