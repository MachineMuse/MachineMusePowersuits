package net.machinemuse.powersuits.item

import net.minecraft.item.{ItemStack, ItemArmor}
import java.lang.String
import net.machinemuse.powersuits.common.Config
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.model.ModelBiped
import net.machinemuse.powersuits.client.render.{ArmorBootsModel, ArmorModel}
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.powersuits.powermodule.misc.TransparentArmorModule
import net.machinemuse.general.geometry.Colour
import net.minecraft.entity.{Entity, EntityLiving}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:09 PM, 4/23/13
 */
trait CustomRenderArmorBase extends ItemArmor with ModularItemBase {
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

}
