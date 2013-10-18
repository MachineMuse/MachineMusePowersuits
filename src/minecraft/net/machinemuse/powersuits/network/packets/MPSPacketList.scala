package net.machinemuse.powersuits.network.packets

import net.machinemuse.numina.network.{MusePacketModeChangeRequest, MusePacketHandler}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 PM, 9/3/13
 */
object MPSPacketList {
  def registerPackets() {
    MusePacketHandler.packagers.put(1, MusePacketInventoryRefresh)
    MusePacketHandler.packagers.put(2, MusePacketInstallModuleRequest)
    MusePacketHandler.packagers.put(3, MusePacketSalvageModuleRequest)
    MusePacketHandler.packagers.put(4, MusePacketTweakRequest)
    MusePacketHandler.packagers.put(5, MusePacketCosmeticInfo)
    MusePacketHandler.packagers.put(6, MusePacketPlayerUpdate)
    MusePacketHandler.packagers.put(7, MusePacketToggleRequest)
    MusePacketHandler.packagers.put(8, MusePacketPlasmaBolt)
    MusePacketHandler.packagers.put(10, MusePacketColourInfo)
    MusePacketHandler.packagers.put(11, MusePacketPropertyModifierConfig)
  }
}
