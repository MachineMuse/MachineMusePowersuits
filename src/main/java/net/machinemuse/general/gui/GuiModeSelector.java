package net.machinemuse.general.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.machinemuse.general.gui.frame.*;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
//        this.drawDefaultBackground(); // Shading on the world view
    }

    @Override
    public void update() {
        super.update();
        Minecraft mc = Minecraft.getMinecraft();
        if (!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[player.inventory.currentItem].getKeyCode())) {
        	//switch to selected mode
        	
        	//close animation
        	
        	//close Gui
        	try {
    			keyTyped('1',1);
    		} catch (IOException e){
    			System.out.printf("Radial Selection GUI exited.");
    		}
        }
    }
}

/*

private static long keyEventTime =  0L;
private static final int numFrames = 250; //250ms = 1/4 second

//x = r × cos(θ)
private double getXfromPolar(double radius, double theta) {
	return radius * Math.cos(theta); 
}

//y = r × sin(θ)
private double getYfromPolar(double radius, double theta) {
	return radius * Math.sin(theta); 
}

//θ = 3π/2 - 2π(modeNum/totalModes) + 2π(1-(frame/numFrames))

private double getTheta(int modeNum, int totalModes, double frame) {
    return ((3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / totalModes) + (2 * Math.PI * ((1 - (frame / numFrames))));
}

private void drawRadialSelector(EntityPlayer player) {

	ItemStack stack = player.inventory.getCurrentItem();
    if (stack != null && stack.getItem() instanceof IModeChangingItem) {
        IModeChangingItem item = (IModeChangingItem) stack.getItem();
   		List<String> modes = ((IModeChangingItem) item).getValidModes(stack);
    	int center_w = screen.width / 2;
    	int center_h = screen.height / 2;
    	int radius = Math.min(center_w, center_h) / 2 - 8;
    	double frame = 0;
    	double theta, x, y, percent;
    	TextureAtlasSprite currentMode;

    	if (keyEventTime == 0) {
   			keyEventTime = System.currentTimeMillis();
   		} else {
   			frame = numFrames - (System.currentTimeMillis() - keyEventTime);
    		if (frame <= 0) {
    			frame = 0;
    			keyEventTime = 0;
    		}
   		}
    	if (frame != 0) {
	    	MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
	    	RenderState.blendingOn();
    		for (int i = 0; i < modes.size(); i++) {
	    		currentMode  = item.getModeIcon(modes.get(i), stack, player);
    			theta = getTheta(i, modes.size(), frame);
    			percent =  frame / numFrames;
	    		x = center_w - 8 + getXfromPolar(radius * percent, theta);
	    		y = center_h - 8 + getYfromPolar(radius * percent, theta);
	    		MuseIconUtils.drawIconAt(x, y, currentMode, Colour.WHITE.withAlpha(percent));
	    	}
	    	RenderState.blendingOff();
	    	MuseTextureUtils.popTexture();
	    	Colour.WHITE.doGL();
        } else {
        	try {
    			keyTyped('1',1);
    		} catch (IOException e){
    			System.out.printf("Radial Selection GUI exited.");
    		}
        }
    }
}
*/