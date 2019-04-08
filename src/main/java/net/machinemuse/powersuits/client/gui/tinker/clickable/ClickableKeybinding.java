package net.machinemuse.powersuits.client.gui.tinker.clickable;

import net.machinemuse.numina.client.gui.IClickable;
import net.machinemuse.numina.client.gui.clickable.ClickableButton;
import net.machinemuse.numina.client.gui.clickable.ClickableModule;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.numina.network.packets.MusePacketToggleRequest;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.client.control.KeybindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableKeybinding extends ClickableButton {
    public boolean toggleval = false;
    public boolean displayOnHUD;
    protected List<ClickableModule> boundModules = new ArrayList<>();
    boolean toggled = false;
    KeyBinding keybind;

    public ClickableKeybinding(KeyBinding keybind, MusePoint2D position, boolean free, Boolean displayOnHUD) {
        super(ClickableKeybinding.parseName(keybind), position, true);
        this.displayOnHUD = (displayOnHUD != null) ? displayOnHUD : false;
        this.keybind = keybind;
    }

    static String parseName(KeyBinding keybind) {
        if (keybind.getKey().getKeyCode() < 0) {
            return "Mouse" + (keybind.getKey().getKeyCode() + 100);
        } else {
            return keybind.getKey().getName();
        }
    }

    public void doToggleTick() {
        doToggleIf(keybind.isPressed());
    }

    public void doToggleIf(boolean value) {
        if (value && !toggled) {
            toggleModules();
            KeybindManager.writeOutKeybinds();
        }
        toggled = value;
    }

    public void toggleModules() {
        EntityPlayerSP player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        for (ClickableModule module : boundModules) {
            String valstring = (toggleval) ? " on" : " off";
            NuminaPackets.sendToServer(new MusePacketToggleRequest(player.getEntityId(), module.getModule().getItem().getRegistryName(), toggleval));
        }
        toggleval = !toggleval;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        for (ClickableModule module : boundModules) {
            MuseRenderer.drawLineBetween(this, module, Colour.LIGHTBLUE);
            GL11.glPushMatrix();
            GL11.glScaled(0.5, 0.5, 0.5);
            if (displayOnHUD) {
                MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("HUD", MuseStringUtils.FormatCodes.BrightGreen), this.position.getX() * 2 + 6, this.position.getY() * 2 + 6);
            } else {
                MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.Red), this.position.getX() * 2 + 6, this.position.getY() * 2 + 6);
            }
            GL11.glPopMatrix();
        }
    }

    public KeyBinding getKeyBinding() {
        return keybind;
    }

    public List<ClickableModule> getBoundModules() {
        return boundModules;
    }

    public void bindModule(ClickableModule module) {
        if (!boundModules.contains(module)) {
            boundModules.add(module);
        }
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
        return (boundModules.size() > 6) ? (16 + (boundModules.size() - 6) * 3) : 16;
    }

    public void attractBoundModules(IClickable exception) {
        for (ClickableModule module : boundModules) {
            if (!module.equals(exception)) {
                MusePoint2D euclideanDistance = module.getPosition().minus(this.getPosition());
                MusePoint2D directionVector = euclideanDistance.normalize();
                MusePoint2D tangentTarget = directionVector.times(getTargetDistance()).plus(this.getPosition());
                MusePoint2D midpointTangent = module.getPosition().midpoint(tangentTarget);
                module.move(midpointTangent.getX(), midpointTangent.getY());
            }
        }
    }

    public boolean equals(ClickableKeybinding other) {
        return other.keybind.getKey().getKeyCode() == this.keybind.getKey().getKeyCode();
    }

    public void toggleHUDState() {
        displayOnHUD = !displayOnHUD;
    }
}
