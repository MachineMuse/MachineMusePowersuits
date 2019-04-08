package net.machinemuse.powersuits.client.event;


public class EventRegisterRenderers {
//    @SubscribeEvent
//    public void registerRenderers(ModelRegistryEvent event) {
//        MPSItems mpsItems = MPSItems.INSTANCE;
//
//        // PowerFist
//        regRenderer(mpsItems.powerFist);
//
//        // Armor
//        regRenderer(mpsItems.powerArmorHead);
//        regRenderer(mpsItems.powerArmorTorso);
//        regRenderer(mpsItems.powerArmorLegs);
//        regRenderer(mpsItems.powerArmorFeet);
//
//        // Tinker Table
//        regRenderer(Item.getItemFromBlock(MPSItems.INSTANCE.tinkerTable));
//
//        // Lux Capacitor
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MPSItems.INSTANCE.luxCapacitor), 0, ModelLuxCapacitor.modelResourceLocation);
//
//        // Components
//        Item components = mpsItems.components;
//        if (components != null) {
//            for (Integer meta : ((ItemComponent) components).names.keySet()) {
//                String oredictName = ((ItemComponent) components).names.get(meta);
//                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(MPSResourceConstants.COMPONENTS_PREFIX + oredictName, "inventory");
//                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);
//                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
//            }
//        }
//
//        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);
//
//
//
//
//
//
//
//        ModelResourceLocation liquid_nitrogen_location = new ModelResourceLocation(MPSItems.INSTANCE.blockLiquidNitrogen.getRegistryName(), "normal");
//        Item fluid = Item.getItemFromBlock(MPSItems.INSTANCE.blockLiquidNitrogen);
//
//        ModelBakery.registerItemVariants(fluid);
//        ModelLoader.setCustomMeshDefinition(fluid, stack -> liquid_nitrogen_location);
//        ModelLoader.setCustomStateMapper(MPSItems.INSTANCE.blockLiquidNitrogen, new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return liquid_nitrogen_location;
//            }
//        });
//
//    }
//
////
////    ModelResourceLocation fluidLocation = new ModelResourceLocation(MODID.toLowerCase() + ":" + FiniteFluidBlock.id, "normal");
////
////    Item fluid = Item.getItemFromBlock(FiniteFluidBlock.instance);
////            ModelLoader.setCustomModelResourceLocation(EmptyFluidContainer.instance, 0, new ModelResourceLocation("forge:bucket", "inventory"));
////            ModelLoader.setBucketModelDefinition(FluidContainer.instance);
////    // no need to pass the locations here, since they'll be loaded by the block model logic.
////            ModelBakery.registerItemVariants(fluid);
////            ModelLoader.setCustomMeshDefinition(fluid, stack -> fluidLocation);
////            ModelLoader.setCustomStateMapper(MPSItems.INSANCE., new StateMapperBase() {
////        @Override
////        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
////            return fluidLocation;
////        }
////    });
//
//
//    private void regRenderer(Item item) {
//        ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
//        ModelLoader.setCustomModelResourceLocation(item, 0, location);
//    }
}