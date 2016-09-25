package net.machinemuse.powersuits.common

import java.net.URL

import net.machinemuse.general.sound.SoundDictionary
import net.machinemuse.numina.network.{MusePacket, MusePacketHandler, MusePacketModeChangeRequest, PacketSender}
import net.machinemuse.numina.render.RenderGameOverlayEventHandler
import net.machinemuse.powersuits.block.{BlockLuxCapacitor, BlockTinkerTable, TileEntityLuxCapacitor}
import net.machinemuse.powersuits.client.ModelLocations
import net.machinemuse.powersuits.client.render.block.RenderLuxCapacitorTESR
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader
import net.machinemuse.powersuits.control.{KeybindKeyHandler, KeybindManager}
import net.machinemuse.powersuits.event.{ClientTickHandler, PlayerLoginHandlerThingy, PlayerUpdateHandler, RenderEventHandler}
import net.machinemuse.powersuits.item.ItemComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.block.model.{ModelBakery, ModelResourceLocation}
import net.minecraft.item.Item
import net.minecraftforge.client.model.{ModelLoader, ModelLoaderRegistry}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * Common side of the proxy. Provides functions which
 * the ClientProxy and ServerProxy will override if the behaviour is different for client and
 * server, and keeps some common behaviour.
 *
 * @author MachineMuse
 */
trait CommonProxy {
  def registerEvents() {}

  def registerRenderers() {}

  def registerHandlers() {}

  def postInit() {}

  def sendModeChange(dMode: Int, newMode: String) {}
}

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
  override def registerEvents() {
    MinecraftForge.EVENT_BUS.register(new SoundDictionary)
  }


  override def registerRenderers() {


    // register component icons
    val mpsItems = MPSItems
    if (mpsItems.components != null) {
      var i = 0
      val nameList = ItemComponent.names

      for( i <- 1 to  nameList.size()) {
        val itemModelResourceLocation: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + nameList.get(i-1), "inventory")
        ModelLoader.setCustomModelResourceLocation(mpsItems.components, i-1, itemModelResourceLocation)
      }
    }
/*
//    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.instance), 0, ModelLocations.luxCapacitor)

//    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityTinkerTable], new TinkerTableRenderer)
    */





    val resource: URL = classOf[ClientProxy].getResource("/assets/powersuits/models/item/armor/modelspec.xml")
    ModelSpecXMLReader.parseFile(resource)
    val otherResource: URL = classOf[ClientProxy].getResource("/assets/powersuits/models/item/armor/armor2.xml")
    ModelSpecXMLReader.parseFile(otherResource)


    // Armor
    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorHead, 0, ModelLocations.powerArmorHead)
    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorTorso, 0, ModelLocations.powerArmorTorso)
    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorLegs, 0, ModelLocations.powerArmorLegs)
    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorFeet, 0, ModelLocations.powerArmorFeet)


    // PowerFist
    ModelBakery.registerItemVariants(mpsItems.powerTool,
      ModelLocations.powerFistGUI,
      ModelLocations.powerFistLeft,
      ModelLocations.powerFistLeftFiring,
      ModelLocations.powerFistRight,
      ModelLocations.powerFistRightFiring)

    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.instance), 0, ModelLocations.luxCapacitor)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityLuxCapacitor], new RenderLuxCapacitorTESR)

    // Try using
    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 0, ModelLocations.powerFistGUI)
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 1, ModelLocations.powerFistLeft)
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 2, ModelLocations.powerFistLeftFiring)
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 3, ModelLocations.powerFistRight)
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 4, ModelLocations.powerFistRightFiring)

    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockTinkerTable.instance), 0, ModelLocations.tinkerTable)
    /*
 * Loader for OBJ models.
 * To enable your mod call instance.addDomain(modid).
 * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
 */
    // Moved to java file because scala doesn't like the factory method
    RegEntityRenderers.Reg()

    ModelLoaderRegistry.registerLoader(OBJLoader.INSTANCE)

//    ModelHandler.INSTANCE.registerTexture("blocks/myfolder/texture");














    //    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityRenderItem], new ToolRenderer)
//    ForgeHooksClient.registerTESRItemStack(mpsItems.powerTool, 0, classOf[TileEntityRenderItem])


//        RenderingRegistry.registerEntityRenderingHandler(classOf[EntityPlasmaBolt], new IRenderFactory[EntityPlasmaBolt]() {
//          override def createRenderFor(manager: RenderManager): Render[_ >: EntityPlasmaBolt] = return new EntityRenderPlasmaBolt(manager)
//        })
//
//
//
//
//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntitySpinningBlade], new IRenderFactory[EntitySpinningBlade]() {
//      override def createRenderFor(manager: RenderManager): Render[_ >: EntitySpinningBlade] = return new EntityRenderSpinningBlade(manager)
//    })



//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntitySpinningBlade], new IRenderFactory[EntitySpinningBlade]() {
//      override def createRenderFor(manager: RenderManager): Render[_ >: EntitySpinningBlade] = {
//        return new EntityRenderSpinningBlade(manager)
//      }
//    })





//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntitySpinningBlade], new IRenderFactory[EntitySpinningBlade]() {
//      override def createRenderFor(manager: RenderManager): Render[_>: EntitySpinningBlade]  {
//        return new EntityRenderSpinningBlade(manager)
//      }
//    });
//





//    button.setOnAction(new EventHandler[ActionEvent] {
//      override def handle(event: ActionEvent): Unit = println("I'm an anonymous inner class")
//    })

//    Use RenderingRegistry#registerEntityRenderingHandler(Class<T>, IRenderFactory<? super T>) in preInit to register your renderer.
//
//      To implement IRenderFactory, I suggest using an anonymous class (if you're targeting Java 6/7) or a constructor method reference (if you're targeting Java 8).
//


//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityRisenSkeleton], new IRenderFactory[EntityRisenSkeleton]() {
//
//      override def createRenderFor(manager: RenderManager): Render[_ >: EntityRisenSkeleton] = return new RenderRisenSkeleton(manager)
//    })
//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityRisenWitherSkeleton], new IRenderFactory[EntityRisenWitherSkeleton]() {
//
//      override def createRenderFor(manager: RenderManager): Render[_ >: EntityRisenWitherSkeleton] = {
//        return new RenderRisenWitherSkeleton(manager)
//      }
//    })




















    //    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityPlasmaBolt], new EntityRenderPlasmaBolt)

    //ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityRenderItem], new RenderDiamondSwordHandBlender)
    //
      // registering items
      // registering entities
      // registering baked models
    //ForgeHooksClient.registerTESRItemStack(DiamondSwordHandBlenderCore.Items.itemDSHandBlender, 0, classOf[TileEntityRenderItem])

    /*

    Make two models (json or whatever you wish) for the two variants. In preInit call ModelBakery.registerItemVariants(<item>, <modelA>, <modelB>) where <modelA> and <modelB> are the ModelResourceLocations pointing to the models (you can use a blockstate json as well here that then points to the models, does not matter).

Create a class implementing IFlexibleBakedModel and IPerspectiveAwareModel. In handlePerspective check the TransformType and choose
the model to display based on it. You get the models using the ModelResourceLocations you registered above and
Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel.
Check if the model is an instance of IFlexibleBakedModel. If so, use it as is, otherwise wrap it using new IFlexibleBakedModel.Wrapper(<model>, DefaultVertexFormats.ITEM).
Then return Pair.of(<model>, TRSRTransformation.identy().getMatrix()), where <model> is the model you chose.
The other methods will never be called, you can have them throw exceptions.

Create a class implementing IModel. In getDependencies and getTextures return ImmutableList.of() (the empty list). In getDefaultState return TRSRTransformation.identity(). In bake return a new instance of your baked model class from above.

Create a class implementing ICustomModelLoader. In accepts, return true if the ResourceLocation is the one for your Item (bound via ModelLoader.setCustomModelResouceLocation). In loadModel return a new instance of the IModel class from above.
     */










//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFist-Left", "inventory"))
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFist-Right", "inventory"))
//
//
//
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorFeet, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorFeet", "inventory"))
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorFeet, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorFeet", "inventory"))
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorFeet, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorFeet", "inventory"))
//    ModelLoader.setCustomModelResourceLocation(mpsItems.powerArmorFeet, 0, new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorFeet", "inventory"))



//    val powerTool = MPSRegistry.registerItem(new ItemPowerFist, "powerTool", "powerFist")

    MinecraftForge.EVENT_BUS.register(new RenderEventHandler)
  }


  /**
   * Register all the custom renderers for this mod.
   */
  def registerRenderersOld() {
//    MinecraftForgeClient.registerItemRenderer(MPSItems.powerTool, new ToolRenderer)
//    val tinkTableRenderID: Int = RenderingRegistry.getNextAvailableRenderId
//    val tinkTableRenderer: TinkerTableRenderer = new TinkerTableRenderer(tinkTableRenderID)
//    BlockTinkerTable.setRenderType(tinkTableRenderID)
//    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityTinkerTable], tinkTableRenderer)
//    RenderingRegistry.registerBlockHandler(tinkTableRenderer)ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityLuxCapacitor], new RenderLuxCapacitorTESR)
//    val luxCapacitorRenderID: Int = RenderingRegistry.getNextAvailableRenderId
//    val luxCapacitorRenderer: RenderLuxCapacitorTESR = new RenderLuxCapacitorTESR(luxCapacitorRenderID)
//    MPSItems.luxCapacitor.setRenderType(luxCapacitorRenderID)
//    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityLuxCapacitor], luxCapacitorRenderer)
//    RenderingRegistry.registerBlockHandler(luxCapacitorRenderer)
//
//
//
//
//
//
//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityPlasmaBolt], new EntityRenderPlasmaBolt)
//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntitySpinningBlade], new EntityRenderSpinningBlade)
//    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityLuxCapacitor], new EntityRenderLuxCapacitorEntity)




  }

  /**
   * Register the tick handler (for on-tick behaviour) and packet handler (for
   * network synchronization and permission stuff).
   */
  override def registerHandlers() {
    MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler)

    MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler)
    MinecraftForge.EVENT_BUS.register(new ClientTickHandler)
    val packetHandler: MusePacketHandler.type = MusePacketHandler



  }

  override def postInit() {
    KeybindManager.readInKeybinds()
  }

  override def sendModeChange(dMode: Int, newMode: String) {
    val player: EntityPlayerSP = Minecraft.getMinecraft.thePlayer
    RenderGameOverlayEventHandler.updateSwap(Math.signum(dMode).asInstanceOf[Int])
    val modeChangePacket: MusePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem)
    PacketSender.sendToServer(modeChangePacket)
  }
}


class ServerProxy extends CommonProxy {
  override def registerEvents() {
    MinecraftForge.EVENT_BUS.register(PlayerLoginHandlerThingy)
  }

  override def registerHandlers() {
    MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler)
  }
}
