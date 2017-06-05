package net.machinemuse.general.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.machinemuse.general.gui.frame.*;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiModeSelector extends MuseGui {
    EntityPlayer player;
    RadialSelectionFrame radialSelect;

    public GuiModeSelector(EntityPlayer player) {
        this.player = player;
        this.xSize = 256;
        this.ySize = 200;
    }

    /**
     * Add the buttons (and other controls) to the screen.
     */
    @Override
    public void initGui() {
        super.initGui();
        radialSelect = new RadialSelectionFrame(
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
        if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[player.inventory.currentItem].getKeyCode())) {
        	//close animation
        	//TODO
        } else {
        	//close Gui
        	try {
    			keyTyped('1',1);
    		} catch (IOException e){}
        }
    }
}