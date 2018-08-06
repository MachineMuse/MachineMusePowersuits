package net.machinemuse.powersuits.client.event;

import api.player.model.ModelPlayerAPI;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.client.model.item.armor.SMovingArmorModel;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

public class EventRegisterRenderers {
    @SubscribeEvent
    public void registerRenderers(ModelRegistryEvent event) {
        MPSItems mpsItems = MPSItems.INSTANCE;

        // PowerFist
        regRenderer(mpsItems.powerFist);

        // Armor
        regRenderer(mpsItems.powerArmorHead);
        regRenderer(mpsItems.powerArmorTorso);
        regRenderer(mpsItems.powerArmorLegs);
        regRenderer(mpsItems.powerArmorFeet);

        // Tinker Table
        regRenderer(Item.getItemFromBlock(BlockTinkerTable.getInstance()));

        // Lux Capacitor
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.getInstance()), 0, ModelLuxCapacitor.modelResourceLocation);

        // Components
        Item components = mpsItems.components;
        if (components != null) {
            for (Integer  meta : ((ItemComponent)components).names.keySet()) {
                String oredictName = ((ItemComponent)components).names.get(meta);
                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(MPSResourceConstants.COMPONENTS_PREFIX  + oredictName, "inventory");
                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);
                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
            }
        }

//        // Modules --------------------------------------------------------------------------------
//        // Debug
//        regRenderModule(mpsItems.module_debug, "debug", "debug_module");
//
        // Armor
//        regRenderModule(mpsItems.module_leather_plating, "armor","leather_plating");
//        regRenderModule(mpsItems.module_iron_plating, "armor", "iron_plating");
//        regRenderModule(mpsItems.module_diamond_plating, "armor", "diamond_plating");
//        regRenderModule(mpsItems.module_energy_shield, "armor", "energy_shield");
//
//        // Cosmetic
//        regRenderModule(mpsItems.module_citizen_joe, "cosmetic", "citizen_joe");
//        regRenderModule(mpsItems.module_cosmetic_glow, "cosmetic", "cosmetic_glow");
//        regRenderModule(mpsItems.module_3d_armor, "cosmetic", "3d_armor");
//        regRenderModule(mpsItems.module_tint, "cosmetic", "tint");
//        regRenderModule(mpsItems.module_transparent_armor, "cosmetic", "transparent_armor");
//
//        // Energy ---------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_basic_battery, "energy", "basic_battery");
//        regRenderModule(mpsItems.module_advanced_battery, "energy", "advanced_battery");
//        regRenderModule(mpsItems.module_elite_battery, "energy", "elite_battery");
//        regRenderModule(mpsItems.module_ultimate_battery, "energy", "ultimate_battery");
//        regRenderModule(mpsItems.module_solar_generator, "energy", "solar_generator");
//        regRenderModule(mpsItems.module_advanced_generator, "energy", "advanced_generator");
//        regRenderModule(mpsItems.module_coal_generator, "energy", "coal_generator");
//        regRenderModule(mpsItems.module_kinetic_generator, "energy", "kinetic_generator");
//        regRenderModule(mpsItems.module_stirling_generator, "energy", "stirling_generator");
//
//        // Environmental --------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_autofeeder, "environmental", "autofeeder");
//        regRenderModule(mpsItems.module_airtight_seal, "environmental", "airtight_seal");
//        regRenderModule(mpsItems.module_apiarist_armor, "environmental", "apiarist_armor");
//        regRenderModule(mpsItems.module_cooling_system, "environmental", "cooling_system");
//        regRenderModule(mpsItems.module_hazmat, "environmental", "hazmat");
//        regRenderModule(mpsItems.module_mechanical_assistance, "environmental", "mob_repulsor");
//        regRenderModule(mpsItems.module_mob_repulsor, "environmental", "mob_repulsor");
//        regRenderModule(mpsItems.module_heat_sink, "environmental", "heat_sink");
//        regRenderModule(mpsItems.module_nitrogen_cooling_system, "environmental", "nitrogen_cooling_system");
//        regRenderModule(mpsItems.module_water_electrolyzer, "environmental", "water_electrolyzer");
//        regRenderModule(mpsItems.module_water_tank, "environmental", "water_tank");
//
//        // Movement -------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_blink_drive, "movement", "blink_drive");
//        regRenderModule(mpsItems.module_climb_assist, "movement", "climb_assist");
//        regRenderModule(mpsItems.module_flight_control, "movement", "flight_control");
//        regRenderModule(mpsItems.module_glider, "movement", "glider");
//        regRenderModule(mpsItems.module_jet_boots, "movement", "jet_boots");
//        regRenderModule(mpsItems.module_jetpack, "movement", "jetpack");
//        regRenderModule(mpsItems.module_jump_assist, "movement", "jump_assist");
//        regRenderModule(mpsItems.module_parachute, "movement", "parachute");
//        regRenderModule(mpsItems.module_shock_absorber, "movement", "shock_absorber");
//        regRenderModule(mpsItems.module_sprint_assist, "movement", "sprint_assist");
//        regRenderModule(mpsItems.module_swim_boost, "movement", "swim_boost");
//
//        // Special --------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_clock, "special", "clock");
//        regRenderModule(mpsItems.module_compass, "special", "compass");
//        regRenderModule(mpsItems.module_invisibility, "special", "invisibility");
//        regRenderModule(mpsItems.module_magnet, "special", "magnet");
//
//        // Vision ---------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_binnoculars, "vision", "binnoculars");
//        regRenderModule(mpsItems.module_night_vision, "vision", "night_vision");
//        regRenderModule(mpsItems.module_aurameter, "special", "aurameter");
//
//        // Tools ----------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_pickaxe, "tool", "pickaxe");
//        regRenderModule(mpsItems.module_diamond_pick_upgrade, "tool", "diamond_pick_upgrade");
//        regRenderModule(mpsItems.module_aoe_pick_upgrade, "tool", "aoe_pick_upgrade");
//        regRenderModule(mpsItems.module_axe, "tool", "axe");
//        regRenderModule(mpsItems.module_shears, new ResourceLocation("minecraft", "shears"));
//        regRenderModule(mpsItems.module_shovel, "tool", "shovel");
//        regRenderModule(mpsItems.module_appeng_ec_wireless_fluid, "tool", "appeng_ec_wireless_fluid");
//        regRenderModule(mpsItems.module_appeng_wireless, "tool", "appeng_wireless");
//        regRenderModule(mpsItems.module_aqua_affinity, "tool", "aqua_affinity");
//        regRenderModule(mpsItems.module_chisel, "tool", "chisel");
//        regRenderModule(mpsItems.module_dim_rift_gen, "tool", "dim_rift_gen");
//        regRenderModule(mpsItems.module_field_tinkerer, "tool", "field_tinkerer");
//        regRenderModule(mpsItems.module_flint_and_steel, new ResourceLocation("minecraft", "flint_and_steel"));
//        regRenderModule(mpsItems.module_grafter, new ResourceLocation("forestry", "grafter"));
//        regRenderModule(mpsItems.module_hoe, new ResourceLocation("minecraft", "golden_hoe"));
//        regRenderModule(mpsItems.module_leafblower, "tool", "leafblower");
//        regRenderModule(mpsItems.module_luxcaplauncher, "tool", "luxcaplauncher");
//        regRenderModule(mpsItems.module_mffsfieldteleporter, "tool", "mffsfieldteleporter");
//// regRenderModule(mpsItems.module_octerminal, "tool", "octerminal);
//        regRenderModule(mpsItems.module_omniprobe, "tool", "omniprobe");
//        regRenderModule(mpsItems.module_omniwrench, "tool", "omniwrench");
//        regRenderModule(mpsItems.module_ore_scanner, "tool", "ore_scanner");
//        regRenderModule(mpsItems.module_cmpsd, new ResourceLocation("cm2", "psd"));
//        regRenderModule(mpsItems.module_portable_crafting_table, new ResourceLocation("minecraft", "crafting_table"));
//        regRenderModule(mpsItems.module_refinedstoragewirelessgrid, //"tool", "refinedstoragewirelessgrid");
//        new ResourceLocation("refinedstorage", "wireless_grid"));
//
//        regRenderModule(mpsItems.module_scoop, new ResourceLocation("forestry", "scoop"));
//        regRenderModule(mpsItems.module_tree_tap, new ResourceLocation("ic2", "tool/electric/electric_treetap"));
//
//        // Weapon ---------------------------------------------------------------------------------
//        regRenderModule(mpsItems.module_blade_launcher, "weapon", "blade_launcher");
//        regRenderModule(mpsItems.module_lightning, "weapon", "lightning");
//        regRenderModule(mpsItems.module_melee_assist, "weapon", "melee_assist");
//        regRenderModule(mpsItems.module_plasma_cannon, "weapon", "plasma_cannon");
//        regRenderModule(mpsItems.module_railgun, "weapon", "railgun");


//        // Capacitors
//        ModelLoader.setCustomModelResourceLocation(mpsItems.capacitor, 0, new ModelResourceLocation(new ResourceLocation(MODID, "module/energy/lv_capacitor"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(mpsItems.capacitor, 1, new ModelResourceLocation(new ResourceLocation(MODID, "module/energy/mv_capacitor"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(mpsItems.capacitor, 2, new ModelResourceLocation(new ResourceLocation(MODID, "module/energy/hv_capacitor"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(mpsItems.capacitor, 3, new ModelResourceLocation(new ResourceLocation(MODID, "module/energy/ev_capacitor"), "inventory"));

        if (ModCompatibility.isRenderPlayerAPILoaded()) {
            ModelPlayerAPI.register(MODID, SMovingArmorModel.class);
        }

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);
    }


    private void regRenderer(Item item) {
        ModelResourceLocation location =  new ModelResourceLocation(item.getRegistryName(), "inventory");
        System.out.println("location: " + location);


        ModelLoader.setCustomModelResourceLocation(item, 0,location);
    }

    private void regRenderModule(Item item, String subfolder, String fileName) {
        ModelResourceLocation location =  new ModelResourceLocation(
                new ResourceLocation(MODID, "module/" + subfolder + "/"+ fileName), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0,location);
    }

    private void regRenderModule(Item item, ResourceLocation resLocation) {
        regRenderModule(item, resLocation, 0);
    }

    private void regRenderModule(Item item, ResourceLocation resLocation, int meta) {
        ModelResourceLocation location =  new ModelResourceLocation(resLocation, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, meta,location);
    }

}