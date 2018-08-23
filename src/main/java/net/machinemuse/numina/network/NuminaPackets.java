package net.machinemuse.numina.network;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:20 PM, 9/6/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public class NuminaPackets {
    static final MusePacketHandler handler = MusePacketHandler.getInstance();

    public static void init() {
        handler.addPackager(MusePacketNameChangeRequest.getPackagerInstance());
        handler.addPackager(MusePacketModeChangeRequest.getPackagerInstance());
        handler.addPackager(MusePacketModeChangeRequest.getPackagerInstance());
        handler.addPackager(MusePacketRecipeUpdate.getPackagerInstance());
        handler.addPackager(NuminaPacketConfig.getPackagerInstance());
    }
}
