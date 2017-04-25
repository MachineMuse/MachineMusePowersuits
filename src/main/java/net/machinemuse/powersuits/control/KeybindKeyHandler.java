package net.machinemuse.powersuits.control;

import net.machinemuse.numina.item.ModeChangingItem;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
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
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    	KeyBinding[] hotbarKeys = Minecraft.getMinecraft().gameSettings.keyBindsHotbar;

        // Only activate if there is a player to work with
        if (player == null) {
            return;
        }
        if (pressed) {
            ModeChangingItem mci = new ModeChangingItem(player.inventory.getCurrentItem());
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
                PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName()).downKey = true; // TODO: is this correct?
            }

            /* cycleToolBackward/cycleToolForward only seem to be used if actual keys are assinged instead of mousewheel */
            if (key == cycleToolBackward.getKeyCode()) {
                Minecraft.getMinecraft().playerController.updateController();
                mci.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, 1);

            }
            if (key == cycleToolForward.getKeyCode()) {
                Minecraft.getMinecraft().playerController.updateController();
                mci.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem), player, -1);
            }
            //Originally this was hard-coded to "key >1 && key < 11", 
            //but this way will still work even if the keycodes for these keys change.
            if (key == hotbarKeys[0].getKeyCode() || key == hotbarKeys[1].getKeyCode() || key == hotbarKeys[2].getKeyCode() || 
            		key == hotbarKeys[3].getKeyCode() || key == hotbarKeys[4].getKeyCode() || key == hotbarKeys[5].getKeyCode() || 
            		key == hotbarKeys[6].getKeyCode() || key == hotbarKeys[7].getKeyCode() || key == hotbarKeys[8].getKeyCode()) {
            	PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.getCommandSenderEntity().getName()).hotbarKey = true;
            }
        } else {
            if (Minecraft.getMinecraft().thePlayer != null && key == goDownKey.getKeyCode()) {
                PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.getCommandSenderEntity().getName()).downKey = false;
            }
            if (key == hotbarKeys[0].getKeyCode() || key == hotbarKeys[1].getKeyCode() || key == hotbarKeys[2].getKeyCode() || 
            		key == hotbarKeys[3].getKeyCode() || key == hotbarKeys[4].getKeyCode() || key == hotbarKeys[5].getKeyCode() || 
            		key == hotbarKeys[6].getKeyCode() || key == hotbarKeys[7].getKeyCode() || key == hotbarKeys[8].getKeyCode()) {
            	PlayerInputMap.getInputMapFor(Minecraft.getMinecraft().thePlayer.getCommandSenderEntity().getName()).hotbarKey = false;
            }
        }
    }
}
/*
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack != null && stack.getItem() instanceof IModeChangingItem) {
            player.inventory.getCurrentItem().getItem() instanceof IModeChangingItem
 */
//player.inventory.currentItem
/*
public static int isHotbarKeyDown() {
  KeyBinding[] bindings = Minecraft.getMinecraft().gameSettings.keyBindsHotbar;
  for (int i = 0; i < bindings.length; i++)
      if (bindings[i].isPressed())
          return i;
  return -1;
}
*/