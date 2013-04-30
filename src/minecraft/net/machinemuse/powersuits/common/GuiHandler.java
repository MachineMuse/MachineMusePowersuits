package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.network.IGuiHandler;
import net.machinemuse.general.gui.GuiFieldTinker;
import net.machinemuse.general.gui.GuiTinkerTable;
import net.machinemuse.general.gui.KeyConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

/**
 * Gui handler for this mod. Mainly just takes an ID according to what was
 * passed to player.OpenGUI, and opens the corresponding GUI.
 *
 * @author MachineMuse
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        switch (ID) {
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        switch (ID) {
            case 0:
                Minecraft.getMinecraft().thePlayer.addStat(
                        AchievementList.openInventory, 1);
                return new GuiTinkerTable((EntityClientPlayerMP) player);
            case 1:
                return new KeyConfigGui(player);
            case 2:
                return new GuiFieldTinker((EntityClientPlayerMP) player);
            default:
                return null;
        }
    }
}
