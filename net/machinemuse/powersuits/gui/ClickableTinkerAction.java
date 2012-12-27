package net.machinemuse.powersuits.gui;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.trash.ModuleUtils;
import net.machinemuse.powersuits.trash.PowerModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableTinkerAction extends Clickable {
	protected PowerModule module;
	protected NBTTagCompound moduleTag;

	/**
	 * @param vaug
	 */
	public ClickableTinkerAction(PowerModule module, Point2D position) {
		super(position);
		this.module = module;
		this.moduleTag = module.newModuleTag();
	}

	/**
	 * @param vaug
	 */
	public ClickableTinkerAction(NBTTagCompound moduleTag, Point2D position) {
		super(position);
		this.moduleTag = moduleTag;
		this.module = ModuleUtils.getModuleFromNBT(moduleTag);
	}

	@Override
	public List<String> getToolTip() {
		return module.getTooltip(
				Minecraft.getMinecraft().thePlayer, moduleTag);
	}

	@Override
	public void draw(RenderEngine engine, MuseGui gui) {
		int x = gui.absX(getPosition().x());
		int y = gui.absY(getPosition().y());

		Colour c1 = new Colour(1.0F, 0.2F, 0.6F, 1.0F);
		Colour c2 = new Colour(0.6F, 0.2F, 1.0F, 1.0F);

		MuseRenderer.drawModuleAt(x - 8, y - 8, gui, module, moduleTag, null);

	}

	@Override
	public boolean hitBox(int x, int y, MuseGui gui) {
		boolean hitx = Math.abs(x - gui.absX(getPosition().x())) < 8;
		boolean hity = Math.abs(y - gui.absY(getPosition().y())) < 8;
		return hitx && hity;
	}

}
