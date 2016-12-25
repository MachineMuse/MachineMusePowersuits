package net.machinemuse.numina.mouse;

import net.machinemuse.numina.item.IModeChangingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:40 PM, 9/5/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public final class MouseEventHandler {
    @SubscribeEvent
    public void onMouseEvent(MouseEvent e) {
        if (e.getDwheel() != 0) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack != null && stack.getItem() instanceof IModeChangingItem) {
                IModeChangingItem item = (IModeChangingItem) stack.getItem();
                if (player.isSneaking()) {
                    item.cycleMode(stack, player, e.getDwheel() / 120);
                    e.setCanceled(true);
                }
            }
        }
    }
}