package net.machinemuse.powersuits.item

import atomicscience.api.IAntiPoisonArmor
import atomicscience.api.poison.Poison
import forestry.api.apiculture.IArmorApiarist
import net.machinemuse.api.IModularItem
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.powermodule.misc.{TransparentArmorModule, TintModule}
import net.machinemuse.utils.ElectricItemUtils
import net.machinemuse.utils.MuseCommonStrings
import net.machinemuse.utils.MuseHeatUtils
import net.machinemuse.utils.MuseItemUtils
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
import net.machinemuse.powersuits.client.render.{ArmorModel, ArmorBootsModel}
import net.machinemuse.powersuits.powermodule.armor.{HazmatModule, ApiaristArmorModule}

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 */
abstract class ItemPowerArmor(id: Int, renderIndex: Int, armorType: Int) extends ItemElectricArmor(id, EnumArmorMaterial.IRON, renderIndex, armorType) with ISpecialArmor with IAntiPoisonArmor with IModularItem with IArmorApiarist {
  setMaxStackSize(1)
  setCreativeTab(Config.getCreativeTab)

  /**
   * Inherited from ISpecialArmor, allows significant customization of damage
   * calculations.
   */
  def getProperties(player: EntityLiving, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ISpecialArmor.ArmorProperties = {
    val priority: Int = 1
    if (source.isFireDamage && !(source == MuseHeatUtils.overheatDamage)) {
      return new ISpecialArmor.ArmorProperties(priority, 0.25, (25 * damage).asInstanceOf[Int])
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
    val model = armorSlot match {
      case 3 => ArmorBootsModel.getInstance
      case _ => ArmorModel.getInstance
    }
    model.bipedHead.showModel = armorSlot == 0
    model.bipedHeadwear.showModel = armorSlot == 0
    model.bipedBody.showModel = armorSlot == 1
    model.bipedRightArm.showModel = armorSlot == 1
    model.bipedLeftArm.showModel = armorSlot == 1
    model.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3
    model.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3
    if (itemstack != null) {
      if (MuseItemUtils.itemHasActiveModule(itemstack, TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
        return null
      }
      model.normalcolour = this.getColorFromItemStack(itemstack)
      model.glowcolour = this.getGlowFromItemStack(itemstack)
    }
    return model
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
  def getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int = {
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
      }
      else if (source.isFireDamage) {
        val player: EntityPlayer = entity.asInstanceOf[EntityPlayer]
        MuseHeatUtils.heatPlayer(player, damage)
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

  def isProtectedFromPoison(itemStack: ItemStack, entityLiving: EntityLiving, `type`: Poison): Boolean = {
    return MuseItemUtils.itemHasActiveModule(itemStack, HazmatModule.MODULE_HAZMAT)
  }

  def onProtectFromPoison(itemStack: ItemStack, entityLiving: EntityLiving, `type`: Poison) {
  }

  def protectPlayer(player: EntityPlayer, armor: ItemStack, cause: String, doProtect: Boolean): Boolean = {
    if (MuseItemUtils.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
      ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION))
      return true
    }
    return false
  }
}