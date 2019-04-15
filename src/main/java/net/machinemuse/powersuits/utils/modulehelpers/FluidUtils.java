package net.machinemuse.powersuits.utils.modulehelpers;

import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.capabilities.MPSChestPlateFluidHandler;
import net.machinemuse.powersuits.gui.hud.FluidMeter;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class FluidUtils {
    ItemStack itemStack;
    EntityPlayer player;
    FluidTank fluidTank;
    String dataName;

    public FluidUtils(EntityPlayer player, ItemStack itemStack, String dataName) {
        this.player = player;
        this.itemStack = itemStack;
        this.dataName = dataName;

        IFluidHandlerItem fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        this.fluidTank = fluidHandler instanceof MPSChestPlateFluidHandler ?
                ((MPSChestPlateFluidHandler) fluidHandler).getFluidTank(dataName) : null;
    }

    public List<String> getFluidDisplayString() {
        List<String> currentTipList = new LinkedList<>();
        int fluidLevel = getFluidLevel();
        int maxFluidLevel = getMaxFluidLevel();

        if (fluidTank != null && maxFluidLevel > 0 && fluidLevel > 0) {
            String fluidInfo = I18n.format(fluidTank.getFluid().getLocalizedName()) + " " + MuseStringUtils.formatNumberShort(fluidLevel) + '/'
                    + MuseStringUtils.formatNumberShort(maxFluidLevel);

            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                    MuseStringUtils.FormatCodes.Indigo));

            fluidInfo = MuseStringUtils.formatNumberFromUnits(fluidTank.getFluid().getFluid().getTemperature() - 273.15D, "°C");

            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(fluidInfo, MuseStringUtils.FormatCodes.Italic.character,
                    MuseStringUtils.FormatCodes.Indigo));
        }
        return currentTipList;
    }


    public int getFluidLevel() {
        return fluidTank != null ? fluidTank.getFluidAmount() : 0;
    }

    public int getMaxFluidLevel() {
        return fluidTank != null ? fluidTank.getCapacity() : 0;
    }

    /*
     * This will probably make the assumption that 1 bucket = 1 liter or whatever Minecraft math applies
     * returns the amount in
     */
    public double getFluidMass() {
        FluidStack fluid = fluidTank != null ? fluidTank.getFluid() : null;

        if (fluid != null) {
            // fluidVolumeInBuckets * density
            return fluid.amount / 1000 * fluid.getFluid().getDensity();
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public FluidMeter getFluidMeter() {
        if (fluidTank.getFluid() != null) {
            return new FluidMeter(fluidTank.getFluid().getFluid());
        }
        return null;
    }


    public double getCoolingEfficiency() {
        FluidStack fluid = fluidTank != null ? fluidTank.getFluid() : null;
        if (fluid != null) {
            // closer to full greater heat transfer efficiency
            double volumeEfficiency = fluidTank.getFluidAmount() / fluidTank.getCapacity();

            int boilingPointOfWater = 100;
            int fluidTemperatureInCelsius = fluid.getFluid().getTemperature() - 273; // should be - 273.15 but we're not working with much precision here.

            int temperatureEfficiency = boilingPointOfWater - fluidTemperatureInCelsius;


            // TODO: viscosity bonus
            // so far water efficiency = cooling efficiency: 73.0

            return temperatureEfficiency * volumeEfficiency;
        }
        return 0;


        /**

         ( Some notes on how this evolved )

         Bonuses
         ------------------------------------

         volumeEfficiency = volume/maxVolume
         temperatureEfficiency = boilingPointOfWater - fluidTemperatureInCelsius



         ( volume/maxVolume ) * (boilingPointOfWater - fluidTemperatureInCelsius)



         T(K) = T(°C) + 273.15


         fluid has these attributes

         viscosity ( bonus for viscosity lower than water )
         density
         temperature
         amount
         doesVaporize

         mass * volumeEfficiency/ (joulesIn + fluidTemp)

         temperature 300  / ? = 750

         assume effiency of 75% for water cooling with full tank

         Heat = mass of object × change in temperature × specific heat capacity of material

         ideally, specific heat would be the best way to go, however, fluid temps are static..

         (baseCooling - fluidTemp) / tankEmptyness

         fluidTemperature( Kelvin ) * fluidAmount

         (fl * fluidAmount)

         the emptier the tank, the lowere the efficiency
         the hotter the fluid, the lower the efficiency
         the ...

         */


    }

    public int drain(int amount) {
        if (fluidTank != null) {
            fluidTank.drain(amount, true);
        }
        return 0;
    }

    public double getHeat() {
        return getFluidMass() * getFluidTemperature();
    }

    public int getViscosity() {
        FluidStack fluid = fluidTank != null ? fluidTank.getFluid() : null;

        if (fluid != null) {
            return fluid.getFluid().getViscosity();
        }
        return 0;
    }

    public int getFluidTemperature() {
        int fluidTemp = 300;
        if (fluidTank != null) {
            FluidStack fluid = fluidTank.getFluid();
            fluidTemp = fluid != null ? fluid.getFluid().getTemperature(fluid) : fluidTemp;
        }
        return fluidTemp - 273; // convert to celsius
    }


    public void fillWaterFromEnvironment() {
        if (this.dataName != MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME || fluidTank == null)
            return;

        // Fill with water block and remove that block from the world
        if (player.isInWater()) {
            // Fill tank if player is in water
            IBlockState iblockstate = player.world.getBlockState(player.getPosition());
            Material material = iblockstate.getMaterial();

            if (material == Material.WATER && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                Fluid fluid = FluidRegistry.lookupFluidForBlock(iblockstate.getBlock());
                player.world.setBlockState(player.getPosition(), Blocks.AIR.getDefaultState(), 11);
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                fluidTank.fill(new FluidStack(fluid, Math.min(getMaxFluidLevel() - getFluidLevel(), 1000)), false);
            }
        }

        // Fill tank if raining
        boolean isRaining = (player.world.getBiomeForCoordsBody(player.getPosition()).getRainfall() > 0) && (player.world.isRaining() || player.world.isThundering());
        if (isRaining && player.world.canBlockSeeSky(player.getPosition().add(0, 1, 0))
                && (player.world.getTotalWorldTime() % 5) == 0 && getFluidLevel() < getMaxFluidLevel()) {
            fluidTank.fill(new FluidStack(FluidRegistry.WATER, Math.min(getMaxFluidLevel() - getFluidLevel(), 100)), false);
        }
    }

// I think this was for the power fist and the personal shrinking device...
//    public static void setLiquid(@Nonnull ItemStack stack, String id) {
//        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//            itemTag.setString(TAG_LIQUID, id);
//        }
//    }
//
//    public static String getLiquid(@Nonnull ItemStack stack) {
//        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//            String s = itemTag.getString(TAG_LIQUID);
//            if (s != null) {
//                return s;
//            }
//        }
//        return "";
//    }
}
