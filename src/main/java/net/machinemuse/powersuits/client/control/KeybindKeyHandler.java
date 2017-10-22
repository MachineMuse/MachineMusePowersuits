package net.machinemuse.powersuits.client.control;

import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.item.ModeChangingItem;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeybindKeyHandler {
    public static final String mps = "Modular Powersuits";
    public static final KeyBinding openKeybindGUI = new KeyBinding("Open MPS Keybind GUI", -1, mps);
    public static final KeyBinding goDownKey = new KeyBinding("Go Down (MPS Flight Control)", Keyboard.KEY_Z, mps);
    public static final KeyBinding cycleToolBackward = new KeyBinding("Cycle Tool Backward (MPS)", -1, mps);
    public static final KeyBinding cycleToolForward = new KeyBinding("Cycle Tool Forward (MPS)", -1, mps);
    public static final KeyBinding openCosmeticGUI = new KeyBinding("Cosmetic (MPS)", -1, mps);
    public static final KeyBinding[] keybindArray = new KeyBinding[]{openKeybindGUI, goDownKey, cycleToolBackward, cycleToolForward, openCosmeticGUI};
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
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
    	KeyBinding[] hotbarKeys = mc.gameSettings.keyBindsHotbar;

        // Only activate if there is a player to work with
        if (player == null) {
            return;
        }
        if (pressed) {
            ModeChangingItem mci = new ModeChangingItem(player.inventory.getCurrentItem());
            if (key == openKeybindGUI.getKeyCode()) {
                World world = mc.world;
                if (mc.inGameHasFocus) {
                    player.openGui(ModularPowersuits.getInstance(), 1, world, 0, 0, 0);
                }
            }
            if (key == openCosmeticGUI.getKeyCode()) {
                World world = mc.world;
                if (mc.inGameHasFocus) {
                    player.openGui(ModularPowersuits.getInstance(), 3, world, 0, 0, 0);
                }
            }
            if (key == goDownKey.getKeyCode()) {
                PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName()).downKey = true; // TODO: is this correct?
            }

            /* cycleToolBackward/cycleToolForward only seem to be used if actual keys are assigned instead of mouse-wheel */
            if (key == cycleToolBackward.getKeyCode()) {
                mc.playerController.updateController();
                mci.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, 1);

            }
            if (key == cycleToolForward.getKeyCode()) {
                mc.playerController.updateController();
                mci.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, -1);
            }
            if (player.inventory.currentItem < hotbarKeys.length && key == hotbarKeys[player.inventory.currentItem].getKeyCode()) {
            	ItemStack stack = player.inventory.getCurrentItem();
            	if (stack != null && stack.getItem() instanceof IModeChangingItem) {
                    World world = mc.world;
                    if (mc.inGameHasFocus) {
                    	player.openGui(ModularPowersuits.getInstance(), 5, world, 0, 0, 0);
                    }
            	}
            }
        } else {
            if (player != null && key == goDownKey.getKeyCode()) {
                PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName()).downKey = false;
            }
        }
    }
}