package machinemuse.powersuits.common;

import machinemuse.powersuits.client.GuiTinkerTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new ContainerTinkerTable(player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new GuiTinkerTable(new ContainerTinkerTable(player, world, x, y,
				z));
	}
}
