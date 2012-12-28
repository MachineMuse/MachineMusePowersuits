package net.machinemuse.powersuits.gui;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.item.TinkerAction;
import net.machinemuse.powersuits.item.TinkerEffect;
import net.minecraft.client.renderer.RenderEngine;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 * 
 * @author MachineMuse
 */
public class ClickableTinkerAction extends Clickable {
	protected TinkerAction action;

	/**
	 * @param vaug
	 */
	public ClickableTinkerAction(TinkerAction action, Point2D position) {
		super(position);
		this.action = action;
	}

	public List<String> wrapString(String str, int length) {
		List<String> strlist = new ArrayList();

		int i = 0;
		while (i + length < str.length()) {
			int j = str.lastIndexOf(" ", i + length);
			if (j == -1) {
				j = str.indexOf(" ", i + length);
			}
			if (j == -1) {
				break;
			}
			strlist.add(str.substring(i, j));
			i = j;
		}
		strlist.add(str.substring(i));

		return strlist;
	}

	@Override
	public List<String> getToolTip() {
		List<String> toolTipText = new ArrayList();
		toolTipText.add(action.name);
		toolTipText.addAll(wrapString(action.description, 30));
		for (TinkerEffect effect : action.getEffects()) {
			toolTipText.add(effect.toString());
		}

		return toolTipText;
	}

	@Override
	public void draw(RenderEngine engine, MuseGui gui) {
		int x = gui.absX(getPosition().x());
		int y = gui.absY(getPosition().y());

		Colour c1 = new Colour(1.0F, 0.2F, 0.6F, 1.0F);
		Colour c2 = new Colour(0.6F, 0.2F, 1.0F, 1.0F);

		Colour.getGreyscale(1.0f, 1.0f).doGL();
		MuseRenderer.drawIconAt(x - 8, y - 8, gui, action.getIcon(), null);

	}

	@Override
	public boolean hitBox(int x, int y, MuseGui gui) {
		boolean hitx = Math.abs(x - gui.absX(getPosition().x())) < 8;
		boolean hity = Math.abs(y - gui.absY(getPosition().y())) < 8;
		return hitx && hity;
	}

}
