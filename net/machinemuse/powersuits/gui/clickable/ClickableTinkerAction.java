package net.machinemuse.powersuits.gui.clickable;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.tinker.TinkerAction;
import net.machinemuse.powersuits.tinker.TinkerEffect;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableTinkerAction extends Clickable {
	private TinkerAction action;

	/**
	 * @param vaug
	 */
	public ClickableTinkerAction(TinkerAction action, Point2D position) {
		super(position);
		this.setAction(action);
	}

	@Override
	public List<String> getToolTip() {
		List<String> toolTipText = new ArrayList();
		toolTipText.add(getAction().name);
		toolTipText.addAll(MuseStringUtils.wrapStringToLength(
				getAction().description, 30));
		for (TinkerEffect effect : getAction().getEffects()) {
			toolTipText.add(effect.toString());
		}

		return toolTipText;
	}

	@Override
	public void draw() {
		Colour c1 = new Colour(1.0F, 0.2F, 0.6F, 1.0F);
		Colour c2 = new Colour(0.6F, 0.2F, 1.0F, 1.0F);

		Colour.getGreyscale(1.0f, 1.0f).doGL();
		MuseRenderer.drawIconAt(getPosition().x() - 8, getPosition().y() - 8,
				getAction().getIcon(), null);

	}

	@Override
	public boolean hitBox(int x, int y) {
		boolean hitx = Math.abs(x - getPosition().x()) < 8;
		boolean hity = Math.abs(y - getPosition().y()) < 8;
		return hitx && hity;
	}

	public TinkerAction getAction() {
		return action;
	}

	public ClickableTinkerAction setAction(TinkerAction action) {
		this.action = action;
		return this;
	}

}
