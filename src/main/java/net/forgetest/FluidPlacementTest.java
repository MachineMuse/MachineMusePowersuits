package net.forgetest;

/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = FluidPlacementTest.MODID, name = "ForgeDebugFluidPlacement", version = FluidPlacementTest.VERSION, acceptableRemoteVersions = "*")
public class FluidPlacementTest {
    public static final String MODID = "forgedebugfluidplacement";
    public static final String VERSION = "1.0";

    public static final boolean ENABLE = true;

    @Mod.EventBusSubscriber(modid = MODID)
    public static class Registration {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            if (!ENABLE || !ModelFluidTest.ENABLE)
                return;
            event.getRegistry().registerAll(
                    FiniteFluidBlock.instance
            );
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            if (!ENABLE || !ModelFluidTest.ENABLE)
                return;
            FluidRegistry.registerFluid(FiniteFluid.instance);
            FluidRegistry.addBucketForFluid(FiniteFluid.instance);
            event.getRegistry().registerAll(
                    EmptyFluidContainer.instance,
                    FluidContainer.instance,
                    new FluidItemBlock(FiniteFluidBlock.instance).setRegistryName(FiniteFluidBlock.instance.getRegistryName())
            );
            MinecraftForge.EVENT_BUS.register(FluidContainer.instance);
        }
    }

    @Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
    public static class ClientEventHandler {
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            if (!ENABLE || !ModelFluidTest.ENABLE)
                return;
            ModelResourceLocation fluidLocation = new ModelResourceLocation(MODID.toLowerCase() + ":" + FiniteFluidBlock.name, "normal");

            Item fluid = Item.getItemFromBlock(FiniteFluidBlock.instance);
            ModelLoader.setCustomModelResourceLocation(EmptyFluidContainer.instance, 0, new ModelResourceLocation("forge:bucket", "inventory"));
            ModelLoader.setBucketModelDefinition(FluidContainer.instance);
            // no need to pass the locations here, since they'll be loaded by the block model logic.
            ModelBakery.registerItemVariants(fluid);
            ModelLoader.setCustomMeshDefinition(fluid, stack -> fluidLocation);
            ModelLoader.setCustomStateMapper(FiniteFluidBlock.instance, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return fluidLocation;
                }
            });
        }
    }









}