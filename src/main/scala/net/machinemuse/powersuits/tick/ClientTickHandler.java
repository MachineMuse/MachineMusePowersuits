package net.machinemuse.powersuits.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import java.util.EnumSet;

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *independent of rendering, so don't do rendering here
 * -is also the parent class of KeyBindingHandler
 *
 * @author MachineMuse
 */
public class ClientTickHandler implements ITickHandler {
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
            kb.doToggleTick();
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null && MuseItemUtils.getModularItemsInInventory(player).size() > 0) {
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
                MusePacket inputPacket = new MusePacketPlayerUpdate(player, inputmap);
                PacketSender.sendToServer(inputPacket.getPacket131());
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
