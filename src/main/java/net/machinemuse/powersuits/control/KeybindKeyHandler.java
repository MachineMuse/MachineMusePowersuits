package net.machinemuse.powersuits.control;

import net.machinemuse.numina.capabilities.player.CapabilityPlayerValues;
import net.machinemuse.numina.capabilities.player.IPlayerValues;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
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

    public KeybindKeyHandler() {
        for (KeyBinding key : keybindArray) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    void updatePlayerValues(EntityPlayerSP clientPlayer, Boolean downKeyState, Boolean jumpKeyState) {
        boolean markForSync = false;

        IPlayerValues playerCap = clientPlayer.getCapability(CapabilityPlayerValues.PLAYER_VALUES, null);
        if (playerCap != null) {
            if(downKeyState != null)
                if (playerCap.getDownKeyState() != downKeyState) {
                    playerCap.setDownKeyState(downKeyState);
                    markForSync = true;
                }

            if (jumpKeyState != null)
                if (playerCap.getJumpKeyState() != jumpKeyState) {
                    playerCap.setJumpKeyState(jumpKeyState);
                    markForSync = true;
                }

            if (markForSync) {
                MPSPackets.sendToServer(new MusePacketPlayerUpdate(clientPlayer, playerCap.getDownKeyState(), playerCap.getJumpKeyState()));
            }
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
            if (player.inventory.getCurrentItem().getItem() instanceof IModeChangingItem) {
                IModeChangingItem mci = (IModeChangingItem) player.inventory.getCurrentItem().getItem();

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
                    World world = mc.world;
                    if (mc.inGameHasFocus) {
                        player.openGui(ModularPowersuits.getInstance(), 5, world, 0, 0, 0);
                    }
                }
            }

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
            // update down key on server side
            if (key == goDownKey.getKeyCode()) {
                updatePlayerValues(player, true, null);

                // update jump key on server side
            } else if (key == mc.gameSettings.keyBindJump.getKeyCode())
                updatePlayerValues(player, null, true);
        } else {
            // update down key and jump key on server side
            if (key == goDownKey.getKeyCode() || key == mc.gameSettings.keyBindJump.getKeyCode()) {
                updatePlayerValues(player, false, false);
            }
        }
    }
}