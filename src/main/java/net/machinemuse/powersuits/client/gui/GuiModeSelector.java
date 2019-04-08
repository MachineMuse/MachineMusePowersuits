package net.machinemuse.powersuits.client.gui;

import net.machinemuse.numina.client.gui.MuseGui;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.minecraft.entity.player.EntityPlayer;

public class GuiModeSelector extends MuseGui {
    EntityPlayer player;
    RadialModeSelectionFrame radialSelect;

    public GuiModeSelector(EntityPlayer player) {
        this.player = player;
        this.xSize = Math.min(mc.mainWindow.getScaledWidth() - 50, 500);
        this.ySize = Math.min(mc.mainWindow.getScaledHeight() - 50, 300);
    }

    /**
     * Add the buttons (and other controls) to the screen.
     */
    @Override
    public void initGui() {
        super.initGui();
        radialSelect = new RadialModeSelectionFrame(
                new MusePoint2D(absX(-0.5F), absY(-0.5F)),
                new MusePoint2D(absX(0.5F), absY(0.5F)),
                player);
        frames.add(radialSelect);
    }

    @Override
    public void drawBackground() {

    }

    @Override
    public void update() {
        super.update();
        if (!mc.gameSettings.keyBindsHotbar[player.inventory.currentItem].isKeyDown()) {
            this.close();
        }
    }
}