package net.machinemuse.powersuits.client.event;

import net.machinemuse.numina.client.gui.clickable.ClickableModule;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.DrawableMuseRect;
import net.machinemuse.powersuits.basemod.MPSConfig;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.client.control.KeybindManager;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.client.model.helper.MPSModelHelper;
import net.machinemuse.powersuits.constants.MPSModuleConstants;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderEventHandler {

    private static final MPSConfig config = MPSConfig.INSTANCE;
    private static boolean ownFly;
    private final DrawableMuseRect frame = new DrawableMuseRect(config.HUD_KEYBIND_HUD_X.get(), config.HUD_KEYBIND_HUD_Y.get(), config.HUD_KEYBIND_HUD_X.get() + (double) 16, config.HUD_KEYBIND_HUD_Y.get() + (double) 16, true, Colour.DARKGREEN.withAlpha(0.2), Colour.GREEN.withAlpha(0.2));

    public RenderEventHandler() {
        this.ownFly = false;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void preTextureStitch(TextureStitchEvent.Pre event) {
        MuseIcon.registerIcons(event);
        MPSModelHelper.loadArmorModels(event);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event) {

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(RenderGameOverlayEvent.Post e) {
        RenderGameOverlayEvent.ElementType elementType = e.getType();
        if (RenderGameOverlayEvent.ElementType.HOTBAR.equals(elementType)) {
            this.drawKeybindToggles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        MainWindow screen = mc.mainWindow;
    }

    @SubscribeEvent
    public void onPreRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!event.getEntityPlayer().abilities.isFlying && !event.getEntityPlayer().onGround && this.playerHasFlightOn(event.getEntityPlayer())) {
            event.getEntityPlayer().abilities.isFlying = true;
            RenderEventHandler.ownFly = true;
        }
    }

    private boolean playerHasFlightOn(EntityPlayer player) {
        return !ModuleManager.INSTANCE.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), new ResourceLocation(MPSItems.INSTANCE.MODULE_JETPACK__REGNAME)) ||
                ModuleManager.INSTANCE.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), new ResourceLocation(MPSItems.INSTANCE.MODULE_GLIDER__REGNAME)) ||
                ModuleManager.INSTANCE.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.FEET), new ResourceLocation(MPSItems.INSTANCE.MODULE_JETBOOTS__REGNAME)) ||
                ModuleManager.INSTANCE.itemHasActiveModule(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), new ResourceLocation(MPSItems.INSTANCE.MODULE_FLIGHT_CONTROL__REGNAME));
    }

    @SubscribeEvent
    public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
        if (RenderEventHandler.ownFly) {
            RenderEventHandler.ownFly = false;
            event.getEntityPlayer().abilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent e) {
        ItemStack helmet = e.getEntity().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (ModuleManager.INSTANCE.itemHasActiveModule(helmet, new ResourceLocation(MPSItems.INSTANCE.BINOCULARS_MODULE__REGNAME))) {
            e.setNewfov(e.getNewfov() / (float) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(helmet, MPSModuleConstants.FOV));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawKeybindToggles() {
        if (config.HUD_DISPLAY_HUD.get()) {
            Minecraft mc = Minecraft.getInstance();
            EntityPlayerSP player = mc.player;
            frame.setLeft(config.HUD_KEYBIND_HUD_X.get());
            frame.setTop(config.HUD_KEYBIND_HUD_Y.get());
            frame.setBottom(frame.top() + 16);
            for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
                if (kb.displayOnHUD) {
                    double stringwidth = MuseRenderer.getStringWidth(kb.getLabel());
                    frame.setWidth(stringwidth + kb.getBoundModules().size() * 16);
                    frame.draw();
                    MuseRenderer.drawString(kb.getLabel(), frame.left() + 1, frame.top() + 3, (kb.toggleval) ? Colour.RED : Colour.GREEN);
                    double x = frame.left() + stringwidth;
                    for (ClickableModule module : kb.getBoundModules()) {
                        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
                        boolean active = false;
                        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
                            if (!module.getModule().isEmpty())
                                active = ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getModule().getItem().getRegistryName());
                        }
                        MuseRenderer.drawModuleAt(x, frame.top(), module.getModule(), active);
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