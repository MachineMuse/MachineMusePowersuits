package net.machinemuse.powersuits.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;
import net.machinemuse.api.IModularItem;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.util.EnumSet;

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *independent of rendering, so sometimes there
 * might be visual artifacts* -is also the parent class of KeyBindingHandler
 *
 * @author MachineMuse
 */
public class ClientTickHandler implements ITickHandler {
    protected int slotSelected = -1;
    public static int dWheel;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
            kb.doToggleTick();
        }
        if (Config.useMouseWheel()) {
            dWheel = Mouse.getDWheel() / 120;
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IModularItem
                    && player.isSneaking()) {
                slotSelected = player.inventory.currentItem;
            } else {
                slotSelected = -1;
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null && MuseItemUtils.getModularItemsInInventory(player).size() > 0) {
            if (slotSelected > -1 && dWheel != Mouse.getDWheel()) {
                player.inventory.currentItem = slotSelected;
                Minecraft.getMinecraft().playerController.updateController();
                ItemStack stack = player.inventory.getStackInSlot(slotSelected);
                MuseItemUtils.cycleMode(stack, player, dWheel - Mouse.getDWheel());
            }
            slotSelected = -1;
            PlayerInputMap inputmap = PlayerInputMap.getInputMapFor(player.username);
            inputmap.forwardKey = Math.signum(player.movementInput.moveForward);
            inputmap.strafeKey = Math.signum(player.movementInput.moveStrafe);
            inputmap.jumpKey = player.movementInput.jump;
            inputmap.sneakKey = player.movementInput.sneak;
            inputmap.motionX = player.motionX;
            inputmap.motionY = player.motionY;
            inputmap.motionZ = player.motionZ;

            if (inputmap.hasChanged()) {
                inputmap.refresh();
                MusePacket inputPacket = new MusePacketPlayerUpdate((Player) player, inputmap);
                player.sendQueue.addToSendQueue(inputPacket.getPacket250());
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "MMMPS: Client Tick";
    }

}
