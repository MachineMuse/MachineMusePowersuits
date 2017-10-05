package net.machinemuse_old.powersuits.common;

import net.machinemuse_old.general.gui.*;
import net.machinemuse_old.general.gui.frame.PortableCraftingContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui handler for this mod. Mainly just takes an ID according to what was
 * passed to player.OpenGUI, and opens the corresponding GUI.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/3/16.
 */
public final class MPSGuiHandler implements IGuiHandler {
    private static MPSGuiHandler INSTANCE;

    public static MPSGuiHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MPSGuiHandler();
        return INSTANCE;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 4)
            return new PortableCraftingContainer(player.inventory, world, new BlockPos(x,y,z));
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Minecraft.getMinecraft().thePlayer.addStat(AchievementList.OPEN_INVENTORY, 1);
        switch (ID) {
            case 0:
                return new GuiTinkerTable(player, x, y, z);
            case 1:
                return new KeyConfigGui(player, x, y, z);
            case 2:
                return new GuiFieldTinker(player);
            case 3:
                return new CosmeticGui(player, x, y, z);
            case 4:
                return new PortableCraftingGui(player, world, new BlockPos(x,y,z));
            case 5:
            	return new GuiModeSelector(player);
            default:
                return null;
        }
    }
}
