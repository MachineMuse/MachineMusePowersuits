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
  val unlocalizedName: String = "powerArmorBoots"

  setUnlocalizedName(unlocalizedName)

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
  
  override def onArmorPieceTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
      System.out.println(player.inventory.armorItemInSlot(0).getItem.asInstanceOf[ItemPowerArmor].getUnlocalizedName)
  }
  
  override def onFullArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
  }
}