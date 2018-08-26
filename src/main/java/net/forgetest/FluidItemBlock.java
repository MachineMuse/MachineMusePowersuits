package net.forgetest;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;

public class FluidItemBlock extends ItemBlock {
    FluidItemBlock(BlockFluidBase block) {
        super(block);
    }

    @Override
    public BlockFluidBase getBlock() {
        return (BlockFluidBase) super.getBlock();
    }

    @Override
    public int getMetadata(int damage) {
        return getBlock().getMaxRenderHeightMeta();
    }
}