package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.network.MusePacketHandler;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 PM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 10/28/16.
 */
public class MPSPacketList {
    static final MusePacketHandler handler = MusePacketHandler.getInstance();

    public static void registerPackets() {
//        handler.addPackager( MusePacketInventoryRefresh.getPackagerInstance() );
        handler.addPackager(MusePacketInstallModuleRequest.getPackagerInstance());
        handler.addPackager(MusePacketSalvageModuleRequest.getPackagerInstance());
        handler.addPackager(MusePacketTweakRequestDouble.getPackagerInstance());
//        handler.addPackager( MusePacketTweakRequestInteger.getPackagerInstance() );

        handler.addPackager(MusePacketCosmeticInfo.getPackagerInstance());
        handler.addPackager(MusePacketPlayerUpdate.getPackagerInstance());
        handler.addPackager(MusePacketToggleRequest.getPackagerInstance());
//        handler.addPackage( MusePacketPlasmaBolt.getPackagerInstance() );
        handler.addPackager(MusePacketColourInfo.getPackagerInstance());
//        handler.addPackager( MusePacketPropertyModifierConfig.getPackagerInstance() );
        handler.addPackager(MPSPacketConfig.getPackagerInstance());
        handler.addPackager(MusePacketCosmeticPresetUpdate.getPackagerInstance());
    }
}