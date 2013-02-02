package net.machinemuse.general.gui.clickable;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableModule extends Clickable {
	protected IPowerModule module;

	/**
	 * @param vaug
	 */
	public ClickableModule(IPowerModule module2, MusePoint2D position) {
		super(position);
		this.setModule(module2);
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
				getModule().getIcon(null), null);

	}

	@Override
	public boolean hitBox(double x, double y) {
		boolean hitx = Math.abs(x - getPosition().x()) < 8;
		boolean hity = Math.abs(y - getPosition().y()) < 8;
		return hitx && hity;
	}

	public IPowerModule getModule() {
		return module;
	}

	public ClickableModule setModule(IPowerModule module2) {
		this.module = module2;
		return this;
	}

	public boolean equals(ClickableModule other) {
		if (this.module.equals(other.module)) {
			return true;
		}
		return false;
	}

}
