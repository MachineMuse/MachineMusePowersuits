package net.machinemuse.general.gui.frame;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

public class PortableCraftingContainer extends ContainerWorkbench {
    public PortableCraftingContainer(InventoryPlayer inventoryPlayer, World world, int x, int y, int z) {
        super(inventoryPlayer, world, x, y, z);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public void onCraftGuiClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }
}