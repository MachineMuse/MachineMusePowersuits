package net.machinemuse.general.gui.clickable;

import java.util.List;

import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;

public class ClickableLabel implements IClickable {
	protected String label;
	protected Point2D position;

	public ClickableLabel(String label, Point2D position) {
		this.label = label;
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * machinemuse.powersuits.gui.Clickable#draw(net.minecraft.client.renderer
	 * .RenderEngine, machinemuse.powersuits.gui.MuseGui)
	 */
	@Override
	public void draw() {
		MuseRenderer.drawCenteredString(this.label, position.x(),
				position.y() - 4);
	}

	@Override
	public boolean hitBox(int x, int y) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see machinemuse.powersuits.gui.Clickable#getToolTip()
	 */
	@Override
	public List<String> getToolTip() {
		return null;
	}

}
