package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.network.MusePacketHandler;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 PM, 9/3/13
 *
 * Ported to Java by lehjr on 10/28/16.
 */
public class MPSPacketList {
    static {
        new MPSPacketList();
    }

    public static void registerPackets() {
        MusePacketHandler.packagers.put(1, MusePacketInventoryRefresh.getPackagerInstance());
        MusePacketHandler.packagers.put(2, MusePacketInstallModuleRequest.getPackagerInstance());
        MusePacketHandler.packagers.put(3, MusePacketSalvageModuleRequest.getPackagerInstance());
        MusePacketHandler.packagers.put(4, MusePacketTweakRequest.getPackagerInstance());
        MusePacketHandler.packagers.put(5, MusePacketCosmeticInfo.getPackagerInstance());
        MusePacketHandler.packagers.put(6, MusePacketPlayerUpdate.getPackagerInstance());
        MusePacketHandler.packagers.put(7, MusePacketToggleRequest.getPackagerInstance());
        MusePacketHandler.packagers.put(8, MusePacketPlasmaBolt.getPackagerInstance());
        MusePacketHandler.packagers.put(10, MusePacketColourInfo.getPackagerInstance());
        MusePacketHandler.packagers.put(11, MusePacketPropertyModifierConfig.getPackagerInstance());
    }
}
