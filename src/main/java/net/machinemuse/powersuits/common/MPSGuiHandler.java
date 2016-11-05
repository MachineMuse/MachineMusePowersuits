package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.general.gui.*;
import net.machinemuse.general.gui.frame.PortableCraftingContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

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
            return new PortableCraftingContainer(player.inventory, world, x, y, z);
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Minecraft.getMinecraft().thePlayer.addStat(AchievementList.openInventory, 1);
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
                return new PortableCraftingGui(player, world, x, y, z);
            default:
                return null;
        }
    }
}