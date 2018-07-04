package net.machinemuse.powersuits.item;

//import appeng.api.config.AccessRestriction;

import net.machinemuse.numina.utils.math.Colour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements IModularItemBase {
    public ItemElectricArmor(ItemArmor.ArmorMaterial material, int renderIndexIn, EntityEquipmentSlot slot) {
        super(material, renderIndexIn, slot);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    //=================================================================================
    @Override
    public boolean hasColor(ItemStack stack) {
        return true ;
    }

    @Override
    public int getColor(ItemStack stack) {
        Colour c = this.getColorFromItemStack(stack);
//        setColor(stack, c.getInt());

        return c.getInt();
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }
}