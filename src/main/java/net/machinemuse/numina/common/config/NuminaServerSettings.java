package net.machinemuse.numina.common.config;

import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;

import java.io.DataInputStream;

public class NuminaServerSettings {
    public final double mekRatio;
    public final double ic2Ratio;
    public final double rsRatio;
    public final double ae2Ratio;

    public final int maxTier1;
    public final int maxTier2;
    public final int maxTier3;
    public final int maxTier4;

    public NuminaServerSettings() {
        mekRatio = NuminaSettings.mekRatio;
        ic2Ratio = NuminaSettings.ic2Ratio;
        rsRatio = NuminaSettings.rsRatio;
        ae2Ratio = NuminaSettings.ae2Ratio;

        maxTier1 = NuminaSettings.maxTier1;
        maxTier2 = NuminaSettings.maxTier2;
        maxTier3 = NuminaSettings.maxTier3;
        maxTier4 = NuminaSettings.maxTier4;
    }

    public NuminaServerSettings(final DataInputStream datain) {
        mekRatio= MusePackager.getInstance().readDouble(datain);
        ic2Ratio= MusePackager.getInstance().readDouble(datain);
        rsRatio= MusePackager.getInstance().readDouble(datain);
        ae2Ratio = MusePackager.getInstance().readDouble(datain);

        maxTier1 = MusePackager.getInstance().readInt(datain);
        maxTier2 = MusePackager.getInstance().readInt(datain);
        maxTier3 = MusePackager.getInstance().readInt(datain);
        maxTier4 = MusePackager.getInstance().readInt(datain);
    }

    /**
     * This is a server side operation that gets the values and writes them to the packet.
     * This packet is then sent to a new client on login to sync config values. This allows
     * the server to be able to control these settings.
     */
    public void writeToBuffer(final MusePacket packet) {
        packet.writeDouble(mekRatio);
        packet.writeDouble(ic2Ratio);
        packet.writeDouble(rsRatio);
        packet.writeDouble(ae2Ratio);

        packet.writeInt(maxTier1);
        packet.writeInt(maxTier2);
        packet.writeInt(maxTier3);
        packet.writeInt(maxTier4);
    }
}
