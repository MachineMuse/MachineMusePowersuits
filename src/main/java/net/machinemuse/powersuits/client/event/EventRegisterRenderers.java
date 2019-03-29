package net.machinemuse.powersuits.client.event;

import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

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
        regRenderer(Item.getItemFromBlock(mpsItems.tinkerTable));

        // Lux Capacitor
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(mpsItems.luxCapacitor), 0, ModelLuxCapacitor.modelResourceLocation);

        // Components
        Item components = mpsItems.components;
        if (components != null) {
            for (Integer meta : ((ItemComponent) components).names.keySet()) {
                String oredictName = ((ItemComponent) components).names.get(meta);
                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(MPSResourceConstants.COMPONENTS_PREFIX + oredictName, "inventory");
                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);
                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
            }
        }

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);

        ModelResourceLocation liquid_nitrogen_location = new ModelResourceLocation(MPSItems.INSTANCE.blockLiquidNitrogenName, "normal");
        Item fluid = Item.getItemFromBlock(mpsItems.blockLiquidNitrogen);

        ModelBakery.registerItemVariants(fluid);
        ModelLoader.setCustomMeshDefinition(fluid, stack -> liquid_nitrogen_location);
        ModelLoader.setCustomStateMapper(mpsItems.blockLiquidNitrogen, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return liquid_nitrogen_location;
            }
        });
    }

    private void regRenderer(Item item) {
        ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, location);
    }
}