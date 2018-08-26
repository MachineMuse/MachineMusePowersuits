package net.machinemuse.powersuits.fluid;

import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.awt.*;

public class LiquidNitrogen extends Fluid {

    // fixme: which constructor to use?

//    public LiquidNitrogen(Color color) {
//        super("liquid nitrogen", new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_still"),  new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"), color);
//    }

//    public LiquidNitrogen(Color color) {
//        super("liquid nitrogen", new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_still"),  new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"), overlay, color);
//    }

//    public LiquidNitrogen(int color) {
//        super("liquid nitrogen", new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_still"),  new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"), color);
//    }

//    public LiquidNitrogen(int color) {
//        super("liquid nitrogen", new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_still"),  new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"), overlay, color);
//    }

    public LiquidNitrogen() {
        super("liquid_nitrogen", new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_still"),  new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"));


        setTemperature(70);
        setViscosity(200);

    }

//    public LiquidNitrogen(@Nullable ResourceLocation overlay) {
//        super("liquid nitrogen", new ResourceLocation(MPSModConstants.MODID,  "fluids/liquid_nitrogen_still"), new ResourceLocation(MPSModConstants.MODID, "fluids/liquid_nitrogen_flow"), overlay);
//    }


//    @Override
//    public Fluid setBlock(Block block) {
//        return super.setBlock(block);
//    }
//
//    @Override
//    public Fluid setLuminosity(int luminosity) {
//        return super.setLuminosity(luminosity);
//    }
//
//    @Override
//    public Fluid setDensity(int density) {
//        return super.setDensity(density);
//    }
//
//    @Override
//    public Fluid setTemperature(int temperature) {
//        return super.setTemperature(temperature);
//    }
//
//    @Override
//    public Fluid setViscosity(int viscosity) {
//        return super.setViscosity(viscosity);
//    }
//
//    @Override
//    public Fluid setGaseous(boolean isGaseous) {
//        return super.setGaseous(isGaseous);
//    }
//
//    @Override
//    public Fluid setRarity(EnumRarity rarity) {
//        return super.setRarity(rarity);
//    }
//
//    @Override
//    public Fluid setFillSound(SoundEvent fillSound) {
//        return super.setFillSound(fillSound);
//    }
//
//    @Override
//    public Fluid setEmptySound(SoundEvent emptySound) {
//        return super.setEmptySound(emptySound);
//    }
//
//    @Override
//    public Fluid setColor(Color color) {
//        return super.setColor(color);
//    }
//
//    @Override
//    public Fluid setColor(int color) {
//        return super.setColor(color);
//    }
//
//    @Override
//    public boolean doesVaporize(FluidStack fluidStack) {
//        return super.doesVaporize(fluidStack);
//    }
//
//    @Override
//    public void vaporize(@Nullable EntityPlayer player, World worldIn, BlockPos pos, FluidStack fluidStack) {
//        super.vaporize(player, worldIn, pos, fluidStack);
//    }
//
//    @Override
//    public String getLocalizedName(FluidStack stack) {
//        return super.getLocalizedName(stack);
//    }
//
//    @Override
//    public String getUnlocalizedName(FluidStack stack) {
//        return super.getUnlocalizedName(stack);
//    }
//
//    @Override
//    public String getUnlocalizedName() {
//        return super.getUnlocalizedName();
//    }
//
//    @Override
//    public EnumRarity getRarity() {
//        return super.getRarity();
//    }
//
//    @Override
//    public int getColor() {
//        return super.getColor();
//    }
//
//    @Override
//    public ResourceLocation getStill() {
//        return super.getStill();
//    }
//
//    @Override
//    public ResourceLocation getFlowing() {
//        return super.getFlowing();
//    }
//
//    @Nullable
//    @Override
//    public ResourceLocation getOverlay() {
//        return super.getOverlay();
//    }
//
//    @Override
//    public SoundEvent getFillSound() {
//        return super.getFillSound();
//    }
//
//    @Override
//    public SoundEvent getEmptySound() {
//        return super.getEmptySound();
//    }
//
//    @Override
//    public int getLuminosity(FluidStack stack) {
//        return super.getLuminosity(stack);
//    }
//
//    @Override
//    public int getDensity(FluidStack stack) {
//        return super.getDensity(stack);
//    }
//
//    @Override
//    public int getTemperature(FluidStack stack) {
//        return super.getTemperature(stack);
//    }
//
//    @Override
//    public int getViscosity(FluidStack stack) {
//        return super.getViscosity(stack);
//    }
//
//    @Override
//    public boolean isGaseous(FluidStack stack) {
//        return super.isGaseous(stack);
//    }
//
//    @Override
//    public EnumRarity getRarity(FluidStack stack) {
//        return super.getRarity(stack);
//    }
//
//    @Override
//    public int getColor(FluidStack stack) {
//        return super.getColor(stack);
//    }
//
//    @Override
//    public ResourceLocation getStill(FluidStack stack) {
//        return super.getStill(stack);
//    }
//
//    @Override
//    public ResourceLocation getFlowing(FluidStack stack) {
//        return super.getFlowing(stack);
//    }
//
//    @Override
//    public SoundEvent getFillSound(FluidStack stack) {
//        return super.getFillSound(stack);
//    }
//
//    @Override
//    public SoundEvent getEmptySound(FluidStack stack) {
//        return super.getEmptySound(stack);
//    }
//
//    @Override
//    public int getLuminosity(World world, BlockPos pos) {
//        return super.getLuminosity(world, pos);
//    }
//
//    @Override
//    public int getDensity(World world, BlockPos pos) {
//        return super.getDensity(world, pos);
//    }
//
//    @Override
//    public int getTemperature(World world, BlockPos pos) {
//        return super.getTemperature(world, pos);
//    }
//
//    @Override
//    public int getViscosity(World world, BlockPos pos) {
//        return super.getViscosity(world, pos);
//    }
//
//    @Override
//    public boolean isGaseous(World world, BlockPos pos) {
//        return super.isGaseous(world, pos);
//    }
//
//    @Override
//    public EnumRarity getRarity(World world, BlockPos pos) {
//        return super.getRarity(world, pos);
//    }
//
//    @Override
//    public int getColor(World world, BlockPos pos) {
//        return super.getColor(world, pos);
//    }
//
//    @Override
//    public ResourceLocation getStill(World world, BlockPos pos) {
//        return super.getStill(world, pos);
//    }
//
//    @Override
//    public ResourceLocation getFlowing(World world, BlockPos pos) {
//        return super.getFlowing(world, pos);
//    }
//
//    @Override
//    public SoundEvent getFillSound(World world, BlockPos pos) {
//        return super.getFillSound(world, pos);
//    }
//
//    @Override
//    public SoundEvent getEmptySound(World world, BlockPos pos) {
//        return super.getEmptySound(world, pos);
//    }
}
