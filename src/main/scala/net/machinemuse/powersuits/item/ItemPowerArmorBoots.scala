package net.machinemuse.powersuits.item

import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemPowerArmorBoots extends ItemPowerArmor(0, 3) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorfeet"

  setUnlocalizedName("powerArmorBoots")

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
  
  override def onArmorPieceTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
      if (player.inventory.armorItemInSlot(3).getItem.isInstanceOf[ItemPowerArmor]) {
          System.out.println("itemStack and armor item are equal")
      }
  }
  
  override def onFullArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
  }
}