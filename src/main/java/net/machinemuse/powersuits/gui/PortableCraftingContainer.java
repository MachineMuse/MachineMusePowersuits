package net.machinemuse.powersuits.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortableCraftingContainer extends ContainerWorkbench {
    public PortableCraftingContainer(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
        super(inventoryPlayer, world, pos);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}