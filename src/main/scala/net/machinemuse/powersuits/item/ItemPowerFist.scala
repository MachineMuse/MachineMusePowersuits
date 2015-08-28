package net.machinemuse.powersuits.item

import appeng.api.implementations.items.IAEWrench
import buildcraft.api.tools.IToolWrench
import cofh.api.item.IToolHammer
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.{Side, SideOnly}
import crazypants.enderio.api.tool.ITool
import forestry.api.arboriculture.IToolGrafter
import mekanism.api.IMekWrench
import mods.railcraft.api.core.items.IToolCrowbar
import net.machinemuse.api._
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.machinemuse.general.gui.MuseIcon
import net.machinemuse.numina.scala.OptionCast
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.powermodule.tool.{GrafterModule, OmniWrenchModule}
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule
import net.machinemuse.utils.{ElectricItemUtils, MuseHeatUtils}
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraft.util.{DamageSource, Vec3}
import net.minecraft.world.World
import powercrystals.minefactoryreloaded.api.IMFRHammer

/**
 * Describes the modular power tool.
 *
 * @author MachineMuse
 */
@Optional.InterfaceList(Array(
  new Optional.Interface(iface = "mekanism.api.IMekWrench", modid = "Mekanism", striprefs = true),
  new Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO", striprefs = true),
  new Optional.Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = "ProjRed|Core", striprefs = true),
  new Optional.Interface(iface = "com.bluepowermod.api.misc.IScrewdriver", modid = "bluepower", striprefs = true),
  new Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "Forestry", striprefs = true),
  new Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "Railcraft", striprefs = true),
  new Optional.Interface(iface = "powercrystals.minefactoryreloaded.api.IMFRHammer", modid = "MineFactoryReloaded", striprefs = true),
  new Optional.Interface(iface = "cofh.api.item.IToolHammer", modid = "CoFHCore", striprefs = true),
  new Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true),
  new Optional.Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2", striprefs = true)
))
class ItemPowerFist extends ItemElectricTool(0, ToolMaterial.EMERALD)
with IModularItem
with IToolGrafter
with IToolHammer
with IMFRHammer
with IToolCrowbar
with IAEWrench
with IToolWrench
with com.bluepowermod.api.misc.IScrewdriver
with mrtjp.projectred.api.IScrewdriver
with ITool
with IMekWrench
with ModeChangingModularItem {
  val iconpath: String = MuseIcon.ICON_PREFIX + "handitem"
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

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }

  /**
   * Current implementations of this method in child classes do not use the
   * entry argument beside stack. They just raise the damage on the stack.
   */
  override def hitEntity(stack: ItemStack, entityBeingHit: EntityLivingBase, entityDoingHitting: EntityLivingBase): Boolean = {
    if (ModuleManager.itemHasActiveModule(stack, OmniWrenchModule.MODULE_OMNI_WRENCH)) {
      entityBeingHit.rotationYaw += 90.0F;
      entityBeingHit.rotationYaw %= 360.0F;
    }
    if (entityDoingHitting.isInstanceOf[EntityPlayer] && ModuleManager.itemHasActiveModule(stack, MeleeAssistModule.MODULE_MELEE_ASSIST)) {
      val player: EntityPlayer = entityDoingHitting.asInstanceOf[EntityPlayer]
      val drain: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_ENERGY)
      if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
        ElectricItemUtils.drainPlayerEnergy(player, drain)
        val damage: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE)
        val knockback: Double = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_KNOCKBACK)
        val damageSource: DamageSource = DamageSource.causePlayerDamage(player)
        if (entityBeingHit.attackEntityFrom(damageSource, damage.asInstanceOf[Int])) {
          val lookVec: Vec3 = player.getLookVec
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
  override def onBlockDestroyed(stack: ItemStack, world: World, block: Block, x: Int, y: Int, z: Int, entity: EntityLivingBase): Boolean = {
    entity match {
      case player: EntityPlayer =>
        import scala.collection.JavaConversions._
        for (module <- ModuleManager.getBlockBreakingModules) {
          if (ModuleManager.itemHasActiveModule(stack, module.getDataName)) {
            if (module.onBlockDestroyed(stack, world, block, x, y, z, player)) {
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
  override def onItemRightClick(itemStack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    import scala.collection.JavaConversions._
    for (module <- ModuleManager.getRightClickModules) {
      if (module.isValidForItem(itemStack) && ModuleManager.itemHasActiveModule(itemStack, module.getDataName)) {
        module.onRightClick(player, world, itemStack)
      }
    }
    return itemStack
  }

  /**
   * returns the action that specifies what animation to play when the items
   * is being used
   */
  override def getItemUseAction(stack: ItemStack): EnumAction = {
    return EnumAction.bow
  }

  /**
   * Called when the right click button is released
   */
  override def onPlayerStoppedUsing(itemStack: ItemStack, world: World, player: EntityPlayer, par4: Int) {
    val mode: String = getActiveMode(itemStack, player)
    val module: IPowerModule = ModuleManager.getModule(mode)
    OptionCast[IRightClickModule](module) map {
      m => m.onPlayerStoppedUsing(itemStack, world, player, par4)
    }
  }

  def shouldPassSneakingClickToBlock(world: World, x: Int, y: Int, z: Int): Boolean = true

  override def onItemUseFirst(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val mode: String = getActiveMode(itemStack, player)
    val module: IPowerModule = ModuleManager.getModule(mode)
    module match {
      case m: IRightClickModule => m.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ)
      case _ => false
    }
  }

  override def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val mode: String = getActiveMode(itemStack, player)
    val module: IPowerModule = ModuleManager.getModule(mode)
    module match {
      case m: IRightClickModule => m.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ); false
      case _ => false
    }
  }

  @Optional.Method(modid = "Forestry")
  def getSaplingModifier(stack: ItemStack, world: World, player: EntityPlayer, x: Int, y: Int, z: Int): Float = {
    if (ModuleManager.itemHasActiveModule(stack, GrafterModule.MODULE_GRAFTER)) {
      ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION))
      MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(stack, GrafterModule.GRAFTER_HEAT_GENERATION))
      100F
    } else {
      0F
    }
  }

  def canHarvestBlock(stack: ItemStack, block: Block, meta: Int, player: EntityPlayer): Boolean = {
    if (block.getMaterial.isToolNotRequired) {
      return true
    }
    import scala.collection.JavaConversions._
    for (module <- ModuleManager.getBlockBreakingModules) {
      if (ModuleManager.itemHasActiveModule(stack, module.getDataName) && module.canHarvestBlock(stack, block, meta, player)) {
        return true
      }
    }
    return false
  }

  // TE Crescent Hammer
  override def isUsable(itemStack: ItemStack, entityLivingBase: EntityLivingBase, i: Int, i1: Int, i2: Int): Boolean = {
    entityLivingBase match {
      case player: EntityPlayer => getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
      case _ => false
    }
  }

  // TE Crescent Hammer
  override def toolUsed(itemStack: ItemStack, entityLivingBase: EntityLivingBase, i: Int, i1: Int, i2: Int): Unit = {}

  // Railcraft Crowbar
  override def canWhack(player: EntityPlayer, itemStack: ItemStack, i: Int, i1: Int, i2: Int): Boolean = {
    getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Railcraft Crowbar
  override def canLink(player: EntityPlayer, itemStack: ItemStack, entityMinecart: EntityMinecart): Boolean = {
    getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Railcraft Crowbar
  override def canBoost(player: EntityPlayer, itemStack: ItemStack, entityMinecart: EntityMinecart): Boolean = {
    getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Railcraft Crowbar
  override def onLink(player: EntityPlayer, itemStack: ItemStack, entityMinecart: EntityMinecart): Unit = {}

  // Railcraft Crowbar
  override def onWhack(player: EntityPlayer, itemStack: ItemStack, i: Int, i1: Int, i2: Int): Unit = {}

  // Railcraft Crowbar
  override def onBoost(player: EntityPlayer, itemStack: ItemStack, entityMinecart: EntityMinecart): Unit = {}

  // AE wrench
  override def canWrench(itemStack: ItemStack, player: EntityPlayer, i: Int, i1: Int, i2: Int): Boolean = {
    getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Buildcraft Wrench
  override def wrenchUsed(player: EntityPlayer, i: Int, i1: Int, i2: Int): Unit = { }

  // Buildcraft Wrench
  override def canWrench(player: EntityPlayer, i: Int, i1: Int, i2: Int): Boolean = {
    getActiveMode(player.getHeldItem, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Bluepower Screwdriver
  override def damage(itemStack: ItemStack, i: Int, entityPlayer: EntityPlayer, b: Boolean): Boolean = {
    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // ProjectRed Screwdriver
  override def damageScrewdriver(entityPlayer: EntityPlayer, itemStack: ItemStack): Unit = {}

  // ProjectRed Screwdriver
  override def canUse(entityPlayer: EntityPlayer, itemStack: ItemStack): Boolean = {
    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // EnderIO Tool
  override def used(itemStack: ItemStack, entityPlayer: EntityPlayer, i: Int, i1: Int, i2: Int): Unit = {}

  // EnderIO Tool
  override def canUse(itemStack: ItemStack, entityPlayer: EntityPlayer, i: Int, i1: Int, i2: Int): Boolean = {
    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // EnderIO Tool
  override def shouldHideFacades(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean = {
    getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  // Mekanism Wrench
  override def canUseWrench(entityPlayer: EntityPlayer, i: Int, i1: Int, i2: Int): Boolean = {
    getActiveMode(entityPlayer.getHeldItem, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH)
  }
}
