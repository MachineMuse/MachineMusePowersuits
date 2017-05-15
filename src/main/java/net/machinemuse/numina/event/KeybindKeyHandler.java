package net.machinemuse.numina.event;

import net.machinemuse.numina.common.NuminaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by leon on 10/17/16.
 *
 * Yes, a Keybind handler for a single key
 */
@SideOnly(Side.CLIENT)
public class KeybindKeyHandler {
    public static KeyBinding fovToggleKey = new KeyBinding(I18n.format("keybind.fovfixtoggle"), Keyboard.KEY_NONE, "Numina");
    public boolean fovIsActive = NuminaConfig.fovFixDefaultState();

    public KeybindKeyHandler() {
        ClientRegistry.registerKeyBinding(fovToggleKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (fovToggleKey.isPressed()){
            fovIsActive = !fovIsActive;
            if (fovIsActive)
                player.sendMessage(new TextComponentString(I18n.format("fovfixtoggle.enabled")));
            else
                player.sendMessage(new TextComponentString(I18n.format("fovfixtoggle.disabled")));
        }
    }
}
