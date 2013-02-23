package net.machinemuse.general.gui;

import org.lwjgl.opengl.GL11;

import net.machinemuse.powersuits.container.PortableCraftingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class PortableCraftingGui extends GuiContainer {
	public PortableCraftingGui(EntityPlayer player, World world, int x, int y, int z) {
        super(new PortableCraftingContainer(player.inventory, world, x, y, z));
    }

    /**
* Draw the foreground layer for the GuiContainer (everything in front of
* the items)
*/
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
* Draw the background layer for the GuiContainer (everything behind the
* items)
*/
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

        // TODO Variable-ize this
        int var4 = this.mc.renderEngine.getTexture("/gui/crafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
