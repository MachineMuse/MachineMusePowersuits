package net.machinemuse.powersuits.control;

import net.minecraft.entity.player.EntityPlayer;

public interface IBindableControl {
	public void onActivate(EntityPlayer player);

	public void onDeactivate(EntityPlayer player);

}
