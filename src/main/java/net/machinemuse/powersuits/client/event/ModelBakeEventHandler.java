package net.machinemuse.powersuits.client.event;

import net.machinemuse.powersuits.client.models.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.models.ModelPowerFist;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Ported to Java by lehjr on 12/22/16.
 */
@SideOnly(Side.CLIENT)
public class ModelBakeEventHandler {
    private ModelBakeEventHandler(){
    }
    private static ModelBakeEventHandler INSTANCE;

    public static ModelBakeEventHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelBakeEventHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelBakeEventHandler();
                }
            }
        }
        return INSTANCE;
    }


    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        event.getModelRegistry().putObject(ModelLuxCapacitor.modelResourceLocation, new ModelLuxCapacitor());

        for (EnumFacing facing : EnumFacing.VALUES) {
            event.getModelRegistry().putObject(ModelLuxCapacitor.getModelResourceLocation(facing), new ModelLuxCapacitor());
        }

        for(ModelResourceLocation resLoc : event.getModelRegistry().getKeys()) {
            if (resLoc.getResourcePath().contains("powersuits"))
                System.out.println(resLoc.toString());
        }

        IBakedModel powerFistIcon = event.getModelRegistry().getObject(ModelPowerFist.modelResourceLocation);
        event.getModelRegistry().putObject(ModelPowerFist.modelResourceLocation, new ModelPowerFist(powerFistIcon));

        // todo: move to login event after config settings
        MPSSettings.loadModelSpecs(null);



    }
}
