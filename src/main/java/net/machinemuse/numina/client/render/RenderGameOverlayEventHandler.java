package net.machinemuse.numina.client.render;

import net.machinemuse.numina.api.capability_ports.inventory.IModeChangingItemCapability;
import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.capability_ports.itemwrapper.ModeChangingItemWrapper;
import net.machinemuse.numina.api.capability_ports.itemwrapper.ModularItemWrapper;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.math.geometry.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:17 PM, 9/6/13
 *
 * Ported to Java by lehjr on 10/25/16.
 */
public class RenderGameOverlayEventHandler {
    @SubscribeEvent
    public void onPreRenderGameOverlayEvent(final RenderGameOverlayEvent.Pre e) {
//        switch (e.type) {
//            case ALL:
//            case HELMET: break;
//            case PORTAL: break;
//            case CROSSHAIRS: break;
//            case BOSSHEALTH: break;
//            case ARMOR: break;
//            case HEALTH: break;
//            case FOOD: break;
//            case AIR: break;
//            case HOTBAR: break;
//            case EXPERIENCE: break;
//            case TEXT: break;
//            case HEALTHMOUNT: break;
//            case JUMPBAR: break;
//            default:break;
//        }
    }


    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(final RenderGameOverlayEvent.Post e) {
        switch (e.getType()) {
//            case ALL: break;
//            case HELMET: break;
//            case PORTAL: break;
//            case CROSSHAIRS: break;
//            case BOSSHEALTH: break;
//            case ARMOR: break;
//            case HEALTH: break;
//            case FOOD: break;
//            case AIR: break;
            case HOTBAR:
                drawModeChangeIcons();
                break;
//            case EXPERIENCE: break;
//            case TEXT: break;
//            case HEALTHMOUNT: break;
//            case JUMPBAR: break;
            default: break;
        }
    }

    public void drawModeChangeIcons() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        int i = player.inventory.currentItem;
        ItemStack stack = player.inventory.getCurrentItem();
        if (!stack.isEmpty()) {
            IItemHandler modeChangingCapability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (modeChangingCapability instanceof IModeChangingItemCapability) {


                System.out.println("active mode index: " + ((IModeChangingItemCapability) modeChangingCapability).getActiveMode());

                System.out.println("installed Module list size: " + ((IModeChangingItemCapability) modeChangingCapability).getInstalledModuleNames().size());


                TextureAtlasSprite currentMode = ((IModeChangingItemCapability) modeChangingCapability).getModeIcon(((IModeChangingItemCapability) modeChangingCapability).getActiveMode());

                if (currentMode != null) {
                    ScaledResolution screen = new ScaledResolution(mc);
                    MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
                    RenderState.blendingOn();
                    double currX;
                    double currY;
                    int sw = screen.getScaledWidth();
                    int sh = screen.getScaledHeight();
                    int baroffset = 22;
                    if (!player.capabilities.isCreativeMode) {
                        baroffset += 16;
                        if (ForgeHooks.getTotalArmorValue(player) > 0) {
                            baroffset += 8;
                        }
                    }
                    RenderState.scissorsOn(0, 0, sw, sh - baroffset);
                    baroffset = screen.getScaledHeight() - baroffset;
                    currX = sw / 2.0 - 89.0 + 20.0 * i;
                    currY = baroffset - 18;
                    drawIcon(currX, currY, currentMode, 0.8);
                    RenderState.scissorsOff();
                    RenderState.blendingOff();
                    MuseTextureUtils.popTexture();
                    Colour.WHITE.doGL();
                }
            }
        }
    }

    public void drawIcon(double x, double y, TextureAtlasSprite icon, double alpha) {
        if (icon !=null)
            MuseIconUtils.drawIconAt(x, y, icon, Colour.WHITE.withAlpha(alpha));
    }
}