package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.general.gui.clickable.ClickableButton;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.general.gui.clickable.IClickable;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

public class KeybindConfigFrame implements IGuiFrame {
	protected List<ClickableModule> modules;
	protected List<ClickableKeybinding> keybinds;
	protected IClickable selectedClickie;
	protected ClickableKeybinding closestKeybind;
	protected EntityPlayer player;
	protected MusePoint2D ul;
	protected MusePoint2D br;
	protected MuseGui gui;
	protected boolean selecting;
	protected ClickableButton newKeybindButton;
	protected ClickableButton trashKeybindButton;
	protected long takenTime;

	public KeybindConfigFrame(MuseGui gui, MusePoint2D ul, MusePoint2D br, EntityPlayer player) {
		modules = new ArrayList();
		keybinds = new ArrayList();
		this.gui = gui;
		this.ul = ul;
		this.br = br;
		List<PowerModule> installedModules = ItemUtils.getPlayerInstalledModules(player);
		List<MusePoint2D> points = MuseRenderer.pointsInLine(
				installedModules.size(),
				new MusePoint2D(ul.x() + 10, ul.y() + 10),
				new MusePoint2D(ul.x() + 10, br.y() - 10));
		Iterator<MusePoint2D> pointIterator = points.iterator();
		for (PowerModule module : installedModules) {
			if (module.isToggleable()) {
				modules.add(new ClickableModule(module, pointIterator.next()));
			}
		}
		int offset = 1;
		// for (Object keybind : KeyBinding.keybindArray) {
		// if (((KeyBinding) keybind).keyCode > 0) {
		// keybinds.add(new ClickableKeybinding((KeyBinding) keybind, new
		// MusePoint2D(br.x() - 10, br.y() - (offset++ * 10))));
		// }
		// }
		MusePoint2D center = br.plus(ul).times(0.5);
		newKeybindButton = new ClickableButton("New", center.plus(new MusePoint2D(0, -8)), true);
		trashKeybindButton = new ClickableButton("Trash", center.plus(new MusePoint2D(0, 8)), true);
	}

	@Override
	public void onMouseDown(double x, double y, int button) {
		if (button == 0) {
			if (selectedClickie == null) {
				for (ClickableModule module : modules) {
					if (module.hitBox(x, y)) {
						selectedClickie = module;
						return;
					}
				}
				for (ClickableKeybinding keybind : keybinds) {
					if (keybind.hitBox(x, y)) {
						selectedClickie = keybind;
						return;
					}
				}
			}
			if (newKeybindButton.hitBox(x, y)) {
				selecting = true;
			}
		}
	}

	@Override
	public void onMouseUp(double x, double y, int button) {
		if (button == 0) {
			if (selectedClickie != null && closestKeybind != null && selectedClickie instanceof ClickableModule) {
				closestKeybind.bindModule((ClickableModule) selectedClickie);
			} else if (selectedClickie != null && selectedClickie instanceof ClickableKeybinding && trashKeybindButton.hitBox(x, y)) {
				KeyBinding binding = ((ClickableKeybinding) selectedClickie).getKeyBinding();
				KeyBinding.keybindArray.remove(binding);
				KeyBinding.hash.removeObject(binding.keyCode);

			}
			selectedClickie = null;
		}

	}

	@Override
	public void update(double mousex, double mousey) {
		if (selecting) {
			return;
		}
		this.closestKeybind = null;
		double closestDistance = Double.MAX_VALUE;
		if (this.selectedClickie != null) {
			this.selectedClickie.move(mousex, mousey);
			if (this.selectedClickie instanceof ClickableModule) {
				ClickableModule selectedModule = ((ClickableModule) this.selectedClickie);
				for (ClickableKeybinding keybind : this.keybinds) {
					double distance = keybind.getPosition().minus(selectedModule.getPosition()).distance();
					if (distance < closestDistance) {
						closestDistance = distance;
						if (closestDistance < 32) {
							this.closestKeybind = keybind;
						}
					}
				}
			}
		}
		for (ClickableKeybinding keybind : this.keybinds) {
			if (keybind != selectedClickie) {
				keybind.unbindFarModules();
			}
			keybind.attractBoundModules(selectedClickie);
		}
		for (IClickable module : modules) {
			if (module != selectedClickie) {
				repelOtherModules(module);
			}
		}
		for (IClickable keybind : keybinds) {
			if (keybind != selectedClickie) {
				repelOtherModules(keybind);
			}
		}
		for (IClickable module : modules) {
			clampClickiePosition(module);
		}
		for (IClickable keybind : keybinds) {
			clampClickiePosition(keybind);
		}
	}

	private void clampClickiePosition(IClickable clickie) {
		MusePoint2D position = clickie.getPosition();
		position.setX(clampDouble(position.x(), ul.x(), br.x()));
		position.setY(clampDouble(position.y(), ul.y(), br.y()));
	}

	private double clampDouble(double x, double lower, double upper) {
		if (x < lower) {
			return lower;
		} else if (x > upper) {
			return upper;
		} else {
			return x;
		}
	}

	private void repelOtherModules(IClickable module) {
		MusePoint2D modulePosition = module.getPosition();
		for (ClickableModule otherModule : modules) {
			if (otherModule != selectedClickie && otherModule != module && otherModule.getPosition().distanceTo(modulePosition) < 16) {
				MusePoint2D euclideanDistance = otherModule.getPosition().minus(module.getPosition());
				MusePoint2D directionVector = euclideanDistance.normalize();
				MusePoint2D tangentTarget = directionVector.times(16).plus(module.getPosition());
				MusePoint2D midpointTangent = otherModule.getPosition().midpoint(tangentTarget);
				if (midpointTangent.distanceTo(module.getPosition()) > 2) {
					otherModule.move(midpointTangent.x(), midpointTangent.y());
				}
				// Point2D away = directionVector.times(0).plus(modulePosition);
				// module.move(away.x(), away.y());
			}
		}
	}

	@Override
	public void draw() {
		if (selecting) {
			MusePoint2D pos = ul.plus(br).times(0.5);
			MuseRenderer.drawCenteredString("Press Key", pos.x(), pos.y());
			return;
		}
		if (takenTime + 1000 > System.currentTimeMillis()) {
			MusePoint2D pos = newKeybindButton.getPosition().plus(new MusePoint2D(0, -20));
			MuseRenderer.drawCenteredString("Taken!", pos.x(), pos.y());
		}
		for (ClickableModule module : modules) {
			module.draw();
		}
		for (ClickableKeybinding keybind : keybinds) {
			keybind.draw();
		}
		newKeybindButton.draw();
		trashKeybindButton.draw();
		if (selectedClickie != null && closestKeybind != null) {
			MuseRenderer.drawLineBetween(selectedClickie, closestKeybind, Colour.YELLOW);
		}
	}

	@Override
	public List<String> getToolTip(int x, int y) {
		if (Config.doAdditionalInfo()) {
			for (ClickableModule module : modules) {
				if (module.hitBox(x, y)) {
					return module.getToolTip();
				}
			}
		}
		return null;
	}

	public void handleKeyboard() {
		if (selecting) {
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				if (!KeyBinding.hash.containsItem(key)) {
					KeyBinding keybind = new KeyBinding(Keyboard.getKeyName(key), key);
					ClickableKeybinding clickie = new ClickableKeybinding(keybind, newKeybindButton.getPosition().plus(new MusePoint2D(0, -20)));
					keybinds.add(clickie);
				} else {
					takenTime = System.currentTimeMillis();
				}
				selecting = false;
			}
		}
	}

}
