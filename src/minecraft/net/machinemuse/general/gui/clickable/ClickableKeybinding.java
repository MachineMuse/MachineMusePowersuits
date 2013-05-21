package net.machinemuse.general.gui.clickable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.powersuits.network.packets.MusePacketToggleRequest;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClickableKeybinding extends ClickableButton {
    protected List<ClickableModule> boundModules;
    protected KeyBinding keybind;
    protected boolean toggleval;
    protected boolean toggled;
    protected boolean free;

    public ClickableKeybinding(KeyBinding keybind, MusePoint2D position, boolean free) {
        super(parseName(keybind), position, true);
        this.keybind = keybind;
        this.position = position;
        this.boundModules = new ArrayList<ClickableModule>();
        this.enabled = free;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void doToggleTick() {
        doToggleIf(keybind.pressed);
    }

    public void doToggleIf(boolean value) {
        if (value && !toggled) {
            toggleModules();
        }
        toggled = value;
    }

    public void toggleModules() {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }
        for (ClickableModule module : boundModules) {
            String valstring = toggleval ? " on" : " off";
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                player.sendChatToPlayer("Toggled " + module.getModule().getName() + valstring);
            }
            MuseItemUtils.toggleModuleForPlayer(player, module.getModule().getName(), toggleval);
            MusePacketToggleRequest toggleRequest = new MusePacketToggleRequest((Player) player, module.getModule().getName(), toggleval);
            player.sendQueue.addToSendQueue(toggleRequest.getPacket250());
        }
        toggleval = !toggleval;
    }

    public static String parseName(KeyBinding keybind) {
        if (keybind.keyCode < 0) {
            return "Mouse" + (keybind.keyCode + 100);
        } else {
            return Keyboard.getKeyName(keybind.keyCode);
        }
    }

    @Override
    public void draw() {
        super.draw();
        for (ClickableModule module : boundModules) {
            MuseRenderer.drawLineBetween(this, module, Colour.LIGHTBLUE);
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

    public boolean equals(ClickableKeybinding other) {
        return other.keybind.keyCode == this.keybind.keyCode;
    }

}
