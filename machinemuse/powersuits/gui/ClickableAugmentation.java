package machinemuse.powersuits.gui;

import java.util.List;

import machinemuse.general.geometry.Colour;
import machinemuse.general.geometry.Doodler;
import machinemuse.general.geometry.Point2D;
import machinemuse.powersuits.augmentation.AugManager;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableAugmentation extends Clickable {
	protected NBTTagCompound aug;

	/**
	 * @param vaug
	 */
	public ClickableAugmentation(NBTTagCompound aug, Point2D position) {
		super(position);
		this.aug = aug;
	}

	@Override
	public List<String> getToolTip() {

		return AugManager.getTooltipFor(aug);
	}

	@Override
	public void draw(RenderEngine engine, MuseGui gui) {
		int x = gui.absX(getPosition().x());
		int y = gui.absY(getPosition().y());

		Colour c1 = new Colour(1.0F, 0.2F, 0.6F, 1.0F);
		Colour c2 = new Colour(0.6F, 0.2F, 1.0F, 1.0F);

		Doodler.drawGradientRect(x - 8, y - 8, x + 8, y + 8, c1, c2, 100.0f);

	}

	@Override
	public boolean hitBox(int x, int y, MuseGui gui) {
		boolean hitx = Math.abs(x - gui.absX(getPosition().x())) < 8;
		boolean hity = Math.abs(y - gui.absY(getPosition().y())) < 8;
		return hitx && hity;
	}

}
