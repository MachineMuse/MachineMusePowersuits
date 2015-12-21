package net.machinemuse.powersuits.event

import java.util

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.{ClientTickEvent, RenderTickEvent}
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.machinemuse.general.gui.{WaterMeter, EnergyMeter, HeatMeter}
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.network.{MusePacket, PacketSender}
import net.machinemuse.powersuits.block.BlockTinkerTable
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.control.{KeybindManager, PlayerInputMap}
import net.machinemuse.powersuits.item.{ItemPowerArmorChestplate, ItemPowerArmorHelmet, ItemPowerFist}
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate
import net.machinemuse.powersuits.powermodule.misc.{AutoFeederModule, ClockModule, CompassModule}
import net.machinemuse.utils.render.MuseRenderer
import net.machinemuse.utils.{ElectricItemUtils, MuseHeatUtils, MuseItemUtils, MuseStringUtils, AddonWaterUtils}
import net.machinemuse.powersuits.powermodule.armor.WaterTankModule;

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import net.minecraft.init.Blocks
import net.machinemuse.api.ModuleManager


class ClientTickHandler {
  /**
   * This handler is called before/after the game processes input events and
   * updates the gui state mainly. *independent of rendering, so don't do rendering here
   * -is also the parent class of KeyBindingHandler
   *
   * @author MachineMuse
   */
  @SubscribeEvent def onPreClientTick(event: ClientTickEvent) {
    if (event.phase == TickEvent.Phase.START) {
      import scala.collection.JavaConversions._
      for (kb <- KeybindManager.getKeybindings) {
        kb.doToggleTick
      }
    }
    else {
      val player: EntityClientPlayerMP = Minecraft.getMinecraft.thePlayer
      if (player != null && MuseItemUtils.getModularItemsInInventory(player).size > 0) {
        val inputmap: PlayerInputMap = PlayerInputMap.getInputMapFor(player.getCommandSenderName)
        inputmap.forwardKey = Math.signum(player.movementInput.moveForward)
        inputmap.strafeKey = Math.signum(player.movementInput.moveStrafe)
        inputmap.jumpKey = player.movementInput.jump
        inputmap.sneakKey = player.movementInput.sneak
        inputmap.motionX = player.motionX
        inputmap.motionY = player.motionY
        inputmap.motionZ = player.motionZ
        if (inputmap.hasChanged) {
          inputmap.refresh()
          val inputPacket: MusePacket = new MusePacketPlayerUpdate(player, inputmap)
          PacketSender.sendToServer(inputPacket)
        }
      }
    }
  }

  var modules: util.ArrayList[String] = _

 def findInstalledModules(player: EntityPlayer) {
    if (player != null) {
      val tool = player.getCurrentEquippedItem
      if (tool != null && tool.getItem.isInstanceOf[ItemPowerFist]) {
      }
      val helmet = player.getCurrentArmor(3)
      if (helmet != null && helmet.getItem.isInstanceOf[ItemPowerArmorHelmet]) {
        if (ModuleManager.itemHasActiveModule(helmet, AutoFeederModule.MODULE_AUTO_FEEDER)) {
          modules.add(AutoFeederModule.MODULE_AUTO_FEEDER)
        }
        if (ModuleManager.itemHasActiveModule(helmet, ClockModule.MODULE_CLOCK)) {
          modules.add(ClockModule.MODULE_CLOCK)
        }
        if (ModuleManager.itemHasActiveModule(helmet, CompassModule.MODULE_COMPASS)) {
          modules.add(CompassModule.MODULE_COMPASS)
        }
      }
      val chest = player.getCurrentArmor(2)
      if (chest != null &&
        chest.getItem.isInstanceOf[ItemPowerArmorChestplate]) {
        if (ModuleManager.itemHasActiveModule(chest, WaterTankModule.MODULE_WATER_TANK)) {
          modules.add(WaterTankModule.MODULE_WATER_TANK)
        }
      }
    }
  }


  var yBaseIcon: Double = _
  var yBaseString: Int = _
  if (Config.useGraphicalMeters) {
    yBaseIcon = 150.0
    yBaseString = 155
  } else {
    yBaseIcon = 26.0
    yBaseString = 32
  }

  var food: ItemStack = new ItemStack(Items.cooked_beef)
  var clock: ItemStack = new ItemStack(Items.clock)
  var compass: ItemStack = new ItemStack(Items.compass)
  var yOffsetIcon: Double = 16.0
  var yOffsetString: Int = 18
  var ampm: String = ""
  var drawWaterMeter: Boolean = false

  //@SideOnly(Side.CLIENT) // MPSA - is this needed or not?
  @SubscribeEvent def onRenderTickEvent(event: RenderTickEvent) {
    if (event.phase == TickEvent.Phase.END) {
      val player: EntityPlayer = Minecraft.getMinecraft.thePlayer
      modules = new util.ArrayList[String]()
      findInstalledModules(player)
      if (player != null && MuseItemUtils.modularItemsEquipped(player).size > 0 && Minecraft.getMinecraft.currentScreen == null) {
        val mc: Minecraft = Minecraft.getMinecraft
        val screen: ScaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
        for (i <- 0 until modules.size) {
          if (modules.get(i) == AutoFeederModule.MODULE_AUTO_FEEDER) {
            val foodLevel = MuseItemUtils.getFoodLevel(player.getCurrentArmor(3)).toInt
            val num = MuseStringUtils.formatNumberShort(foodLevel)
            if (i == 0) {
              MuseRenderer.drawString(num, 17, yBaseString)
              MuseRenderer.drawItemAt(-1.0, yBaseIcon, food)
            } else {
              MuseRenderer.drawString(num, 17, yBaseString + (yOffsetString * i))
              MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), food)
            }
          } else if (modules.get(i) == ClockModule.MODULE_CLOCK) {
            val time = player.worldObj.provider.getWorldTime
            var hour = ((time % 24000) / 1000).toInt
            if (Config.use24hClock) {
              if (hour < 19) {
                hour += 6
              } else {
                hour -= 18
              }
              ampm = "h"
            } else {
              if (hour < 6) {
                hour += 6
                ampm = " AM"
              } else if (hour == 6) {
                hour = 12
                ampm = " PM"
              } else if (hour > 6 && hour < 18) {
                hour -= 6
                ampm = " PM"
              } else if (hour == 18) {
                hour = 12
                ampm = " AM"
              } else {
                hour -= 18
                ampm = " AM"
              }
            }
            if (i == 0) {
              MuseRenderer.drawString(hour + ampm, 17, yBaseString)
              MuseRenderer.drawItemAt(-1.0, yBaseIcon, clock)
            } else {
              MuseRenderer.drawString(hour + ampm, 17, yBaseString + (yOffsetString * i))
              MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), clock)
            }
          } else if (modules.get(i) == CompassModule.MODULE_COMPASS) {
            if (i == 0) {
              MuseRenderer.drawItemAt(-1.0, yBaseIcon, compass)
            } else {
              MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), compass)
            }
          } else if (modules.get(i) == WaterTankModule.MODULE_WATER_TANK) {
            drawWaterMeter = true
          }
        }
        drawMeters(player, screen)
      }
    }
  }

  protected var heat: HeatMeter = null
  protected var energy: HeatMeter = null
  protected var water : WaterMeter = null
  private var lightningCounter: Int = 0

  private def drawMeters(player: EntityPlayer, screen: ScaledResolution) {
    val currEnergy: Double = ElectricItemUtils.getPlayerEnergy(player)
    val maxEnergy: Double = ElectricItemUtils.getMaxEnergy(player)
    val currHeat: Double = MuseHeatUtils.getPlayerHeat(player)
    val maxHeat: Double = MuseHeatUtils.getMaxHeat(player)
    val currWater = AddonWaterUtils.getPlayerWater(player)
    val maxWater = AddonWaterUtils.getMaxWater(player)
    val left: Double = screen.getScaledWidth - 30
    val top: Double = screen.getScaledHeight / 2.0 - 16

    // Heat Meter
    val currHeatStr: String = MuseStringUtils.formatNumberShort(currHeat)
    val maxHeatStr: String = MuseStringUtils.formatNumberShort(maxHeat)

    if (Config.useGraphicalMeters) {
      if (heat == null) {
        heat = new HeatMeter
      }
      heat.draw(left + 8, top, currHeat / maxHeat)
      MuseRenderer.drawRightAlignedString(currHeatStr, left - 2, top + 20)
    }
    else {
      MuseRenderer.drawString(currHeatStr + '/' + maxHeatStr + " C", 1, 10)
    }

    // Energy Meter
    if (maxEnergy > 0 && BlockTinkerTable.energyIcon != null) {
      val currEnergyStr: String = MuseStringUtils.formatNumberShort(currEnergy)
      val maxEnergyStr: String = MuseStringUtils.formatNumberShort(maxEnergy)

      if (Config.useGraphicalMeters) {
        if (energy == null) {
          energy = new EnergyMeter
        }
        energy.draw(left, top, currEnergy / maxEnergy)
        MuseRenderer.drawRightAlignedString(currEnergyStr, left - 2, top + 10)
      }
      else {
        MuseRenderer.drawString(currEnergyStr + '/' + maxEnergyStr + " \u1D60", 1, 1)
      }
    }

    // Water Meter
    if (maxWater > 0 && drawWaterMeter ) {
      val currWaterStr: String = MuseStringUtils.formatNumberShort(currWater)
      val maxWaterStr: String = MuseStringUtils.formatNumberShort(maxWater)

      if (Config.useGraphicalMeters) {
        if (water == null) {
          water = new WaterMeter()
        }

        val left: Double = screen.getScaledWidth - 30
        val top: Double = screen.getScaledHeight / 2.0 - 16

        water.draw(left + 16, top, currWater / maxWater)

        MuseRenderer.drawRightAlignedString(currWaterStr, left - 2, top + 30)
      }
      else {
        MuseRenderer.drawString(currWaterStr + '/' + maxWaterStr + " buckets", 1, 19)
      }
    }
  }
}