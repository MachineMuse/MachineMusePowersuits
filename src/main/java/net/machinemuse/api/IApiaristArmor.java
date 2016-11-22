package net.machinemuse.api;

import forestry.api.apiculture.IArmorApiarist;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:54 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "Forestry", striprefs = true)
public interface IApiaristArmor extends IArmorApiarist {
    @Optional.Method(modid = "Forestry")
    boolean protectEntity(final EntityLivingBase player, final ItemStack armor, final String cause, final boolean doProtect);
}