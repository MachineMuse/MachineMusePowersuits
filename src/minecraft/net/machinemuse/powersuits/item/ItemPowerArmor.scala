package net.machinemuse.powersuits.item

import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.powermodule.misc.{InvisibilityModule, TransparentArmorModule, TintModule}
import net.machinemuse.utils._
import net.minecraft.entity.{Entity, EntityLiving}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraftforge.common.ISpecialArmor
import scala.Predef.String
import net.machinemuse.general.geometry.Colour
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.ModelBiped
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.machinemuse.api.{ArmorTraits, ModuleManager, IModularItem}

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 */
abstract class ItemPowerArmor(id: Int, renderIndex: Int, armorType: Int)
  extends ItemElectricArmor(id, EnumArmorMaterial.IRON, renderIndex, armorType)
  with ISpecialArmor
  with IModularItem
  with ArmorTraits {
  setMaxStackSize(1)
  setCreativeTab(Config.getCreativeTab)

  /**
   * Inherited from ISpecialArmor, allows significant customization of damage
   * calculations.
   */
  def getProperties(player: EntityLiving, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ISpecialArmor.ArmorProperties = {
    val priority: Int = 1
    if (source.isFireDamage && !(source == MuseHeatUtils.overheatDamage)) {
      return new ISpecialArmor.ArmorProperties(priority, 0.25, (25 * damage).toInt)
    }
    val armorDouble = player match {
      case player: EntityPlayer => getArmorDouble(player, armor)
      case _ => 2.0
    }
    var absorbRatio: Double = 0.04 * armorDouble
    var absorbMax: Int = armorDouble.asInstanceOf[Int] * 75
    if (source.isUnblockable) {
      absorbMax = 0
      absorbRatio = 0
    }
    return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax)
  }

  override def getArmorTexture(itemstack: ItemStack, entity: Entity, slot: Int, layer: Int): String = {
    Config.BLANK_ARMOR_MODEL_PATH
  }

  override def getColor(stack: ItemStack): Int = {
    val c: Colour = getColorFromItemStack(stack)
    c.getInt
  }

  @SideOnly(Side.CLIENT)
  override def getArmorModel(entityLiving: EntityLiving, itemstack: ItemStack, armorSlot: Int): ModelBiped = {
    val model = ArmorModel.instance

    model.visible = armorSlot

    if (itemstack != null) {
      if(entityLiving.isInstanceOf[EntityPlayer]) {
        Option(entityLiving.asInstanceOf[EntityPlayer].getCurrentArmor(2)).map(chest=>
          if(MuseItemUtils.itemHasActiveModule(chest, InvisibilityModule.MODULE_ACTIVE_CAMOUFLAGE)) return null)
      }
      if (MuseItemUtils.itemHasActiveModule(itemstack, TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
        return null
      }
      model.renderSpec = MuseItemUtils.getMuseRenderTag(itemstack, armorSlot)
    }
    model
  }

  override def getItemEnchantability: Int = {
    return 0
  }

  /**
   * Return whether the specified armor IC2ItemTest has a color.
   */
  override def hasColor(stack: ItemStack): Boolean = {
    val itemTag: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
    return MuseItemUtils.tagHasModule(itemTag, TintModule.RED_TINT) || MuseItemUtils.tagHasModule(itemTag, TintModule.GREEN_TINT) || MuseItemUtils.tagHasModule(itemTag, TintModule.BLUE_TINT)
  }

  /**
   * Inherited from ISpecialArmor, allows us to customize the calculations for
   * how much armor will display on the player's HUD.
   */
  override def getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int = {
    return getArmorDouble(player, armor).asInstanceOf[Int]
  }

  def getHeatResistance(player: EntityPlayer, stack: ItemStack): Double = {
    return MuseHeatUtils.getMaxHeat(stack)
  }

  override def getArmorDouble(player: EntityPlayer, stack: ItemStack): Double = {
    var totalArmor: Double = 0
    val props: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
    val energy: Double = ElectricItemUtils.getPlayerEnergy(player)
    val physArmor: Double = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL)
    val enerArmor: Double = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY)
    val enerConsum: Double = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION)
    totalArmor += physArmor
    if (energy > enerConsum) {
      totalArmor += enerArmor
    }
    totalArmor = Math.min(Config.getMaximumArmorPerPiece, totalArmor)
    return totalArmor
  }

  /**
   * Inherited from ISpecialArmor, allows us to customize how the armor
   * handles being damaged.
   */
  def damageArmor(entity: EntityLiving, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
    val itemProperties: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
    if (entity.isInstanceOf[EntityPlayer]) {
      if (source == MuseHeatUtils.overheatDamage) {
      } else if (source.isFireDamage) {
        val player: EntityPlayer = entity.asInstanceOf[EntityPlayer]
        if (!source.equals(DamageSource.onFire) || MuseHeatUtils.getPlayerHeat(player) < MuseHeatUtils.getMaxHeat(player)) {
          MuseHeatUtils.heatPlayer(player, damage)
        }
      }
      else {
        val enerConsum: Double = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION)
        val drain: Double = enerConsum * damage
        if (entity.isInstanceOf[EntityPlayer]) {
          ElectricItemUtils.drainPlayerEnergy(entity.asInstanceOf[EntityPlayer], drain)
        }
        else {
          drainEnergyFrom(stack, drain)
        }
      }
    }
  }
}