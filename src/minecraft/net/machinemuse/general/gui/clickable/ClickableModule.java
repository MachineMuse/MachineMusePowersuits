package net.machinemuse.general.gui.clickable;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.general.MuseMathUtils;
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
	protected boolean allowed = true;
	protected boolean installed = false;

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
		double k = Integer.MAX_VALUE;
		double left = getPosition().x() - 8;
		double top = getPosition().y() - 8;
		drawPartial(left, top, left + 16, top + 16);
		// Colour.WHITE.doGL();
		// MuseRenderer.TEXTURE_MAP = getModule().getStitchedTexture(null);
		// MuseRenderer.drawIconAt(getPosition().x() - 8, getPosition().y() - 8,
		// getModule().getIcon(null), null);
		//
		// // Draw the status indicators slightly lower and to the right so they
		// // stand out a little more.
		// if (!allowed) {
		// String string = MuseStringUtils.wrapFormatTags("x",
		// MuseStringUtils.FormatCodes.DarkRed);
		// MuseRenderer.drawString(string, getPosition().x() + 3,
		// getPosition().y() + 1);
		// } else if (installed) {
		// MuseRenderer.drawCheckmark(getPosition().x() - 8 + 1,
		// getPosition().y() - 8 + 1, new Colour(0.0F, 0.667F, 0.0F, 1.0F));
		// }
	}

	public void drawPartial(double xmin, double ymin, double xmax, double ymax) {
		MuseRenderer.TEXTURE_MAP = getModule().getStitchedTexture(null);
		double left = getPosition().x() - 8;
		double top = getPosition().y() - 8;
		xmin = MuseMathUtils.clampDouble(xmin - left, 0, 16);
		ymin = MuseMathUtils.clampDouble(ymin - top, 0, 16);
		xmax = MuseMathUtils.clampDouble(xmax - left, 0, 16);
		ymax = MuseMathUtils.clampDouble(ymax - top, 0, 16);
		MuseRenderer.drawIconPartial(left, top, getModule().getIcon(null), Colour.WHITE, xmin, ymin, xmax, ymax);

		// Draw the status indicators slightly lower and to the right so they
		// stand out a little more.
		if (!allowed) {
			String string = MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.DarkRed);
			MuseRenderer.drawString(string, getPosition().x() + 3, getPosition().y() + 1);
		} else if (installed) {
			MuseRenderer.drawCheckmark(getPosition().x() - 8 + 1, getPosition().y() - 8 + 1, new Colour(0.0F, 0.667F, 0.0F, 1.0F));
		}
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
		return this.module.equals(other.module);
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

}
