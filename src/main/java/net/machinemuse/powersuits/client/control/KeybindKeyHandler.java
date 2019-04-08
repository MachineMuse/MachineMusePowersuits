package net.machinemuse.powersuits.client.control;

import net.machinemuse.numina.capabilities.player.CapabilityPlayerKeyStates;
import net.machinemuse.numina.capabilities.player.IPlayerKeyStates;
import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.numina.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.client.gui.GuiModeSelector;
import net.machinemuse.powersuits.client.gui.tinker.CosmeticGui;
import net.machinemuse.powersuits.client.gui.tinker.KeyConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import static java.util.Optional.empty;

@OnlyIn(Dist.CLIENT)
public class KeybindKeyHandler {
    Minecraft mc;

    public static final String mps = "Modular ModularPowersuits";
    public static final KeyBinding openKeybindGUI = new KeyBinding("Open MPS Keybind GUI", GLFW.GLFW_KEY_UNKNOWN, mps);
    public static final KeyBinding goDownKey = new KeyBinding("Go Down (MPS Flight Control)", GLFW.GLFW_KEY_Z, mps);
    public static final KeyBinding cycleToolBackward = new KeyBinding("Cycle Tool Backward (MPS)", GLFW.GLFW_KEY_UNKNOWN, mps);
    public static final KeyBinding cycleToolForward = new KeyBinding("Cycle Tool Forward (MPS)", GLFW.GLFW_KEY_UNKNOWN, mps);
    public static final KeyBinding openCosmeticGUI = new KeyBinding("Cosmetic (MPS)", GLFW.GLFW_KEY_UNKNOWN, mps);
    public static final KeyBinding[] keybindArray = new KeyBinding[]{openKeybindGUI, goDownKey, cycleToolBackward, cycleToolForward, openCosmeticGUI};

    public KeybindKeyHandler() {
        mc = Minecraft.getInstance();
        for (KeyBinding key : keybindArray) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    void updatePlayerValues(EntityPlayerSP clientPlayer) {
        boolean markForSync = false;
        boolean downKeyState = goDownKey.isKeyDown();
        boolean jumpKeyState = mc.gameSettings.keyBindJump.isKeyDown();

        LazyOptional<IPlayerKeyStates> playerCap = clientPlayer.getCapability(CapabilityPlayerKeyStates.PLAYER_KEYSTATES, null);
        if (playerCap.isPresent()) {
            if (playerCap.map(m -> m.getDownKeyState() != downKeyState).orElse(false)) {
                playerCap.map(m -> {
                    m.setDownKeyState(downKeyState);
                    return empty();
                });
                markForSync = true;
            }

            if (playerCap.map(m -> m.getJumpKeyState() != jumpKeyState).orElse(false)) {
                playerCap.map(m -> {
                    m.setJumpKeyState(jumpKeyState);
                    return empty();
                });
                markForSync = true;
            }

            if (markForSync) {
                NuminaPackets.sendToServer(new MusePacketPlayerUpdate(clientPlayer.getEntityId(), downKeyState, jumpKeyState));
            }
        }
    }

//    @SubscribeEvent
//    public void onKeyInput(InputEvent.KeyInputEvent e) {

    public void checkPlayerKeys() {
        EntityPlayerSP player = mc.player;
        KeyBinding[] hotbarKeys = mc.gameSettings.keyBindsHotbar;
        updatePlayerValues(player);

        if (openKeybindGUI.isKeyDown() && mc.isGameFocused())
            mc.displayGuiScreen(new KeyConfigGui(player));

        if (openCosmeticGUI.isKeyDown() && mc.isGameFocused())
            mc.displayGuiScreen(new CosmeticGui(player));

        if (hotbarKeys[player.inventory.currentItem].isKeyDown() && mc.isGameFocused())
            mc.displayGuiScreen(new GuiModeSelector(player));

        /* cycleToolBackward/cycleToolForward */
        if (cycleToolBackward.isKeyDown()) {
            mc.playerController.tick();
            ModuleManager.INSTANCE.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem),player, 1);
        }

        if (cycleToolForward.isKeyDown()) {
            mc.playerController.tick();
            ModuleManager.INSTANCE.cycleMode(player.inventory.getStackInSlot(player.inventory.currentItem),player, -1);
        }
    }
}