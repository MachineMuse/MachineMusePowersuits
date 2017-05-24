package net.machinemuse.general.gui.frame;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.geometry.SpiralPointToPoint2D;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

import java.util.*;

public class RadialSelectionFrame implements IGuiFrame {
    protected List<ClickableModule> modeButtons = new ArrayList<>();
    protected final long spawnTime;
    protected int selectedModule = -1;
    protected IRightClickModule prevSelection;
    protected ClickableItem lastItem;
    protected EntityPlayer player;
    protected MusePoint2D center;
    protected double radius;
    protected ItemStack stack;
    
    public RadialSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright, EntityPlayer player) {
    	spawnTime = System.currentTimeMillis();
        this.player = player;
        this.center = bottomright.plus(topleft).times(0.5);
        this.radius = Math.min(center.minus(topleft).x(), center.minus(topleft).y());
        this.stack = player.inventory.getCurrentItem();
        loadItems();
		if (stack != null && stack.getItem() instanceof IModeChangingItem) {
	        if (modeButtons != null) {
	            int i = 0;
	            for (ClickableModule mode : modeButtons) {
	            	if (mode.getModule().getDataName() == ((IModeChangingItem) stack.getItem()).getActiveMode(stack)) {
	                	selectedModule = i;
	                    prevSelection = (IRightClickModule) mode.getModule();
	                    break;
	                } else {
	                    i++;
	                }
	            }
	        }
        }

    }

    public boolean alreadyAdded(IRightClickModule module) {
        for (ClickableModule clickie : modeButtons) {
            if (clickie.getModule().getDataName().equals(module.getDataName())) {
                return true;
            }
        }
        return false;
    }

    private void loadItems() {
        if (player != null) {
    		ItemStack stack = null;
        	for (ItemStack itemStack : MuseItemUtils.getModularItemsInInventory(player)) {
        		if (itemStack.getItem() instanceof ItemPowerFist)
        			stack = itemStack;
        	}
            List<IRightClickModule> modes = new ArrayList<>();
            for (IRightClickModule module : ModuleManager.getRightClickModules()) {
                if (module.isValidForItem(stack))
                    if (ModuleManager.itemHasModule(stack, module.getDataName()))
                        modes.add(module);
            }

            int modeNum = 0;
            for (IRightClickModule module : modes) {
                if (!alreadyAdded(module)) {
                    ClickableModule clickie = new ClickableModule(module, new SpiralPointToPoint2D(center, radius, (3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / modes.size()), 250));
                    modeButtons.add(clickie);
                    modeNum++;
                }
            }
        }
    }

    @Override
    public void update(double mousex, double mousey) {
    	//Update Items
    	loadItems();
    	//Determine which mode to highlight from mouse position.
    	if (System.currentTimeMillis() - spawnTime > 250) {
        	selectModule(mousex, mousey);
    	}
		if (prevSelection != null && stack != null && stack.getItem() instanceof IModeChangingItem) {
	    	((IModeChangingItem) stack.getItem()).setActiveMode(stack, prevSelection.getDataName());
		}
    }

    @Override
    public void draw() {
    	//Draw the installed power fist modes
    	for (ClickableModule mode : modeButtons) {
            mode.draw();
        }
    	//Draw the selected mode indicator
    	drawSelection();
    	
/*        MusePoint2D center = ul.plus(br).times(0.5);
        RenderState.blendingOn();
        RenderState.on2D();
        if (selecting) {
            MuseRenderer.drawCenteredString(I18n.format("gui.pressKey"), center.x(), center.y());
            RenderState.off2D();
            RenderState.blendingOff();
            return;
        }
        newKeybindButton.draw();
        trashKeybindButton.draw();
        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        MuseRenderer.drawCenteredString(I18n.format("gui.keybindInstructions1"), center.x(), center.y() + 40);
        MuseRenderer.drawCenteredString(I18n.format("gui.keybindInstructions2"), center.x(), center.y() + 50);
        MuseRenderer.drawCenteredString(I18n.format("gui.keybindInstructions3"), center.x(), center.y() + 60);
        MuseRenderer.drawCenteredString(I18n.format("gui.keybindInstructions4"), center.x(), center.y() + 70);
        if (takenTime + 1000 > System.currentTimeMillis()) {
            MusePoint2D pos = newKeybindButton.getPosition().plus(new MusePoint2D(0, -20));
            MuseRenderer.drawCenteredString(I18n.format("gui.keybindTaken"), pos.x(), pos.y());
        }
        for (ClickableModule module : modules) {
            module.draw();
        }
        for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
            keybind.draw();
        }
        if (selectedClickie != null && closestKeybind != null) {
            MuseRenderer.drawLineBetween(selectedClickie, closestKeybind, Colour.YELLOW);
        }
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
*/
    }
/*
    private void drawItems() {
        for (ModuleSelectionSubFrame frame : categories.values()) {
            frame.drawPartial((int) (this.currentscrollpixels + border.top() + 4),
                    (int) (this.currentscrollpixels + border.top() + border.height() - 4));
        }
    }
*/
    private void drawSelection() {
        ClickableModule module = getSelectedModule();
        if (module != null) {
            MusePoint2D pos = modeButtons.get(selectedModule).getPosition();
//            if (pos.y() > this.currentscrollpixels + border.top() + 4 && pos.y() < this.currentscrollpixels + border.top() + border.height() - 4) {
                MuseRenderer.drawCircleAround(pos.x(), pos.y(), 10);
//            }
        }
    }

    public ClickableModule getSelectedModule() {
        if (modeButtons.size() > selectedModule && selectedModule != -1) {
            return modeButtons.get(selectedModule);
        } else {
            return null;
        }
    }

    private void selectModule(double x, double y) {
        if (modeButtons != null) {
            int i = 0;
            for (ClickableModule module : modeButtons) {
                if (module.hitBox(x, y)) {
                	selectedModule = i;
                    prevSelection = (IRightClickModule) module.getModule();
                    break;
                } else {
                    i++;
                }
            }
        }
    }
/*
    public void loadModules() {
        this.lastPosition = null;
        ClickableItem selectedItem = target.getSelectedItem();
        if (selectedItem != null) {
            moduleButtons = new ArrayList<>();
            categories = new HashMap<>();

            List<IPowerModule> workingModules = ModuleManager.getValidModulesForItem(selectedItem.getItem());

            // Prune the list of disallowed modules, if not installed on this item.
            for (Iterator<IPowerModule> it = workingModules.iterator(); it.hasNext(); ) {
                IPowerModule module = it.next();
                if (!module.isAllowed() &&
                        !ModuleManager.itemHasModule(selectedItem.getItem(), module.getDataName())) {
                    it.remove();
                }
            }

            if (workingModules.size() > 0) {
                this.selectedModule = -1;
                for (IPowerModule module : workingModules) {
                    ModuleSelectionSubFrame frame = getOrCreateCategory(module.getCategory());
                    ClickableModule moduleClickable = frame.addModule(module);
                    // Indicate installed modules
                    if (!module.isAllowed()) {
                        // If a disallowed module made it to the list, indicate
                        // it as disallowed
                        moduleClickable.setAllowed(false);
                    } else if (ModuleManager.itemHasModule(selectedItem.getItem(), module.getDataName())) {
                        moduleClickable.setInstalled(true);
                    }
                    if (moduleClickable.getModule().equals(this.prevSelection)) {
                        this.selectedModule = moduleButtons.size();
                    }
                    moduleButtons.add(moduleClickable);
                }
            }
            for (ModuleSelectionSubFrame frame : categories.values()) {
                frame.refreshButtonPositions();
            }
        }
    }

    private ModuleSelectionSubFrame getOrCreateCategory(String category) {
        if (categories.containsKey(category)) {
            return categories.get(category);
        } else {
            MuseRelativeRect position = new MuseRelativeRect(
                    border.left() + 4,
                    border.top() + 4,
                    border.right() - 4,
                    border.top() + 32);
            position.setBelow(lastPosition);
            lastPosition = position;
            ModuleSelectionSubFrame frame = new ModuleSelectionSubFrame(
                    category,
                    position);

            categories.put(category, frame);
            return frame;
        }
    }
*/
    @Override
    public void onMouseDown(double x, double y, int button) {
/*        super.onMouseDown(x, y, button);
        if (border.left() < x && border.right() > x && border.top() < y && border.bottom() > y) {
            y += currentscrollpixels;
            // loadModules();
            int i = 0;
            for (ClickableModule module : moduleButtons) {
                if (module.hitBox(x, y)) {
                    Musique.playClientSound(SoundDictionary.SOUND_EVENT_GUI_SELECT, SoundCategory.BLOCKS,1, null);
                    selectedModule = i;
                    prevSelection = module.getModule();
                    break;
                } else {
                    i++;
                }
            }
        }*/
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    	
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
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

	//θ = 3π/2 - 2π(modeNum/totalModes) + π(1-(frame/numFrames))
	private double getTheta(int modeNum, int totalModes, double frame) {
	    return ((3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / totalModes) + (Math.PI * ((1 - (frame / numFrames))));
	}

	private void drawRadialSelector(EntityPlayer player) {
    	boolean keyPressed = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName()).hotbarKey;
    	
    	GuiScreen screen = Minecraft.getMinecraft().currentScreen;
    	if (screen != null) {
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

    	    	if (keyPressed) {
    		    	if (keyEventTime == 0) {
    		    		keyEventTime = System.currentTimeMillis();
    		    	} else {
    			    	frame = System.currentTimeMillis() - keyEventTime;
    			    	if (frame > numFrames) {
    			    		frame = numFrames;
    			    		keyEventTime = -1;
    			    	}
    		    	}
    	    	} else {
    	    		if (keyEventTime == -1) {
    	    			keyEventTime = System.currentTimeMillis();
    	    		} else {
    	    			frame = numFrames - (System.currentTimeMillis() - keyEventTime);
    		    		if (frame <= 0) {
    		    			frame = 0;
    		    			keyEventTime = 0;
    		    		}
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
    	    	}
            }
    	}
	}
*/
