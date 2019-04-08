package net.machinemuse.powersuits.client.event;


import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.MPSItems;
import net.machinemuse.powersuits.ModularPowersuits;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public enum ModelBakeEventHandler {
    INSTANCE;

    public static final ModelResourceLocation powerFistIconLocation = new ModelResourceLocation(MPSItems.INSTANCE.POWER_FIST__REG_NAME.toString(), "inventory");
    public static IBakedModel powerFistIconModel;

    private static Map<ModelResourceLocation, IBakedModel> modelRegistry;


    //    ModelResourceLocation tinkerTableLocation = new ModelResourceLocation(new ResourceLocation(ModularPowersuits.MODID, BlockTinkerTable.name).toString());


    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        modelRegistry = event.getModelRegistry();


        for (ResourceLocation location : modelRegistry.keySet()) {
//            MuseLogger.logInfo("model location namespace: " + location.getNamespace());

/*
[20:46:47.695] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:powerarmor_head#inventory
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:tinker_table#facing=east
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:tinker_table#facing=north
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:tinker_table#facing=south
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_magnet#inventory
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_servo#inventory
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:tinker_table#facing=west
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_lv_capacitor#inventory
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:tinker_table#inventory
[20:46:47.696] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_ion_thruster#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_wiring#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_control_circuit#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_laser_emitter#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:powerarmor_legs#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_myofiber_gel#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_artificial_muscle#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_mv_capacitor#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_plating_diamond#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_rubber_hose#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_carbon_myofiber#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_plating_iron#inventory
[20:46:47.697] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_hv_capacitor#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_computer_chip#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:powerarmor_feet#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:powerarmor_torso#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_glider_wing#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_ev_capacitor#inventory
[20:46:47.698] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_field_emitter#inventory
[20:46:47.699] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_solenoid#inventory
[20:46:47.699] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_solar_panel#inventory
[20:46:47.699] [Client thread/INFO] [MachineMuse/]: MPS model location: powersuits:component_parachute#inventory
 */

            if (location.getNamespace().equals(ModularPowersuits.MODID)) {
                MuseLogger.logInfo("MPS model location: " + location.toString());
            }
        }



    }
}
