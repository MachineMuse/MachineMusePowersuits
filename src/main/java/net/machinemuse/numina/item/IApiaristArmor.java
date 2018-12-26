package net.machinemuse.numina.item;

import forestry.api.apiculture.IArmorApiarist;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:54 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "forestry", striprefs = true)
public interface IApiaristArmor extends IArmorApiarist {
    @Optional.Method(modid = "forestry")
    boolean protectEntity(final EntityLivingBase player, final ItemStack armor, final String cause, final boolean doProtect);
}