//package net.minecraftforge.debug
//
//import java.util.ArrayList
//import java.util.List
//import javax.vecmath.AxisAngle4d
//import javax.vecmath.Matrix4f
//import javax.vecmath.Quat4f
//import javax.vecmath.Vector3d
//import javax.vecmath.Vector4f
//import com.google.common.base.Optional
//import com.google.common.collect.UnmodifiableIterator
//import net.minecraft.block.Block
//import net.minecraft.block.ITileEntityProvider
//import net.minecraft.block.material.Material
//import net.minecraft.block.properties.IProperty
//import net.minecraft.block.properties.PropertyBool
//import net.minecraft.block.properties.PropertyDirection
//import net.minecraft.block.state.BlockStateContainer
//import net.minecraft.block.state.IBlockState
//import net.minecraft.client.Minecraft
//import net.minecraft.client.renderer.block.model.ModelResourceLocation
//import net.minecraft.creativetab.CreativeTabs
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.item.Item
//import net.minecraft.item.ItemBlock
//import net.minecraft.item.ItemStack
//import net.minecraft.tileentity.TileEntity
//import net.minecraft.util.EnumFacing
//import net.minecraft.util.EnumHand
//import net.minecraft.util.ITickable
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.text.TextComponentString
//import net.minecraft.world.IBlockAccess
//import net.minecraft.world.World
//import net.minecraftforge.client.model.ModelLoader
//import net.minecraftforge.client.model.b3d.B3DLoader
//import net.minecraftforge.client.model.obj.OBJLoader
//import net.minecraftforge.common.model.IModelPart
//import net.minecraftforge.common.model.IModelState
//import net.minecraftforge.common.model.Models
//import net.minecraftforge.common.model.TRSRTransformation
//import net.minecraftforge.common.property.ExtendedBlockState
//import net.minecraftforge.common.property.IExtendedBlockState
//import net.minecraftforge.common.property.IUnlistedProperty
//import net.minecraftforge.common.property.Properties
//import net.minecraftforge.fml.common.FMLLog
//import net.minecraftforge.fml.common.Mod
//import net.minecraftforge.fml.common.Mod.EventHandler
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
//import net.minecraftforge.fml.common.registry.GameRegistry
//import net.minecraftforge.fml.relauncher.Side
//import com.google.common.collect.Lists
//
////remove if not needed
//import scala.collection.JavaConversions._
//
//object ModelLoaderRegistryDebug {
//
//  val MODID = "ForgeDebugModelLoaderRegistry"
//
//  val VERSION = "1.0"
//
//
//
//
//
//  object OBJTesseractBlock {
//
//    val instance = new OBJTesseractBlock()
//
//    val name = "OBJTesseractBlock"
//  }
//
//  class OBJTesseractBlock private () extends Block(Material.IRON) with ITileEntityProvider {
//
//    setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
//
//    setUnlocalizedName(MODID + ":" + name)
//
//    setRegistryName(new ResourceLocation(MODID, name))
//
//    override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = new OBJTesseractTileEntity()
//
//    override def isOpaqueCube(state: IBlockState): Boolean = false
//
//    override def isFullCube(state: IBlockState): Boolean = false
//
//    override def isVisuallyOpaque(): Boolean = false
//
//    override def onBlockActivated(world: World,
//                                  pos: BlockPos,
//                                  state: IBlockState,
//                                  player: EntityPlayer,
//                                  hand: EnumHand,
//                                  heldItem: ItemStack,
//                                  side: EnumFacing,
//                                  hitX: Float,
//                                  hitY: Float,
//                                  hitZ: Float): Boolean = {
//      if (world.getTileEntity(pos) == null) world.setTileEntity(pos, new OBJTesseractTileEntity())
//      val tileEntity = world.getTileEntity(pos).asInstanceOf[OBJTesseractTileEntity]
//      if (player.isSneaking) {
//        tileEntity.decrement()
//      } else {
//        tileEntity.increment()
//      }
//      world.markBlockRangeForRenderUpdate(pos, pos)
//      false
//    }
//
//    override def hasTileEntity(state: IBlockState): Boolean = true
//
//    override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = {
//      if (world.getTileEntity(pos) != null &&
//        world.getTileEntity(pos).isInstanceOf[OBJTesseractTileEntity]) {
//        val te = world.getTileEntity(pos).asInstanceOf[OBJTesseractTileEntity]
//        return state.asInstanceOf[IExtendedBlockState].withProperty(Properties.AnimationProperty, te.state)
//      }
//      state
//    }
//
//    override def createBlockState(): BlockStateContainer = {
//      new ExtendedBlockState(this, Array.ofDim[IProperty](0), Array(Properties.AnimationProperty))
//    }
//  }
//
//  class OBJTesseractTileEntity extends TileEntity {
//
//    private var counter: Int = 1
//
//    private var max: Int = 32
//
//    private val hidden = new ArrayList[String]()
//
//    private val state = new IModelState() {
//
//      private val value = Optional.of(TRSRTransformation.identity())
//
//      override def apply(part: Optional[_ <: IModelPart]): Optional[TRSRTransformation] = {
//        if (part.isPresent) {
//          val parts = Models.getParts(part.get)
//          if (parts.hasNext) {
//            val name = parts.next()
//            if (!parts.hasNext && hidden.contains(name)) {
//              return value
//            }
//          }
//        }
//        Optional.absent()
//      }
//    }
//
//    def increment() {
//      if (this.counter == max) {
//        this.counter = 0
//        this.hidden.clear()
//      }
//      this.counter += 1
//      this.hidden.add(java.lang.Integer.toString(this.counter))
//      val text = new TextComponentString("" + this.counter)
//      if (this.worldObj.isRemote) Minecraft.getMinecraft.ingameGUI.getChatGUI.printChatMessage(text)
//    }
//
//    def decrement() {
//      if (this.counter == 1) {
//        this.counter = max + 1
//        for (i <- 1 until max) this.hidden.add(java.lang.Integer.toString(i))
//      }
//      this.hidden.remove(java.lang.Integer.toString(this.counter))
//      this.counter -= 1
//      val text = new TextComponentString("" + this.counter)
//      if (this.worldObj.isRemote) Minecraft.getMinecraft.ingameGUI.getChatGUI.printChatMessage(text)
//    }
//
//    def setMax(max: Int) {
//      this.max = max
//    }
//  }
//
//  object OBJVertexColoring1 {
//
//    val instance = new OBJVertexColoring1()
//
//    val name = "OBJVertexColoring1"
//  }
//
//  class OBJVertexColoring1 private () extends Block(Material.IRON) {
//
//    setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
//
//    setUnlocalizedName(name)
//
//    setRegistryName(new ResourceLocation(MODID, name))
//
//    override def isOpaqueCube(state: IBlockState): Boolean = false
//
//    override def isFullCube(state: IBlockState): Boolean = false
//
//    override def isVisuallyOpaque(): Boolean = false
//  }
//
//
//
//
//  object OBJVertexColoring2 {
//
//    val instance = new OBJVertexColoring2()
//
//    val name = "OBJVertexColoring2"
//  }
//
//  class OBJVertexColoring2 private () extends Block(Material.IRON) with ITileEntityProvider {
//
//    setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
//
//    setUnlocalizedName(name)
//
//    setRegistryName(new ResourceLocation(MODID, name))
//
//    override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = new OBJVertexColoring2TileEntity()
//
//    override def onBlockActivated(world: World,
//                                  pos: BlockPos,
//                                  state: IBlockState,
//                                  player: EntityPlayer,
//                                  hand: EnumHand,
//                                  heldItem: ItemStack,
//                                  side: EnumFacing,
//                                  hitX: Float,
//                                  hitY: Float,
//                                  hitZ: Float): Boolean = {
//      if (world.getTileEntity(pos) != null &&
//        world.getTileEntity(pos).isInstanceOf[OBJVertexColoring2TileEntity]) {
//        world.getTileEntity(pos).asInstanceOf[OBJVertexColoring2TileEntity]
//          .cycleColors()
//      }
//      false
//    }
//  }
//
//  class OBJVertexColoring2TileEntity extends TileEntity {
//
//    private var index: Int = 0
//
//    private var maxIndex: Int = 1
//
//    private var colorList: List[Vector4f] = new ArrayList[Vector4f]()
//
//    private var hasFilledList: Boolean = false
//
//    private var shouldIncrement: Boolean = true
//
//    def cycleColors() {
//      if (this.worldObj.isRemote) {
//        FMLLog.info("%b", shouldIncrement)
//      }
//    }
//  }
//
//
//  class OBJCustomDataBlock private () extends Block(Material.IRON) {
//
//    this.setDefaultState(this.blockState.getBaseState.withProperty(NORTH, false)
//      .withProperty(SOUTH, false)
//      .withProperty(WEST, false)
//      .withProperty(EAST, false))
//
//    setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
//
//    setUnlocalizedName(MODID + ":" + name)
//
//    setRegistryName(new ResourceLocation(MODID, name))
//
//    override def isOpaqueCube(state: IBlockState): Boolean = false
//
//    override def isFullCube(state: IBlockState): Boolean = false
//
//    override def getMetaFromState(state: IBlockState): Int = 0
//
//    def canConnectTo(world: IBlockAccess, pos: BlockPos): Boolean = {
//      val block = world.getBlockState(pos).getBlock
//      block.isInstanceOf[OBJCustomDataBlock]
//    }
//
//    override def getActualState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = {
//      state.withProperty(NORTH, this.canConnectTo(world, pos.north()))
//        .withProperty(SOUTH, this.canConnectTo(world, pos.south()))
//        .withProperty(WEST, this.canConnectTo(world, pos.west()))
//        .withProperty(EAST, this.canConnectTo(world, pos.east()))
//    }
//
//    override def createBlockState(): BlockStateContainer = {
//      new BlockStateContainer(this, Array(NORTH, SOUTH, WEST, EAST))
//    }
//  }
//
//
//}
//
//@Mod(modid = ModelLoaderRegistryDebug.MODID, version = ModelLoaderRegistryDebug.VERSION)
//class ModelLoaderRegistryDebug {
//
//  @EventHandler
//  def preInit(event: FMLPreInitializationEvent) {
//    val blocks = Lists.newArrayList()
//    blocks.add(CustomModelBlock.instance)
//    blocks.add(OBJTesseractBlock.instance)
//    blocks.add(OBJVertexColoring1.instance)
//    blocks.add(OBJVertexColoring2.instance)
//    blocks.add(OBJDirectionBlock.instance)
//    blocks.add(OBJCustomDataBlock.instance)
//    for (block <- blocks) {
//      GameRegistry.register(block)
//      GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName))
//    }
//    GameRegistry.registerTileEntity(classOf[OBJTesseractTileEntity], OBJTesseractBlock.name)
//    GameRegistry.registerTileEntity(classOf[OBJVertexColoring2TileEntity], OBJVertexColoring2.name)
//    if (event.getSide == Side.CLIENT) clientPreInit()
//  }
//
//  private def clientPreInit() {
//    B3DLoader.INSTANCE.addDomain(MODID.toLowerCase())
//    val item = Item.getItemFromBlock(CustomModelBlock.instance)
//    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + CustomModelBlock.name,
//      "inventory"))
//    OBJLoader.INSTANCE.addDomain(MODID.toLowerCase())
//    val item2 = Item.getItemFromBlock(OBJTesseractBlock.instance)
//    ModelLoader.setCustomModelResourceLocation(item2, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + OBJTesseractBlock.name,
//      "inventory"))
//    val item3 = Item.getItemFromBlock(OBJVertexColoring1.instance)
//    ModelLoader.setCustomModelResourceLocation(item3, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + OBJVertexColoring1.name,
//      "inventory"))
//    val item5 = Item.getItemFromBlock(OBJVertexColoring2.instance)
//    ModelLoader.setCustomModelResourceLocation(item5, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + OBJVertexColoring2.name,
//      "inventory"))
//    val item6 = Item.getItemFromBlock(OBJDirectionBlock.instance)
//    ModelLoader.setCustomModelResourceLocation(item6, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + OBJDirectionBlock.name,
//      "inventory"))
//    val item7 = Item.getItemFromBlock(OBJCustomDataBlock.instance)
//    ModelLoader.setCustomModelResourceLocation(item7, 0, new ModelResourceLocation(MODID.toLowerCase() + ":" + OBJCustomDataBlock.name,
//      "inventory"))
//  }
//}
