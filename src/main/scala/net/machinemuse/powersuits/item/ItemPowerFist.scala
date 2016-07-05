package net.machinemuse.powersuits.item

import crazypants.enderio.api.tool.ITool
import forestry.api.arboriculture.IToolGrafter
import net.machinemuse.api._
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.machinemuse.general.gui.MuseIcon
import net.machinemuse.numina.scala.OptionCast
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.powermodule.tool.GrafterModule
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule
import net.machinemuse.utils.{ElectricItemUtils, MuseHeatUtils}
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraft.util.math.{BlockPos, Vec3d}
import net.minecraft.util.{EnumActionResult, _}
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.{Side, SideOnly}


/**
 * Describes the modular power tool.
 *
 * @author MachineMuse
 */
@Optional.InterfaceList(Array(
  new Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO", striprefs = true),
  new Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "Forestry", striprefs = true),
  new Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true)
))
class ItemPowerFist extends ItemElectricTool(0, 0,ToolMaterial.DIAMOND)
with IModularItem
with IToolGrafter
with ITool
with ModeChangingModularItem {
  val iconpath: String = Config.TEXTURE_PREFIX + "handitem"
  setMaxStackSize(1)
  setMaxDamage(0)
  setCreativeTab(Config.getCreativeTab)
  setUnlocalizedName("powerFist")


  /**
   * Returns the strength of the stack against a given block. 1.0F base,
   * (Quality+1)*2 if correct blocktype, 1.5F if sword
   */
  def getStrVsBlock(stack: ItemStack, block: Block): Float = getStrVsBlock(stack, block, 0)

  /**
   * FORGE: Overridden to allow custom tool effectiveness
   */
  def getStrVsBlock(stack: ItemStack, block: Block, meta: Int): Float = 1

  /**
   * Current implementations of this method in child classes do not use the
   * entry argument beside stack. They just raise the damage on the stack.
   */
  override def hitEntity(stack: ItemStack, entityBeingHit: EntityLivingBase, entityDoingHitting: EntityLivingBase): Boolean = {
    // TODO find replacement wrench code or port CoFH lib

//    if (ModuleManager.itemHasActiveModule(stack, OmniWrenchModule.MODULE_OMNI_WRENCH)) {
//      entityBeingHit.rotationYaw += 90.0F;
//      entityBeingHit.rotationYaw %= 360.0F;
//    }
    if (entityDoingHitting.isInstanceOf[EntityPlayer] && ModuleManager.itemHasActiveModule(stack, MeleeAssistModule.MODULE_MELEE_ASSIST)) {
      val player: EntityPlayer = entityDoingHitting.asInstanceOf[EntityPlayer]
      val drain: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_ENERGY)
      if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
        ElectricItemUtils.drainPlayerEnergy(player, drain)
        val damage: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE)
        val knockback: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_KNOCKBACK)
        val damageSource: DamageSource = DamageSource.causePlayerDamage(player)
        if (entityBeingHit.attackEntityFrom(damageSource, damage.asInstanceOf[Int])) {
          val lookVec: Vec3d = player.getLookVec
          entityBeingHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord * knockback)
        }
      }
    }
    return true
  }

  /**
   * Called when a block is destroyed using this tool.
   * <p/>
   * Returns: Whether to increment player use stats with this item
   */
  override def onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean = {
    entityLiving match {
      case player: EntityPlayer =>
        import scala.collection.JavaConversions._
        for (module <- ModuleManager.getBlockBreakingModules) {
          if (ModuleManager.itemHasActiveModule(stack, module.getDataName)) {
            if (module.onBlockDestroyed(stack, worldIn, state, pos, player)) {
              return true
            }
          }
        }
      case _ =>
    }
    true
  }

  /**
   * An itemstack sensitive version of getDamageVsEntity - allows items to
   * handle damage based on
   * itemstack data, like tags. Falls back to getDamageVsEntity.
   *
   * @param par1Entity The entity being attacked (or the attacking mob, if it's a mob
   *                   - vanilla bug?)
   * @param itemStack  The itemstack
   * @return the damage
   */
  def getDamageVsEntity(par1Entity: Entity, itemStack: ItemStack): Float = {
    ModuleManager.computeModularProperty(itemStack, MeleeAssistModule.PUNCH_DAMAGE).toFloat
  }

  @SideOnly(Side.CLIENT) override def isFull3D: Boolean = {
    return true
  }

  /**
   * Return the enchantability factor of the item. In this case, 0. Might add
   * an enchantability module later :P
   */
  override def getItemEnchantability = 0

  /**
   * Return the name for this tool's material.
   */
  override def getToolMaterialName = this.toolMaterial.toString


  /**
   * Return whether this item is repairable in an anvil.
   */
  override def getIsRepairable(par1ItemStack: ItemStack, par2ItemStack: ItemStack) = false

  /**
   * How long it takes to use or consume an item
   */
  override def getMaxItemUseDuration(par1ItemStack: ItemStack): Int = 72000


  /**
   * Called when the right click button is pressed
   */
  override def onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult[ItemStack] = {
    import scala.collection.JavaConversions._
    for (module <- ModuleManager.getRightClickModules) {
      if (module.isValidForItem(itemStackIn) && ModuleManager.itemHasActiveModule(itemStackIn, module.getDataName)) {
        module.onRightClick(playerIn, worldIn, itemStackIn)
      }
    }
    return new ActionResult(EnumActionResult.PASS, itemStackIn);
//    return itemStackIn;
  }

  /**
   * returns the action that specifies what animation to play when the items
   * is being used
   */
  override def getItemUseAction(stack: ItemStack): EnumAction = {
    return EnumAction.BOW
  }

  /**
    * Called when the player stops using an Item (stops holding the right mouse button).
    */
  override def onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
    val mode: String = getActiveMode(stack, entityLiving.asInstanceOf[EntityPlayer])
    val module: IPowerModule = ModuleManager.getModule(mode)
    OptionCast[IRightClickModule](module) map {
      m => m.onPlayerStoppedUsing(stack, worldIn, entityLiving.asInstanceOf[EntityPlayer], timeLeft)
    }
  }

  def shouldPassSneakingClickToBlock(world: World, x: Int, y: Int, z: Int): Boolean = true

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float,
                              hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult = {

    val mode: String = getActiveMode(stack, player)
    val module: IPowerModule = ModuleManager.getModule(mode)
    module match {
      case m: IRightClickModule => m.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ)
      case _ => false
    }

    return EnumActionResult.PASS
  }

  override def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand,
                         facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = {


    val mode: String = getActiveMode(stack, playerIn)
    val module: IPowerModule = ModuleManager.getModule(mode)
    module match {
      case m: IRightClickModule => m.onItemUse(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ); false
      case _ => false
    }
    return EnumActionResult.PASS
  }

  @Optional.Method(modid = "Forestry")
  override def getSaplingModifier(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer, blockPos: BlockPos): Float = {
    if (ModuleManager.itemHasActiveModule(itemStack, GrafterModule.MODULE_GRAFTER)) {
      ElectricItemUtils.drainPlayerEnergy(entityPlayer, ModuleManager.computeModularProperty(itemStack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION))
      MuseHeatUtils.heatPlayer(entityPlayer, ModuleManager.computeModularProperty(itemStack, GrafterModule.GRAFTER_HEAT_GENERATION))
      100F
    } else {
      0F
    }
  }

  def canHarvestBlock(stack: ItemStack, pos: BlockPos, state: IBlockState, player: EntityPlayer): Boolean = {
    if (state.getMaterial.isToolNotRequired) {
      return true
    }
    import scala.collection.JavaConversions._
    for (module <- ModuleManager.getBlockBreakingModules) {
      if (ModuleManager.itemHasActiveModule(stack, module.getDataName) && module.canHarvestBlock(stack, pos, state, player)) {
        return true
      }
    }
    return false
  }


//  // Buildcraft Wrench
//  override def wrenchUsed(player: EntityPlayer, i: Int, i1: Int, i2: Int): Unit = { }
//
//  // Buildcraft Wrench
//  override def canWrench(player: EntityPlayer, i: Int, i1: Int, i2: Int): Boolean = {
//    getActiveMode(player.getHeldItem, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
//  }

  // EnderIO Tool
  override def used(itemStack: ItemStack, entityPlayer: EntityPlayer, pos: BlockPos): Unit = {}

//   EnderIO Tool
  override def canUse(itemStack: ItemStack, entityPlayer: EntityPlayer, pos: BlockPos): Boolean = {
//    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
    false
  }

//   EnderIO Tool
  override def shouldHideFacades(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean = {
//    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
    false
  }
}
