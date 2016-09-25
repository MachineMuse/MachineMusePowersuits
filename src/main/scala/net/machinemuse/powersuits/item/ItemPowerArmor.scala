package net.machinemuse.powersuits.item

import java.util.UUID

import com.google.common.collect.Multimap
import net.machinemuse.api.{ArmorTraits, IModularItem, ModuleManager}
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.powermodule.armor.HazmatModule
import net.machinemuse.powersuits.powermodule.misc.{InvisibilityModule, TintModule, TransparentArmorModule}
import net.machinemuse.utils._
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.{ItemArmor, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraftforge.common.ISpecialArmor
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 *
 * @author MachineMuse
 */
abstract class ItemPowerArmor(renderIndex: Int, armorType: EntityEquipmentSlot)
  extends ItemElectricArmor(ItemArmor.ArmorMaterial.IRON, renderIndex, armorType)
  with ISpecialArmor
  with ArmorTraits
  with IModularItem {
  setMaxStackSize(1)
  setCreativeTab(Config.getCreativeTab)

  /**
   * Inherited from ISpecialArmor, allows significant customization of damage
   * calculations.
   */
  override def getProperties(player: EntityLivingBase, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ISpecialArmor.ArmorProperties = {
    val priority: Int = 0
    if (source.isFireDamage && !(source == MuseHeatUtils.overheatDamage)) {
      return new ISpecialArmor.ArmorProperties(priority, 0.25, (25 * damage).toInt)
    }
    if(ModuleManager.itemHasModule(armor, HazmatModule.MODULE_HAZMAT)) {
      if(source.damageType.equals("electricity") || source.damageType.equals("radiation")) {
        return new ISpecialArmor.ArmorProperties(priority, 0.25, (25 * damage).toInt)
      }
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

    new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax)
  }

  def getArmorTexture(itemstack: ItemStack, entity: Entity, slot: Int, layer: Int): String = {
    Config.BLANK_ARMOR_MODEL_PATH
  }

  override def getColor(stack: ItemStack): Int = {
    val c: Colour = getColorFromItemStack(stack)
    c.getInt
  }

  @SideOnly(Side.CLIENT)
  override def getArmorModel(entityLiving: EntityLivingBase, itemStack: ItemStack, armorSlot: EntityEquipmentSlot, _default: ModelBiped): ModelBiped ={
    /*
     * TODO fix invisibility and add switch for Citizen Joe Armor
     * Note that we have access to arm/hands here, not just amor slots. We can probably use that to return the model for the powerfist from here as well
     *
     *
     *
     */


    //
    //


    // These are the
//    MAINHAND(EntityEquipmentSlot.Type.HAND, 0, 0, "mainhand"),
//    OFFHAND(EntityEquipmentSlot.Type.HAND, 1, 5, "offhand"),
//    FEET(EntityEquipmentSlot.Type.ARMOR, 0, 1, "feet"),
//    LEGS(EntityEquipmentSlot.Type.ARMOR, 1, 2, "legs"),
//    CHEST(EntityEquipmentSlot.Type.ARMOR, 2, 3, "chest"),
//    HEAD(EntityEquipmentSlot.Type.ARMOR, 3, 4, "head");






    val model: ArmorModel = ArmorModel.instance
        model.visibleSection = armorSlot

        if (itemStack != null) {
          entityLiving match {
            case player: EntityPlayer =>
              Option(player.inventory.armorItemInSlot(2)).map(chest =>
                if (ModuleManager.itemHasActiveModule(chest, InvisibilityModule.MODULE_ACTIVE_CAMOUFLAGE)) model.visibleSection = EntityEquipmentSlot.HEAD)
            case _ =>
          }
          if (ModuleManager.itemHasActiveModule(itemStack, TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
            model.visibleSection = EntityEquipmentSlot.HEAD
          }
          model.renderSpec = MuseItemUtils.getMuseRenderTag(itemStack, armorSlot)
        }
        model


//    super.getArmorModel(entityLiving, itemStack, armorSlot, _default)
  }


  override def getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack): Multimap[String, AttributeModifier] = {
    val parent = super.getAttributeModifiers(slot, stack)
    parent.put("generic.knockbackResistance", new AttributeModifier(UUID.fromString("448ef0e9-9b7c-4e56-bf3a-6b52aeabff8d"), "generic.knockbackResistance", 0.25, 0))
    parent
  }




  /*
   * Prevents armor enchantment
   */
  override def getItemEnchantability: Int = {
    0
  }

  /**
   * Return whether the specified armor IC2ItemTest has a color.
   */
  override def hasColor(stack: ItemStack): Boolean = {
    val itemTag: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
    ModuleManager.tagHasModule(itemTag, TintModule.RED_TINT) || ModuleManager.tagHasModule(itemTag, TintModule.GREEN_TINT) || ModuleManager.tagHasModule(itemTag, TintModule.BLUE_TINT)
  }

  /**
   * Inherited from ISpecialArmor, allows us to customize the calculations for
   * how much armor will display on the player's HUD.
   */
  override def getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int = {
    getArmorDouble(player, armor).asInstanceOf[Int]
  }

  def getHeatResistance(player: EntityPlayer, stack: ItemStack): Double = {
    MuseHeatUtils.getMaxHeat(stack)
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
    totalArmor
  }

  /**
   * Inherited from ISpecialArmor, allows us to customize how the armor
   * handles being damaged.
   */
  override def damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
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
