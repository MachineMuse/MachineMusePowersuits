package net.machinemuse.general.gui.frame;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.general.gui.clickable.ClickableButton;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.general.gui.clickable.IClickable;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.GradientAndArcCalculator;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class KeybindConfigFrame implements IGuiFrame {
    protected Set<ClickableModule> modules;
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
        modules = new HashSet();
        for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
            modules.addAll(kb.getBoundModules());
        }
        this.gui = gui;
        this.ul = ul;
        this.br = br;
        this.player = player;
        MusePoint2D center = br.plus(ul).times(0.5);
        newKeybindButton = new ClickableButton(StatCollector.translateToLocal("gui.newKeybind"), center.plus(new MusePoint2D(0, -8)), true);
        trashKeybindButton = new ClickableButton(StatCollector.translateToLocal("gui.trashKeybind"), center.plus(new MusePoint2D(0, 8)), true);
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
                for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
                    if (keybind.hitBox(x, y)) {
                        selectedClickie = keybind;
                        return;
                    }
                }
            }
            if (newKeybindButton.hitBox(x, y)) {
                selecting = true;
            }
        } else if(button == 1) {
            for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
                if (keybind.hitBox(x, y)) {
                    keybind.toggleHUDState();
                    return;
                }
            }
        } else if (button > 2) {
            int key = button - 100;
            if (KeyBinding.hash.containsItem(key)) {
                takenTime = System.currentTimeMillis();
            }
            if (!KeyBinding.hash.containsItem(key)) {
                addKeybind(key, true);
            } else if (Config.allowConflictingKeybinds()) {
                addKeybind(key, false);
            }
            selecting = false;
        }
    }

    public void refreshModules() {
        List<IPowerModule> installedModules = MuseItemUtils.getPlayerInstalledModules(player);
        List<MusePoint2D> points = GradientAndArcCalculator.pointsInLine(
                installedModules.size(),
                new MusePoint2D(ul.x() + 10, ul.y() + 10),
                new MusePoint2D(ul.x() + 10, br.y() - 10));
        Iterator<MusePoint2D> pointIterator = points.iterator();
        for (IPowerModule module : installedModules) {
            if (module instanceof IToggleableModule && !alreadyAdded(module)) {
                ClickableModule clickie = new ClickableModule(module, pointIterator.next());
                modules.add(clickie);
            }
        }
    }

    public boolean alreadyAdded(IPowerModule module) {
        for (ClickableModule clickie : modules) {
            if (clickie.getModule().getDataName().equals(module.getDataName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
        if (button == 0) {
            if (selectedClickie != null && closestKeybind != null && selectedClickie instanceof ClickableModule) {
                closestKeybind.bindModule((ClickableModule) selectedClickie);
            } else if (selectedClickie != null && selectedClickie instanceof ClickableKeybinding && trashKeybindButton.hitBox(x, y)) {
                KeyBinding binding = ((ClickableKeybinding) selectedClickie).getKeyBinding();
                KeyBinding.keybindArray.remove(binding);
                KeyBinding.hash.removeObject(binding.getKeyCode());
                KeybindManager.getKeybindings().remove(selectedClickie);
            }
            selectedClickie = null;
        }

    }

    @Override
    public void update(double mousex, double mousey) {
        if (selecting) {
            return;
        }
        refreshModules();
        this.closestKeybind = null;
        double closestDistance = Double.MAX_VALUE;
        if (this.selectedClickie != null) {
            this.selectedClickie.move(mousex, mousey);
            if (this.selectedClickie instanceof ClickableModule) {
                ClickableModule selectedModule = ((ClickableModule) this.selectedClickie);
                for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
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
        for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
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
        for (IClickable keybind : KeybindManager.getKeybindings()) {
            if (keybind != selectedClickie) {
                repelOtherModules(keybind);
            }
        }
        for (IClickable module : modules) {
            clampClickiePosition(module);
        }
        for (IClickable keybind : KeybindManager.getKeybindings()) {
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
        MusePoint2D center = ul.plus(br).times(0.5);
        RenderState.blendingOn();
        RenderState.on2D();
        if (selecting) {
            MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.pressKey"), center.x(), center.y());
            RenderState.off2D();
            RenderState.blendingOff();
            return;
        }
        newKeybindButton.draw();
        trashKeybindButton.draw();
        MuseTextureUtils.pushTexture(MuseTextureUtils.ITEM_TEXTURE_QUILT);
        MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.keybindInstructions1"), center.x(), center.y() + 40);
        MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.keybindInstructions2"), center.x(), center.y() + 50);
        MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.keybindInstructions3"), center.x(), center.y() + 60);
        MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.keybindInstructions4"), center.x(), center.y() + 70);
        if (takenTime + 1000 > System.currentTimeMillis()) {
            MusePoint2D pos = newKeybindButton.getPosition().plus(new MusePoint2D(0, -20));
            MuseRenderer.drawCenteredString(StatCollector.translateToLocal("gui.keybindTaken"), pos.x(), pos.y());
        }
        for (ClickableModule module : modules) {
            module.draw();
        }
        for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
            keybind.draw();
        }
        if (selectedClickie != null && closestKeybind != null) {
            MuseRenderer.drawLineBetween(selectedClickie, closestKeybind, Colour.YELLOW);
        }
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
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
                if (KeyBinding.hash.containsItem(key)) {
                    takenTime = System.currentTimeMillis();
                }
                if (!KeyBinding.hash.containsItem(key)) {
                    addKeybind(key, true);
                } else if (Config.allowConflictingKeybinds()) {
                    addKeybind(key, false);
                }
                selecting = false;
            }
        }
    }

    private void addKeybind(int key, boolean free) {
        String name;
        try {
            name = Keyboard.getKeyName(key);
        } catch (Exception e) {
            name = "???";
        }
        KeyBinding keybind = new KeyBinding(name, key, KeybindKeyHandler.mps);
        ClickableKeybinding clickie = new ClickableKeybinding(keybind, newKeybindButton.getPosition().plus(new MusePoint2D(0, -20)), free, false);
        KeybindManager.getKeybindings().add(clickie);
    }
}
