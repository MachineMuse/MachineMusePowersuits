package net.machinemuse.powersuits.fluid;

import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidFinite;

public class BlockFluidLiquidNitrogen extends BlockFluidFinite {
    public BlockFluidLiquidNitrogen(ResourceLocation regName) {
        super(MPSItems.liquidNitrogen, Material.WATER);
        setRegistryName(regName);
        setTranslationKey(new StringBuilder(MPSModConstants.MODID).append(".").append(LiquidNitrogen.name).toString());
    }
}