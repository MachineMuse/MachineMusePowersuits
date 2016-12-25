package net.machinemuse.numina.render;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.item.IModeChangingItem;
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
 *
 * Ported to Java by lehjr on 10/25/16.
 */
public class RenderGameOverlayEventHandler {
    public static int SWAPTIME = 200;
    public static long lastSwapTime =  0L;
    private static int lastSwapDirection = 0;

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
            default: break;
        }
    }

    public void drawModeChangeIcons() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        int i = player.inventory.currentItem;
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof IModeChangingItem) {




            IModeChangingItem item = (IModeChangingItem)(stack.getItem());
            ScaledResolution screen = new ScaledResolution(mc);
            MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
            RenderState.blendingOn();
            long swapTime = Math.min(System.currentTimeMillis() - lastSwapTime, SWAPTIME);
            TextureAtlasSprite currentMode = item.getModeIcon(item.getActiveMode(stack), stack, player);
            TextureAtlasSprite nextMode = item.getModeIcon(item.nextMode(stack, player), stack, player);
            TextureAtlasSprite prevMode = item.getModeIcon(item.prevMode(stack, player), stack, player);
            double prevX = .0;
            double prevY = .0;
            double currX = .0;
            double currY = .0;
            double nextX = .0;
            double nextY = .0;
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
            prevX = sw / 2.0 - 105.0 + 20.0 * i;
            prevY = baroffset - 8;
            currX = sw / 2.0 - 89.0 + 20.0 * i;
            currY = baroffset - 18;
            nextX = sw / 2.0 - 73.0 + 20.0 * i;
            nextY = baroffset - 8;
            if (swapTime == SWAPTIME || lastSwapDirection == 0) {
                drawIcon(prevX, prevY, prevMode, 0.4);
                drawIcon(currX, currY, currentMode, 0.8);
                drawIcon(nextX, nextY, nextMode, 0.4);
            }
            else {
                double r1 = 1 - swapTime / SWAPTIME;
                double r2 = swapTime / SWAPTIME;
                if (lastSwapDirection == -1) {
                    nextX = currX * r1 + nextX * r2;
                    nextY = currY * r1 + nextY * r2;
                    currX = prevX * r1 + currX * r2;
                    currY = prevY * r1 + currY * r2;
                    drawIcon(currX, currY, currentMode, 0.8);
                    drawIcon(nextX, nextY, nextMode, 0.8);
                }
                else {
                    prevX = currX * r1 + prevX * r2;
                    prevY = currY * r1 + prevY * r2;
                    currX = nextX * r1 + currX * r2;
                    currY = nextY * r1 + currY * r2;
                    drawIcon(prevX, prevY, prevMode, 0.8);
                    drawIcon(currX, currY, currentMode, 0.8);
                }
            }
            RenderState.scissorsOff();
            RenderState.blendingOff();
            MuseTextureUtils.popTexture();
            Colour.WHITE.doGL();
        }
    }

    public void drawIcon(double x, double y, TextureAtlasSprite icon, double alpha) {
        if (icon !=null)
            MuseIconUtils.drawIconAt(x, y, icon, Colour.WHITE.withAlpha(alpha));
    }

    public static void updateSwap(int dModeSig) {
        lastSwapTime = System.currentTimeMillis();
        lastSwapDirection = dModeSig;
    }
}