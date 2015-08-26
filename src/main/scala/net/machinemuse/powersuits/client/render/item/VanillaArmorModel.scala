package net.machinemuse.powersuits.client.render.item

import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.Entity

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:24 PM, 11/07/13
 */
class VanillaArmorModel extends ArmorModel {
  init()
  /**
   * Sets the models various rotation angles then renders the model.
   */
  override def render(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    prep(entity, par2, par3, par4, par5, par6, scale)
    setRotationAngles(par2, par3, par4, par5, par6, scale, entity)
    super.render(entity, par2, par3, par4, par5, par6, scale)
    post(entity, par2, par3, par4, par5, par6, scale)
  }
}
