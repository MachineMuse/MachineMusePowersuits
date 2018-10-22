package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.gui.PortableCraftingContainer;
import net.machinemuse.powersuits.gui.PortableCraftingGui;
import net.machinemuse.powersuits.gui.ScannerContainer;
import net.machinemuse.powersuits.gui.ScannerGUI;
import net.machinemuse.powersuits.gui.tinker.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Gui handler for this mod. Mainly just takes an ID according to what was
 * passed to player.OpenGUI, and opens the corresponding GUI.
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 11/3/16.
 */
public enum MPSGuiHandler implements IGuiHandler {
    INSTANCE;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 4)
            return new PortableCraftingContainer(player.inventory, world, new BlockPos(x, y, z));
        if (ID == 6) {
            return new ScannerContainer(player, getPlayerHand(player));
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        //        Minecraft.getMinecraft().player.addStat(AchievementList.OPEN_INVENTORY, 1);
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
                return new PortableCraftingGui(player, world, new BlockPos(x, y, z));
            case 5:
                return new GuiModeSelector(player);
            case 6:
                return new ScannerGUI(new ScannerContainer(player, getPlayerHand(player)));
            default:
                return null;
        }
    }

    @Nonnull
    EnumHand getPlayerHand(EntityPlayer player) {
        EnumHand hand;
        hand = player.getActiveHand();
        if (hand == null) {
            ItemStack held = player.getHeldItemMainhand();
            if (!held.isEmpty() && held.getItem() instanceof ItemPowerFist)
                return EnumHand.MAIN_HAND;
            else
                return EnumHand.OFF_HAND;
        }
        return hand;
    }
}
