package net.machinemuse.powersuits.network.packets_old;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataOutputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketPlayerUpdate extends MusePacket {
    EntityPlayer player;
    PlayerInputMap inputMap;

    public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap inputMap) {
        this.player = player;
        this.inputMap = inputMap;
    }

    public static MusePacketPlayerUpdatePackager getPackagerInstance() {
        return MusePacketPlayerUpdatePackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeString(player.getCommandSenderEntity().getName());
//        inputMap.writeToStream(dataout());
        inputMap.writeToStream(new DataOutputStream(bytesOut()));
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
        player.motionX = inputMap.motionX;
        player.motionY = inputMap.motionY;
        player.motionZ = inputMap.motionZ;
        PacketSender.sendToAllAround(updatePacket, player, 128);
    }

    public enum MusePacketPlayerUpdatePackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            String username = readString(datain);
            PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(username);
            inputMap.readFromStream(datain);
            return new MusePacketPlayerUpdate(player, inputMap);
        }
    }
}