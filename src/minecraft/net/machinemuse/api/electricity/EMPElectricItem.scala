package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.minecraft.entity.Entity
import icbm.api.explosion.{IEMPItem, IExplosive}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:21 PM, 4/20/13
 */
trait EMPElectricItem extends IEMPItem with MuseElectricItem {

  def onEMP(itemStack: ItemStack, entity: Entity, empExplosive: IExplosive) {
    setCurrentEnergy(itemStack, 0)
  }
}