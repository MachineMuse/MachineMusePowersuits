package net.machinemuse.numina.event;

import net.machinemuse.numina.common.config.NuminaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:07 PM, 10/17/13
 * <p>
 * Ported to Java by lehjr on 10/10/16.
 */

@OnlyIn(Dist.CLIENT)
public class FOVUpdateEventHandler {
    public static KeyBinding fovToggleKey = new KeyBinding(I18n.format("keybind.fovfixtoggle"), Keyboard.KEY_NONE, "Numina");
    public boolean fovIsActive = NuminaConfig.fovFixDefaultState();

    public FOVUpdateEventHandler() {
        ClientRegistry.registerKeyBinding(fovToggleKey);
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent e) {
        if (NuminaConfig.useFOVFix() && fovIsActive) {
            IAttributeInstance attributeinstance = e.getEntity().getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            e.setNewfov((float) (e.getNewfov() / ((attributeinstance.getValue() / e.getEntity().abilities.getWalkSpeed() + 1.0) / 2.0)));
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (NuminaConfig.useFOVFix()) {
            EntityPlayerSP player = Minecraft.getInstance().player;
            if (fovToggleKey.isPressed()) {
                fovIsActive = !fovIsActive;
                if (fovIsActive)
                    player.sendMessage(new TextComponentString(I18n.format("fovfixtoggle.enabled")));
                else
                    player.sendMessage(new TextComponentString(I18n.format("fovfixtoggle.disabled")));
            }
        }
    }
}