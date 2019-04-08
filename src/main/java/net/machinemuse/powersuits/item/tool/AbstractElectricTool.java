package net.machinemuse.powersuits.item.tool;

import net.machinemuse.powersuits.basemod.MPSItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

import java.util.HashSet;
import java.util.Set;

public class AbstractElectricTool extends ItemTool {
    private final Set<Block> effectiveBlocks = new HashSet<>();

    public AbstractElectricTool() {
        super(0.0F,
                0.0F,
                MPSToolMaterial.EMPTY_TOOL,
                new HashSet<>(),
                new Item.Properties().group(MPSItems.INSTANCE.creativeTab).maxStackSize(1).defaultMaxDamage(0));
    }
}