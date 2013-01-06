package net.machinemuse.general.gui.clickable;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.powermodule.GenericModule;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableModule extends Clickable {
	private GenericModule module;

	/**
	 * @param vaug
	 */
	public ClickableModule(GenericModule module, Point2D position) {
		super(position);
		this.setModule(module);
	}

	@Override
	public List<String> getToolTip() {
		List<String> toolTipText = new ArrayList();
		toolTipText.add(getModule().getName());
		toolTipText.addAll(MuseStringUtils.wrapStringToLength(
				getModule().getDescription(), 30));

		return toolTipText;
	}

	@Override
	public void draw() {
		Colour c1 = new Colour(1.0F, 0.2F, 0.6F, 1.0F);
		Colour c2 = new Colour(0.6F, 0.2F, 1.0F, 1.0F);

		Colour.getGreyscale(1.0f, 1.0f).doGL();
		MuseRenderer.drawIconAt(getPosition().x() - 8, getPosition().y() - 8,
				getModule().getIcon(), null);

	}

	@Override
	public boolean hitBox(double x, double y) {
		boolean hitx = Math.abs(x - getPosition().x()) < 8;
		boolean hity = Math.abs(y - getPosition().y()) < 8;
		return hitx && hity;
	}

	public GenericModule getModule() {
		return module;
	}

	public ClickableModule setModule(GenericModule module) {
		this.module = module;
		return this;
	}

}
