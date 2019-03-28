package net.machinemuse.powersuits.gui.tinker;

import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.gui.MuseGui;
import net.machinemuse.powersuits.gui.RadialModeSelectionFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiModeSelector extends MuseGui {
    EntityPlayer player;
    RadialModeSelectionFrame radialSelect;

    public GuiModeSelector(EntityPlayer player) {
        this.player = player;
        ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft());
        this.xSize = Math.min(screen.getScaledWidth() - 50, 500);
        this.ySize = Math.min(screen.getScaledHeight() - 50, 300);
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
        if (!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[player.inventory.currentItem].getKeyCode())) {
            //close animation
            //TODO
            //close Gui
            try {
                keyTyped('1', 1);
            } catch (IOException e) {
            }
        }
    }
}