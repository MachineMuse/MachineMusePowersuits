package net.machinemuse.powersuits.event

import net.machinemuse.powersuits.client.render.item.PowerFistModel
import net.machinemuse.powersuits.client.{MPSModels, ModelLocations}
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
  * Created by leon on 8/1/16.
  */
class ModelBakeEventHandler {




  @SubscribeEvent def onbakeModels(event: ModelBakeEvent) {
    MPSModels.powerFistGUImodel = event.getModelRegistry.getObject(ModelLocations.powerFistGUI)
    MPSModels.powerFistLeftmodel = event.getModelRegistry.getObject(ModelLocations.powerFistLeft)
    MPSModels.powerFistLeftFiringmodel = event.getModelRegistry.getObject(ModelLocations.powerFistLeftFiring)
    MPSModels.powerFistRightmodel = event.getModelRegistry.getObject(ModelLocations.powerFistRight)
    MPSModels.powerFistRightFiringmodel = event.getModelRegistry.getObject(ModelLocations.powerFistRightFiring)
//    MPSModels.luxCapacitormodel = event.getModelRegistry.getObject(ModelLocations.luxCapacitor)








  val modelPowerFist: PowerFistModel = new PowerFistModel(
                                  event.getModelRegistry.getObject(ModelLocations.powerFistGUI),
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(MPSItems.powerTool, 1,1)),
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(MPSItems.powerTool, 1,2)),
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(MPSItems.powerTool, 1,3)),
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(MPSItems.powerTool, 1,4)))

//                                  event.getModelRegistry.getObject(ModelLocations.powerFistLeft),
//                                  event.getModelRegistry.getObject(ModelLocations.powerFistLeftFiring),
//                                  event.getModelRegistry.getObject(ModelLocations.powerFistRight),
//                                  event.getModelRegistry.getObject(ModelLocations.powerFistRightFiring))
                                  MPSModels.powerFistLeftBaked,
                                  MPSModels.powerFistRightFiringBaked,
                                  MPSModels.powerFistRightBaked,
                                  MPSModels.powerFistRightFiringBaked)


    event.getModelRegistry.putObject(ModelLocations.powerFistGUI, modelPowerFist)
    event.getModelRegistry.putObject(ModelLocations.powerFistLeft, modelPowerFist)
    event.getModelRegistry.putObject(ModelLocations.powerFistLeftFiring, modelPowerFist)
    event.getModelRegistry.putObject(ModelLocations.powerFistRight, modelPowerFist)
    event.getModelRegistry.putObject(ModelLocations.powerFistRightFiring, modelPowerFist)
  }
}
