package net.machinemuse.powersuits.api.item;

import forestry.api.apiculture.IArmorApiarist;
import net.machinemuse.numina.api.item.IArmorTraits;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:54 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "Forestry", striprefs = true)
public interface IApiaristArmor extends IArmorApiarist, IArmorTraits {
    @Optional.Method(modid = "forestry")
    boolean protectEntity(final EntityLivingBase player, final ItemStack armor, final String cause, final boolean doProtect);
}