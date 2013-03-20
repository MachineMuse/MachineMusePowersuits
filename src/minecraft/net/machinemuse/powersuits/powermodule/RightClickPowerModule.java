package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RightClickPowerModule extends PowerModule implements IRightClickModule {
	public RightClickPowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems, textureFile, category);
	}

	@Override
	public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
	}

}
