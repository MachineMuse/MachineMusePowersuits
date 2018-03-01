package net.machinemuse.numina.api.module;

import net.minecraft.item.ItemStack;

/**
 * Interface for a module to use a damage/repair mechanic
 */
public interface IBreakableModule {

    default boolean isDamageable(){
        return true;
    }

    /**
     * Called by CraftingManager to determine if an item is reparable.
     *
     * @return True if reparable
     */
    default boolean isRepairable() {
        return true;
    }

    /**
     * Determines if the durability bar should be rendered for this item.
     * Defaults to vanilla stack.isDamaged behavior.
     * But modders can use this for any data they wish.
     *
     * @param stack The current Item Stack
     * @return True if it should render the 'durability' bar.
     */
    default boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack The current ItemStack
     * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
     */
    default double getDurabilityForDisplay(ItemStack stack) {
        return (double) stack.getItemDamage() / (double) stack.getMaxDamage();
    }

//    /**
//     * Returns the packed int RGB value used to render the durability bar in the GUI.
//     * Defaults to a value based on the hue scaled based on {@link #getDurabilityForDisplay}, but can be overriden.
//     *
//     * @param stack Stack to get durability from
//     * @return A packed RGB value for the durability colour (0x00RRGGBB)
//     */
//    public int getRGBDurabilityForDisplay(ItemStack stack) {
//        return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
//    }

    /**
     * Return the maxDamage for this ItemStack. Defaults to the maxDamage field in this item,
     * but can be overridden here for other sources such as NBT.
     *
     * @param stack The itemstack that is damaged
     * @return the damage value
     */
    int getMaxDamage(ItemStack stack);

    /**
     * Return if this itemstack is damaged. Note only called if {@link #isDamageable()} is true.
     *
     * @param stack the stack
     * @return if the stack is damaged
     */
    boolean isDamaged(ItemStack stack);

    /**
     * Set the damage for this itemstack. Note, this method is responsible for zero checking.
     *
     * @param stack  the stack
     * @param damage the new damage value
     */
    void setDamage(ItemStack stack, int damage);
}
