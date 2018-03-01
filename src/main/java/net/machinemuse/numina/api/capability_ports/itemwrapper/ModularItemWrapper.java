package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ModularItemWrapper extends ItemStackHandler implements IModularItemCapability {
    protected ItemStack container;

    public ModularItemWrapper(@Nonnull ItemStack container, int slotCount) {
        super(slotCount);
        this.container = container;
    }






}
