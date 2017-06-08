package net.machinemuse.general.gui.frame;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.geometry.SpiralPointToPoint2D;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RadialSelectionFrame implements IGuiFrame {
    protected List<ClickableModule> modeButtons = new ArrayList<>();
    protected final long spawnTime;
    protected int selectedModule = -1;
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
        //Determine which mode is currently active
		if (stack != null && stack.getItem() instanceof IModeChangingItem) {
	        if (modeButtons != null) {
	            int i = 0;
	            for (ClickableModule mode : modeButtons) {
	            	if (mode.getModule().getDataName().equals(((IModeChangingItem) stack.getItem()).getActiveMode(stack))) {
	                	selectedModule = i;
	                    break;
	                }
                    i++;
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

    private void selectModule(double x, double y) {
        if (modeButtons != null) {
            int i = 0;
            for (ClickableModule module : modeButtons) {
                if (module.hitBox(x, y)) {
                	selectedModule = i;
                    break;
                }
                i++;
            }
        }
    }

    public ClickableModule getSelectedModule() {
        if (modeButtons.size() > selectedModule && selectedModule != -1) {
            return modeButtons.get(selectedModule);
        } else {
            return null;
        }
    }

    @Override
    public void update(double mousex, double mousey) {
    	//Update items
    	loadItems();
    	//Determine which mode is selected
    	if (System.currentTimeMillis() - spawnTime > 250) {
        	selectModule(mousex, mousey);
    	}
    	//Switch to selected mode
		if (getSelectedModule() != null && stack != null && stack.getItem() instanceof IModeChangingItem) {
	    	((IModeChangingItem) stack.getItem()).setActiveMode(stack, getSelectedModule().getModule().getDataName());
	    	PacketSender.sendToServer(new MusePacketModeChangeRequest(player, getSelectedModule().getModule().getDataName(), player.inventory.currentItem));
		}
    }

    private void drawSelection() {
        ClickableModule module = getSelectedModule();
        if (module != null) {
            MusePoint2D pos = module.getPosition();
                MuseRenderer.drawCircleAround(pos.x(), pos.y(), 10);
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
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
    	
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    	
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
