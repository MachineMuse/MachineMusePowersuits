package net.machinemuse_old.powersuits.event;

import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.general.gui.clickable.ClickableKeybinding;
import net.machinemuse_old.general.gui.clickable.ClickableModule;
import net.machinemuse_old.numina.geometry.Colour;
import net.machinemuse_old.numina.geometry.DrawableMuseRect;
import net.machinemuse_old.numina.render.MuseIconUtils;
import net.machinemuse_old.numina.render.MuseTextureUtils;
import net.machinemuse_old.powersuits.client.render.model.ModelHelper;
import net.machinemuse_old.powersuits.common.Config;
import net.machinemuse_old.powersuits.control.KeybindManager;
import net.machinemuse_old.powersuits.powermodule.misc.BinocularsModule;
import net.machinemuse_old.powersuits.powermodule.movement.FlightControlModule;
import net.machinemuse_old.powersuits.powermodule.movement.GliderModule;
import net.machinemuse_old.powersuits.powermodule.movement.JetBootsModule;
import net.machinemuse_old.powersuits.powermodule.movement.JetPackModule;
import net.machinemuse_old.utils.MuseItemUtils;
import net.machinemuse_old.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Ported to Java by lehjr on 10/24/16.
 */
public class RenderEventHandler {
    private static boolean ownFly;
    private final DrawableMuseRect frame = new DrawableMuseRect(Config.keybindHUDx(), Config.keybindHUDy(), Config.keybindHUDx() + (double)16, Config.keybindHUDy() + (double)16, true, Colour.DARKGREEN.withAlpha(0.2), Colour.GREEN.withAlpha(0.2));

    public RenderEventHandler() {
        this.ownFly = false;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void preTextureStitch(TextureStitchEvent.Pre event) {
        MuseIcon.registerIcons(event);
        ModelHelper.loadArmorModels(false);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event) {

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution screen = new ScaledResolution(mc);
    }

    @SubscribeEvent
    public void onPreRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!event.getEntityPlayer().capabilities.isFlying && !event.getEntityPlayer().onGround && this.playerHasFlightOn(event.getEntityPlayer())) {
            event.getEntityPlayer().capabilities.isFlying = true;
            RenderEventHandler.ownFly =true;
        }
    }

    private boolean playerHasFlightOn(EntityPlayer player) {
        return ModuleManager.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), JetPackModule.MODULE_JETPACK) ||
                ModuleManager.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), GliderModule.MODULE_GLIDER) ||
                ModuleManager.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.FEET), JetBootsModule.MODULE_JETBOOTS) ||
                ModuleManager.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), FlightControlModule.MODULE_FLIGHT_CONTROL);
    }

    @SubscribeEvent
    public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
        if (RenderEventHandler.ownFly) {
            RenderEventHandler.ownFly = false;
            event.getEntityPlayer().capabilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent e) {
        ItemStack helmet = e.getEntity().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (ModuleManager.itemHasActiveModule(helmet, "Binoculars")) {
            e.setNewfov(e.getNewfov() / (float)ModuleManager.computeModularProperty(helmet, BinocularsModule.FOV_MULTIPLIER));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(RenderGameOverlayEvent.Post e) {
        RenderGameOverlayEvent.ElementType elementType = e.getType();
        if (RenderGameOverlayEvent.ElementType.HOTBAR.equals((Object)elementType)) {
            this.drawKeybindToggles();
        }
    }
    @SideOnly(Side.CLIENT)
    public void drawKeybindToggles() {
        if (Config.keybindHUDon()) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;
            ScaledResolution screen = new ScaledResolution(mc);
            frame.setLeft(Config.keybindHUDx());
            frame.setTop(Config.keybindHUDy());
            frame.setBottom(frame.top() + 16);
            for (ClickableKeybinding kb:   KeybindManager.getKeybindings()) {
                if (kb.displayOnHUD) {
                    double stringwidth = net.machinemuse_old.utils.render.MuseRenderer.getStringWidth(kb.getLabel());
                    frame.setWidth(stringwidth + kb.getBoundModules().size() * 16);
                    frame.draw();
                    MuseRenderer.drawString(kb.getLabel(), frame.left() + 1, frame.top() + 3, (kb.toggleval) ? Colour.RED : Colour.GREEN);
                    double x = frame.left() + stringwidth;
                    for (ClickableModule module:  kb.getBoundModules()) {
                        MuseTextureUtils.pushTexture(module.getModule().getStitchedTexture(null));
                        boolean active = false;

                        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
                            if (ModuleManager.itemHasActiveModule(stack, module.getModule().getDataName()))
                                active = true;
                        }

                        MuseIconUtils.drawIconAt(x, frame.top(), module.getModule().getIcon(null), (active) ? Colour.WHITE : Colour.DARKGREY.withAlpha(0.5));
                        MuseTextureUtils.popTexture();
                        x += 16;
                    }
                    frame.setTop(frame.top() + 16);
                    frame.setBottom(frame.top() + 16);
                }
            }
        }
    }
}