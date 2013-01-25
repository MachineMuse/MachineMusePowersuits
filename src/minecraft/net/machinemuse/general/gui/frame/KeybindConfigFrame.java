package net.machinemuse.general.gui.frame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.general.gui.clickable.IClickable;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.minecraft.entity.player.EntityPlayer;

public class KeybindConfigFrame implements IGuiFrame {
	protected List<ClickableModule> modules;
	protected List<ClickableKeybinding> keybinds;
	protected IClickable selectedClickie;
	protected ClickableKeybinding closestKeybind;
	protected EntityPlayer player;
	protected MusePoint2D ul;
	protected MusePoint2D br;

	public KeybindConfigFrame(MusePoint2D ul, MusePoint2D br, EntityPlayer player) {
		modules = new ArrayList();
		keybinds = new ArrayList();
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
		keybinds.add(new ClickableKeybinding("B", new MusePoint2D(br.x() - 10, br.y() - 10)));
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
		}

	}

	@Override
	public void onMouseUp(double x, double y, int button) {
		if (button == 0) {
			if (selectedClickie != null && closestKeybind != null && selectedClickie instanceof ClickableModule) {
				closestKeybind.bindModule((ClickableModule) selectedClickie);
			}
			selectedClickie = null;
		}

	}

	@Override
	public void update(double mousex, double mousey) {
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
		for (ClickableModule module : modules) {
			if (module != selectedClickie) {
				repelOtherModules(module);
			}
		}
		for (ClickableModule module : modules) {
			clampModulePosition(module);
		}
	}

	private void clampModulePosition(ClickableModule module) {
		MusePoint2D position = module.getPosition();
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

	private void repelOtherModules(ClickableModule module) {
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
		for (ClickableModule module : modules) {
			module.draw();
		}
		for (ClickableKeybinding keybind : keybinds) {
			keybind.draw();
		}
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

}
