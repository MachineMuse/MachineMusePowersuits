package net.machinemuse.powersuits.network;

import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.powersuits.network.packets.*;

public class MPSPackets extends NuminaPackets {
    public static void registerMPSPackets() {
        CHANNEL_INSTANCE.registerMessage(
                5,
                MusePacketColourInfo.class,
                MusePacketColourInfo::encode,
                MusePacketColourInfo::decode,
                MusePacketColourInfo::handle);

        CHANNEL_INSTANCE.registerMessage(
                6,
                MusePacketCosmeticInfo.class,
                MusePacketCosmeticInfo::encode,
                MusePacketCosmeticInfo::decode,
                MusePacketCosmeticInfo::handle);

        CHANNEL_INSTANCE.registerMessage(
                7,
                MusePacketCosmeticPreset.class,
                MusePacketCosmeticPreset::encode,
                MusePacketCosmeticPreset::decode,
                MusePacketCosmeticPreset::handle);

        CHANNEL_INSTANCE.registerMessage(
                8,
                MusePacketCosmeticPresetUpdate.class,
                MusePacketCosmeticPresetUpdate::encode,
                MusePacketCosmeticPresetUpdate::decode,
                MusePacketCosmeticPresetUpdate::handle);

        CHANNEL_INSTANCE.registerMessage(
                9,
                MusePacketInstallModuleRequest.class,
                MusePacketInstallModuleRequest::encode,
                MusePacketInstallModuleRequest::decode,
                MusePacketInstallModuleRequest::handle);

        CHANNEL_INSTANCE.registerMessage(
                10,
                MusePacketSalvageModuleRequest.class,
                MusePacketSalvageModuleRequest::encode,
                MusePacketSalvageModuleRequest::decode,
                MusePacketSalvageModuleRequest::handle);

        CHANNEL_INSTANCE.registerMessage(
                11,
                MusePacketTweakRequestDouble.class,
                MusePacketTweakRequestDouble::encode,
                MusePacketTweakRequestDouble::decode,
                MusePacketTweakRequestDouble::handle);
    }
}