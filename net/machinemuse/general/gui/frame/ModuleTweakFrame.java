package net.machinemuse.general.gui.frame;

import java.util.*;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableSlider;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.MusePacketTweakRequest;
import net.machinemuse.powersuits.powermodule.GenericModule;
import net.machinemuse.powersuits.powermodule.IModuleProperty;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.Player;

public class ModuleTweakFrame extends ScrollableFrame {
	protected static double SCALERATIO = 0.5;
	protected ItemSelectionFrame itemTarget;
	protected ModuleSelectionFrame moduleTarget;
	protected List<ClickableSlider> sliders;
	protected Map<String, Double> propertyStrings;
	protected ClickableSlider selectedSlider;
	protected EntityClientPlayerMP player;
	
	public ModuleTweakFrame(
			EntityClientPlayerMP player,
			Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour,
			ItemSelectionFrame itemTarget, ModuleSelectionFrame moduleTarget) {
		super(topleft.times(1 / SCALERATIO), bottomright.times(1 / SCALERATIO), borderColour, insideColour);
		this.itemTarget = itemTarget;
		this.moduleTarget = moduleTarget;
		this.player = player;
	}
	
	@Override public void update(double mousex, double mousey) {
		mousex /= SCALERATIO;
		mousey /= SCALERATIO;
		if (itemTarget.getSelectedItem() != null && moduleTarget.getSelectedModule() != null) {
			ItemStack stack = itemTarget.getSelectedItem().getItem();
			GenericModule module = moduleTarget.getSelectedModule().getModule();
			if (ItemUtils.itemHasModule(itemTarget.getSelectedItem().getItem(), moduleTarget.getSelectedModule().getModule().getName())) {
				loadTweaks(stack, module);
			} else {
				sliders = null;
				propertyStrings = null;
			}
		}
		if (selectedSlider != null) {
			selectedSlider.moveSlider(mousex, mousey);
		}
		
	}
	
	@Override public void draw() {
		if (sliders != null) {
			MuseRenderer.drawFrameRect(topleft.times(SCALERATIO), bottomright.times(SCALERATIO), borderColour, insideColour, 0, 2);
			MuseRenderer.drawCenteredString("Tinker", (topleft.x() + bottomright.x()) / 4, topleft.y() / 2 + 2);
			GL11.glPushMatrix();
			GL11.glScaled(SCALERATIO, SCALERATIO, SCALERATIO);
			for (ClickableSlider slider : sliders) {
				slider.draw();
			}
			int nexty = (int) (sliders.size() * 24 + topleft.y() + 24);
			GenericModule module = moduleTarget.getSelectedModule().getModule();
			for (Map.Entry<String, Double> property : propertyStrings.entrySet()) {
				nexty += 8;
				String[] str = { property.getKey() + ":", MuseStringUtils.formatNumberShort(property.getValue()) };
				MuseRenderer.drawStringsJustified(Arrays.asList(str), topleft.x() + 4, bottomright.x() - 4, nexty);
				
			}
			GL11.glPopMatrix();
		}
	}
	private void loadTweaks(ItemStack stack, GenericModule module) {
		NBTTagCompound itemTag = ItemUtils.getMuseItemTag(stack);
		NBTTagCompound moduleTag = itemTag.getCompoundTag(module.getName());
		
		propertyStrings = new HashMap();
		
		Set<IModuleProperty> propertyCalcs = module.getPropertyComputers();
		for (IModuleProperty property : propertyCalcs) {
			double currValue = 0;
			if (propertyStrings.containsKey(property.getName())) {
				currValue = propertyStrings.get(property.getName());
			}
			currValue += property.computeProperty(moduleTag);
			propertyStrings.put(property.getName(), currValue);
		}
		
		Set<String> tweaks = module.getTweaks();
		sliders = new LinkedList();
		int y = 0;
		for (String tweak : tweaks) {
			y += 24;
			ClickableSlider slider = new ClickableSlider(
					new Point2D((topleft.x() + bottomright.x()) / 2, topleft.y() + y),
					bottomright.x() - topleft.x() - 8,
					moduleTag, tweak);
			sliders.add(slider);
		}
	}
	@Override public void onMouseDown(double x, double y, int button) {
		x /= SCALERATIO;
		y /= SCALERATIO;
		if (button == 0) {
			if (sliders != null) {
				for (ClickableSlider slider : sliders) {
					if (slider.hitBox(x, y)) {
						selectedSlider = slider;
					}
				}
			}
		}
	}
	
	@Override public void onMouseUp(double x, double y, int button) {
		if (selectedSlider != null && itemTarget.getSelectedItem() != null && moduleTarget.getSelectedModule() != null) {
			ClickableItem item = itemTarget.getSelectedItem();
			GenericModule module = moduleTarget.getSelectedModule().getModule();
			MusePacket tweakRequest = new MusePacketTweakRequest((Player) player, item.inventorySlot, module.getName(), selectedSlider.getName(),
					selectedSlider.getValue());
			player.sendQueue.addToSendQueue(tweakRequest.getPacket250());
		}
		if (button == 0) {
			selectedSlider = null;
		}
	}
}
