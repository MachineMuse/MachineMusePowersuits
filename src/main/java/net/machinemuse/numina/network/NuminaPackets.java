package net.machinemuse.numina.network;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:20 PM, 9/6/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public class NuminaPackets {
    public static void init() {
        MusePacketHandler.packagers.put(20, MusePacketNameChangeRequest.MusePacketNameChangeRequestPackager.INSTANCE);
        MusePacketHandler.packagers.put(21, MusePacketModeChangeRequest.MusePacketModeChangeRequestPackager.INSTANCE);
        MusePacketHandler.packagers.put(22, MusePacketRecipeUpdate.MusePacketRecipeUpdatePackager.INSTANCE);
        MusePacketHandler.packagers.put(23, NuminaPacketConfig.MusePacketConfigPackager.INSTANCE);
    }
}
