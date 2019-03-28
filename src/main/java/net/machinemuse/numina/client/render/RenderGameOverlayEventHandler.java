package net.machinemuse.numina.client.render;

import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:17 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
public class RenderGameOverlayEventHandler {

    static {
        new RenderGameOverlayEventHandler();
    }

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
            default:
                break;
        }
    }

    public void drawModeChangeIcons() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        int i = player.inventory.currentItem;
        ItemStack stack = player.inventory.getCurrentItem();
        if (!stack.isEmpty() && stack.getItem() instanceof IModeChangingItem) {

            IModeChangingItem item = (IModeChangingItem) (stack.getItem());
            ScaledResolution screen = new ScaledResolution(mc);
            MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
            RenderState.blendingOn();
            TextureAtlasSprite currentMode = item.getModeIcon(item.getActiveMode(stack), stack, player);
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

    public void drawIcon(double x, double y, TextureAtlasSprite icon, double alpha) {
        if (icon != null)
            MuseIconUtils.drawIconAt(x, y, icon, Colour.WHITE.withAlpha(alpha));
    }
}