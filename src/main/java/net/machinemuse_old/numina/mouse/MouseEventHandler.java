package net.machinemuse_old.numina.mouse;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:40 PM, 9/5/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public final class MouseEventHandler {
/*    @SubscribeEvent
    public void onMouseEvent(MouseEvent e) {
        if (e.getDwheel() != 0) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack != null && stack.getItem() instanceof IModeChangingItem) {
                IModeChangingItem item = (IModeChangingItem) stack.getItem();
//Replace this with check for correct hotbar key.\/
//                if (player.isSneaking()) {
                if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[player.inventory.currentItem].getKeyCode())) {
                    item.cycleMode(stack, player, e.getDwheel() / 120);
                    e.setCanceled(true);
                }
            }
        }
    }*/
}