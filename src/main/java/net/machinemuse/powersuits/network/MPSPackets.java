package net.machinemuse.powersuits.network;

import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.powersuits.network.packets.*;
import net.machinemuse.powersuits.network.packets_old.MusePacketCosmeticPresetUpdate;
import net.machinemuse.powersuits.network.packets_old.MusePacketPlayerUpdate;
import net.machinemuse.powersuits.network.packets_old.MusePacketTweakRequestDouble;
import net.minecraftforge.fml.relauncher.Side;

public class MPSPackets extends NuminaPackets {

    public static void registerMPSPackets() {
        INSTANCE.registerMessage(MPSPacketConfig.Handler.class, MPSPacketConfig.class, 10, Side.CLIENT);
        INSTANCE.registerMessage(MusePacketInstallModuleRequest.Handler.class, MusePacketInstallModuleRequest.class, 11, Side.SERVER);
        INSTANCE.registerMessage(MusePacketSalvageModuleRequest.Handler.class, MusePacketSalvageModuleRequest.class, 12, Side.SERVER);
        INSTANCE.registerMessage(MusePacketColourInfo.Handler.class, MusePacketColourInfo.class, 13, Side.SERVER);
        INSTANCE.registerMessage(MusePacketCosmeticInfo.Handler.class, MusePacketCosmeticInfo.class, 14, Side.SERVER);
        INSTANCE.registerMessage(MusePacketToggleRequest.Handler.class, MusePacketToggleRequest.class, 15, Side.SERVER);



        handler.addPackager(MusePacketTweakRequestDouble.getPackagerInstance());
        handler.addPackager(MusePacketPlayerUpdate.getPackagerInstance());

        handler.addPackager(MusePacketCosmeticPresetUpdate.getPackagerInstance());
    }
}
