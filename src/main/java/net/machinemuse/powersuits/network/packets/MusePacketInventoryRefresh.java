//package net.machinemuse.powersuits.network.packets;
//
//import io.netty.buffer.ByteBufInputStream;
//import net.machinemuse.numina.network.IMusePackager;
//import net.machinemuse.numina.network.MusePacket;
//import net.machinemuse.powersuits.client.gui.MuseGui;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nonnull;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 12:28 PM, 5/6/13
// *
// * Ported to Java by lehjr on 11/14/16.
// */
//public class MusePacketInventoryRefresh extends MusePacket {
//    EntityPlayer player;
//    int slot;
//    ItemStack stack;
//
//    public MusePacketInventoryRefresh(EntityPlayer player, int slot, @Nonnull ItemStack stack) {
//        this.player = player;
//        this.slot = slot;
//        this.stack = stack;
//    }
//
//    @Override
//    public IMusePackager packager() {
//        return getPackagerInstance();
//    }
//
//    @Override
//    public void write() {
//        writeInt(slot);
//        writeItemStack(stack);
//    }
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public void handleClient(EntityPlayer player) {
//        IInventory inventory = player.inventory;
//        inventory.setInventorySlotContents(slot, stack);
//        ((MuseGui)(Minecraft.getMinecraft().currentScreen)).refresh();
//    }
//
//    public static MusePacketInventoryRefreshPackager getPackagerInstance() {
//        return MusePacketInventoryRefreshPackager.INSTANCE;
//    }
//
//    public enum MusePacketInventoryRefreshPackager implements IMusePackager {
//        INSTANCE;
//
//        @Override
//        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
//            int itemSlot = readInt(datain);
//            ItemStack stack = readItemStack(datain);
//            return new MusePacketInventoryRefresh(player, itemSlot, stack);
//        }
//    }
//}