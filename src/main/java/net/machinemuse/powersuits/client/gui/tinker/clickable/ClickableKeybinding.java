package net.machinemuse.powersuits.client.gui.tinker.clickable;

import net.machinemuse.numina.api.gui.IClickable;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.network.packets.MusePacketToggleRequest;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableKeybinding extends ClickableButton {
    protected List<ClickableModule> boundModules = new ArrayList<>();
    public boolean toggleval = false;
    boolean toggled = false;
    KeyBinding keybind;
    public boolean displayOnHUD;


    public ClickableKeybinding(KeyBinding keybind, MusePoint2D position, boolean free, Boolean displayOnHUD) {
        super(ClickableKeybinding.parseName(keybind), position, true);
        this.displayOnHUD = (displayOnHUD!= null) ? displayOnHUD : false;
        this.keybind = keybind;
    }

    static String parseName(KeyBinding keybind) {
        if (keybind.getKeyCode() < 0) {
            return "Mouse" + (keybind.getKeyCode() + 100);
        }
        else {
            return Keyboard.getKeyName(keybind.getKeyCode());
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
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }

        for (ClickableModule module : boundModules) {
            String valstring = (toggleval) ? " on" : " off";
            if ((FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT) && MPSConfig.INSTANCE.toggleModuleSpam())) {
                player.sendMessage(new TextComponentString("Toggled " + module.getModule().getDataName() + valstring));
            }
            ModuleManager.INSTANCE.toggleModuleForPlayer(player, module.getModule().getDataName(), toggleval);
            MusePacketToggleRequest toggleRequest = new MusePacketToggleRequest(player, module.getModule().getDataName(), toggleval);
            PacketSender.sendToServer(toggleRequest);
        }
        toggleval = !toggleval;
    }

    @Override
    public void draw() {
        super.draw();
        for (ClickableModule module : boundModules) {
            MuseRenderer.drawLineBetween(this, module, Colour.LIGHTBLUE);
            GL11.glPushMatrix();
            GL11.glScaled(0.5, 0.5, 0.5);
            if (displayOnHUD) {
                MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("HUD", MuseStringUtils.FormatCodes.BrightGreen), this.position.x()*2 + 6,this.position.y()*2 + 6);
            } else {
                MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.Red), this.position.x()*2 + 6,this.position.y()*2 + 6);
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
                module.move(midpointTangent.x(), midpointTangent.y());
            }
        }
    }

    public boolean equals(ClickableKeybinding other) {
        return other.keybind.getKeyCode() == this.keybind.getKeyCode();
    }

    public void toggleHUDState(){
        displayOnHUD = !displayOnHUD;
    }
}
