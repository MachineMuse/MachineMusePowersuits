package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.clickable.ClickableSlider;
import net.machinemuse.powersuits.tinker.TinkerProperty;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleTweakFrame extends ScrollableFrame {
	protected ItemSelectionFrame itemTarget;
	protected ModuleSelectionFrame moduleTarget;
	protected List<ClickableSlider> sliders;
	protected List<TinkerProperty> properties;
	
	public ModuleTweakFrame(
			EntityPlayer player,
			Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour,
			ItemSelectionFrame itemTarget, ModuleSelectionFrame moduleTarget) {
		super(topleft, bottomright, borderColour, insideColour);
		this.itemTarget = itemTarget;
		this.moduleTarget = moduleTarget;
		this.sliders = new ArrayList();
	}
	
	@Override public void draw() {
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 4);
	}
	
}
