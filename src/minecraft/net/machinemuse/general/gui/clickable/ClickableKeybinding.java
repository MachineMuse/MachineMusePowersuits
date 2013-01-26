package net.machinemuse.general.gui.clickable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRenderer;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class ClickableKeybinding extends Clickable {
	protected List<ClickableModule> boundModules;
	protected KeyBinding keybind;

	public ClickableKeybinding(KeyBinding keybind, MusePoint2D position) {
		this.keybind = keybind;
		this.position = position;
		this.boundModules = new ArrayList();
	}

	@Override
	public void draw() {
		MuseRenderer.drawCircleAround(position.x(), position.y(), 8);
		String name = keybind.keyCode < 0 ? "Mouse" + (keybind.keyCode + 100) : Keyboard.getKeyName(keybind.keyCode);
		MuseRenderer.drawCenteredString("" + name, position.x(), position.y() - 4);
		for (ClickableModule module : boundModules) {
			MuseRenderer.drawLineBetween(this, module, Colour.LIGHTBLUE);
		}
	}

	@Override
	public boolean hitBox(double x, double y) {
		return position.minus(new MusePoint2D(x, y)).distance() < 8;
	}

	@Override
	public List<String> getToolTip() {
		return null;
	}

	public KeyBinding getKeyBinding() {
		return keybind;
	}

	public void bindModule(ClickableModule module) {
		boundModules.add(module);
	}

	public void unbindModule(ClickableModule module) {
		boundModules.remove(module);
	}

	public void unbindFarModules() {
		Iterator<ClickableModule> iterator = boundModules.iterator();
		ClickableModule module;
		while (iterator.hasNext()) {
			module = iterator.next();
			int maxDistance = getTargetDistance() * 2;
			double distanceSq = module.getPosition().distanceSq(this.getPosition());
			if (distanceSq > maxDistance * maxDistance) {
				iterator.remove();
			}
		}
	}

	public int getTargetDistance() {
		int targetDistance = 16;
		if (boundModules.size() > 6) {
			targetDistance += (boundModules.size() - 6) * 3;
		}
		return targetDistance;
	}

	public void attractBoundModules(IClickable exception) {
		for (ClickableModule module : boundModules) {
			if (module != exception) {
				MusePoint2D euclideanDistance = module.getPosition().minus(this.getPosition());
				MusePoint2D directionVector = euclideanDistance.normalize();
				MusePoint2D tangentTarget = directionVector.times(getTargetDistance()).plus(this.getPosition());
				MusePoint2D midpointTangent = module.getPosition().midpoint(tangentTarget);
				module.move(midpointTangent.x(), midpointTangent.y());
			}
		}
	}
}
