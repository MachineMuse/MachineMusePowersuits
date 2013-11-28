package net.machinemuse.powersuits.powermodule.movement

import net.machinemuse.api.IModularItem
import net.machinemuse.api.ModuleManager
import net.machinemuse.api.moduletrigger.IPlayerTickModule
import net.machinemuse.api.moduletrigger.IToggleableModule
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.machinemuse.utils.ElectricItemUtils
import net.machinemuse.utils.MuseCommonStrings
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.StatCollector
import java.util.List

object SprintAssistModule {
  val MODULE_SPRINT_ASSIST: String = "Sprint Assist"
  val SPRINT_ENERGY_CONSUMPTION: String = "Sprint Energy Consumption"
  val SPRINT_SPEED_MULTIPLIER: String = "Sprint Speed Multiplier"
  val SPRINT_FOOD_COMPENSATION: String = "Sprint Exhaustion Compensation"
  val WALKING_ENERGY_CONSUMPTION: String = "Walking Energy Consumption"
  val WALKING_SPEED_MULTIPLIER: String = "Walking Speed Multiplier"
  val TAGUUID: UUID = UUID(-7931854408382894632l, -8160638015224787553l)
}

class SprintAssistModule(validItems: List[IModularItem]) extends PowerModuleBase(validItems) with IToggleableModule with IPlayerTickModule {

  import SprintAssistModule._

  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4))
  addSimpleTradeoff(this, "Power", SPRINT_ENERGY_CONSUMPTION, "J", 0, 10, SPRINT_SPEED_MULTIPLIER, "%", 1, 2)
  addSimpleTradeoff(this, "Compensation", SPRINT_ENERGY_CONSUMPTION, "J", 0, 2, SPRINT_FOOD_COMPENSATION, "%", 0, 1)
  addSimpleTradeoff(this, "Walking Assist", WALKING_ENERGY_CONSUMPTION, "J", 0, 10, WALKING_SPEED_MULTIPLIER, "%", 1, 1)

  def getCategory: String = MuseCommonStrings.CATEGORY_MOVEMENT

  def getDataName: String = MODULE_SPRINT_ASSIST

  def getLocalizedName: String = StatCollector.translateToLocal("module.sprintAssist.name")

  def getDescription: String = "A set of servo motors to help you sprint (double-tap forward) and walk faster."

  def onPlayerTickActive(player: EntityPlayer, item: ItemStack) {
    val horzMovement: Double = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ)
    val totalEnergy: Double = ElectricItemUtils.getPlayerEnergy(player)
    if (player.isSprinting) {
      val exhaustion: Double = Math.round(horzMovement * 100.0F) * 0.01
      val sprintCost: Double = ModuleManager.computeModularProperty(item, SPRINT_ENERGY_CONSUMPTION)
      if (sprintCost < totalEnergy) {
        val sprintMultiplier: Double = ModuleManager.computeModularProperty(item, SPRINT_SPEED_MULTIPLIER)
        val exhaustionComp: Double = ModuleManager.computeModularProperty(item, SPRINT_FOOD_COMPENSATION)
        ElectricItemUtils.drainPlayerEnergy(player, sprintCost * horzMovement * 5)
        setMovementModifier(item, sprintMultiplier)
        player.getFoodStats.addExhaustion((-0.01 * exhaustion * exhaustionComp).asInstanceOf[Float])
                player.jumpMovementFactor = player.getAIMoveSpeed * .2f
      }
    } else {
      val cost: Double = ModuleManager.computeModularProperty(item, WALKING_ENERGY_CONSUMPTION)
      if (cost < totalEnergy) {
        val walkMultiplier: Double = ModuleManager.computeModularProperty(item, WALKING_SPEED_MULTIPLIER)
        ElectricItemUtils.drainPlayerEnergy(player, cost * horzMovement * 5)
        setMovementModifier(item, walkMultiplier)
                player.jumpMovementFactor = player.getAIMoveSpeed * .2f
      }
    }
  }

  def setMovementModifier(item: ItemStack, multiplier: Double) {
    val modifiers: NBTTagList = item.getTagCompound.getTagList("AttributeModifiers")
    item.getTagCompound.setTag("AttributeModifiers", modifiers)
    val sprintModifiers =
      for (i <- 0 until modifiers.tagCount()) yield {
        val tag = modifiers.tagAt(i).asInstanceOf[NBTTagCompound]
        if (new AttributeModifier(tag).name == "Sprint Assist") {
          Some(tag)
        } else None
      } flatMap {
        tag: NBTTagCompound =>
          tag.setInteger("Operation", 1)
          tag.setDouble("Amount", multiplier - 1)
          Some(tag)
      }
    if (sprintModifiers.isEmpty) modifiers.appendTag(AttributeModifier(1, TAGUUID, multiplier - 1, "generic.movementSpeed", "Sprint Assist").toNBT)
  }

  def onPlayerTickInactive(player: EntityPlayer, item: ItemStack) {
    if (item != null) {

      val modifiers: NBTTagList = item.getTagCompound.getTagList("AttributeModifiers")
      for (i <- 0 until modifiers.tagCount()) yield {
        val tag = modifiers.tagAt(i).asInstanceOf[NBTTagCompound]
        if (new AttributeModifier(tag).name == "Sprint Assist") {
          Some(tag)
        } else None
      } flatMap {
        tag: NBTTagCompound =>
          tag.setDouble("Amount", 0)
          Some(tag)
      }
    }
  }

  def getTextureFile: String = "sprintassist"
}