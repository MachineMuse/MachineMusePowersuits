package net.machinemuse.general.gui;

import net.machinemuse.general.gui.frame.PortableCraftingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class PortableCraftingGui extends GuiContainer {
    public PortableCraftingGui(EntityPlayer player, World world, BlockPos pos) {
        super(new PortableCraftingContainer(player.inventory, world, pos));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the item)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        /*this.fontRenderer.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);*/
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the item)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_Loc);
        int var5 = (width - xSize) / 2;
        int var6 = (height - ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public static final ResourceLocation GUI_Loc =
            new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
}