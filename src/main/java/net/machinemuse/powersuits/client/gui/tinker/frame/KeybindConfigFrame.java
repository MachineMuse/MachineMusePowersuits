package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.client.gui.IClickable;
import net.machinemuse.numina.client.gui.MuseGui;
import net.machinemuse.numina.client.gui.clickable.ClickableButton;
import net.machinemuse.numina.client.gui.clickable.ClickableModule;
import net.machinemuse.numina.client.gui.frame.IGuiFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.control.KeyBindingHelper;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.GradientAndArcCalculator;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.basemod.MPSConfig;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.client.control.KeybindKeyHandler;
import net.machinemuse.powersuits.client.control.KeybindManager;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableKeybinding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.*;

public class KeybindConfigFrame implements IGuiFrame {
    private static KeyBindingHelper keyBindingHelper = new KeyBindingHelper();
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
        newKeybindButton = new ClickableButton(I18n.format("gui.powersuits.newKeybind"), center.plus(new MusePoint2D(0, -8)), true);
        trashKeybindButton = new ClickableButton(I18n.format("gui.powersuits.trashKeybind"), center.plus(new MusePoint2D(0, 8)), true);
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
        } else if (button == 1) {
            for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
                if (keybind.hitBox(x, y)) {
                    keybind.toggleHUDState();
                    return;
                }
            }
        } else if (button > 2) {
            int key = button - 100;

            if (keyBindingHelper.keyBindingHasKey(key)) {
                takenTime = System.currentTimeMillis();
            }
            if (!keyBindingHelper.keyBindingHasKey(key)) {
                addKeybind(key, true);
            } else if (MPSConfig.INSTANCE.GENERAL_ALLOW_CONFLICTING_KEYBINDS.get()) {
                addKeybind(key, false);
            }
            selecting = false;
        }
    }

    /*

     */

//

    public void refreshModules() {
        List<ItemStack> installedModules = ModuleManager.INSTANCE.getPlayerInstalledModules(player);
        List<MusePoint2D> points = GradientAndArcCalculator.pointsInLine(
                installedModules.size(),
                new MusePoint2D(ul.getX() + 10, ul.getY() + 10),
                new MusePoint2D(ul.getX() + 10, br.getY() - 10));
        Iterator<MusePoint2D> pointIterator = points.iterator();
        for (ItemStack module : installedModules) {
            if (module.getItem() instanceof IToggleableModule && !alreadyAdded(module)) {
                ClickableModule clickie = new ClickableModule(module, pointIterator.next());
                modules.add(clickie);
            }
        }
    }

    public boolean alreadyAdded(@Nonnull ItemStack module) {
        if (module.isEmpty())
            return false;

        for (ClickableModule clickie : modules) {
            if (clickie.getModule().getItem().getRegistryName().equals(module.getItem().getRegistryName())) {
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
                keyBindingHelper.removeKey(binding);
//                KeyBinding.HASH.removeObject(binding.getKeyCode());
//                keyBindingHelper.removeKey(binding);
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
        position.setX(clampDouble(position.getX(), ul.getX(), br.getX()));
        position.setY(clampDouble(position.getY(), ul.getY(), br.getY()));
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
                    otherModule.move(midpointTangent.getX(), midpointTangent.getY());
                }
                // Point2D away = directionVector.times(0).plus(modulePosition);
                // module.move(away.getX(), away.getY());
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        MusePoint2D center = ul.plus(br).times(0.5);
        RenderState.blendingOn();
        RenderState.on2D();
        if (selecting) {
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.pressKey"), center.getX(), center.getY());
            RenderState.off2D();
            RenderState.blendingOff();
            return;
        }
        newKeybindButton.render(mouseX, mouseY, partialTicks);
        trashKeybindButton.render(mouseX, mouseY, partialTicks);
        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.keybindInstructions1"), center.getX(), center.getY() + 40);
        MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.keybindInstructions2"), center.getX(), center.getY() + 50);
        MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.keybindInstructions3"), center.getX(), center.getY() + 60);
        MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.keybindInstructions4"), center.getX(), center.getY() + 70);
        if (takenTime + 1000 > System.currentTimeMillis()) {
            MusePoint2D pos = newKeybindButton.getPosition().plus(new MusePoint2D(0, -20));
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.keybindTaken"), pos.getX(), pos.getY());
        }
        for (ClickableModule module : modules) {
            module.render(mouseX, mouseY, partialTicks);
        }
        for (ClickableKeybinding keybind : KeybindManager.getKeybindings()) {
            keybind.render(mouseX, mouseY, partialTicks);
        }
        if (selectedClickie != null && closestKeybind != null) {
            MuseRenderer.drawLineBetween(selectedClickie, closestKeybind, Colour.YELLOW);
        }
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        for (ClickableModule module : modules) {
            if (module.hitBox(x, y)) {
                if (doAdditionalInfo()) {
                    return module.getToolTip();
                }
                return Collections.singletonList(module.getLocalizedName(module.getModule()));
            }
        }
        return null;
    }

    public static boolean doAdditionalInfo() {
        return InputMappings.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
    }


    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {

        int key = p_keyPressed_1_; // no idea which one to use here!!

        if (selecting) {
//                if (KeyBinding.HASH.containsItem(key)) {
            if (keyBindingHelper.keyBindingHasKey(key)) {
                takenTime = System.currentTimeMillis();
            }
//                if (!KeyBinding.HASH.containsItem(key)) {
            if (!keyBindingHelper.keyBindingHasKey(key)) {
                addKeybind(key, true);
            } else if (MPSConfig.INSTANCE.GENERAL_ALLOW_CONFLICTING_KEYBINDS.get()) {
                addKeybind(key, false);
            }
            selecting = false;
        }

        return true; // no idea what to return here!!!
    }


//    @Override
//    public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
//        return false;
//    }
//

    private void addKeybind(int key, boolean free) {
        addKeybind(KeyBindingHelper.getInputByCode(key), free);
    }

    private void addKeybind(InputMappings.Input key, boolean free) {
        String name;
        try {
            name = key.getName();
        } catch (Exception e) {
            name = "???";
        }
        KeyBinding keybind = new KeyBinding(name, key.getKeyCode(), KeybindKeyHandler.mps);
        ClickableKeybinding clickie = new ClickableKeybinding(keybind, newKeybindButton.getPosition().plus(new MusePoint2D(0, -20)), free, false);
        KeybindManager.getKeybindings().add(clickie);
    }
}
