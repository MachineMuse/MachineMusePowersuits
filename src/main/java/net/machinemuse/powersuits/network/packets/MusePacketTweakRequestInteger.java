//package net.machinemuse.powersuits.network.packets;
//
//import io.netty.buffer.ByteBufInputStream;
//import net.machinemuse.numina.common.constants.NuminaNBTConstants;
//import net.machinemuse.numina.network.IMusePackager;
//import net.machinemuse.numina.network.MusePacket;
//import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
//import net.machinemuse.powersuits.common.ModuleManager;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//
///**
// * Packet for requesting to purchase an upgrade. Player-to-server. Server
// * decides whether it is a valid upgrade or not and <strike>replies with an associated
// * inventoryrefresh packet</strike>.
// *
// * Author: MachineMuse (Claire Semple)
// * Created: 12:28 PM, 5/6/13
// *
// * Ported to Java by lehjr on 11/14/16.
// */
//public class MusePacketTweakRequestInteger extends MusePacket {
//    final EntityPlayer player;
//    final int itemSlot;
//    final String moduleName;
//    final String tweakName;
//    int tweakValue;
//
//    public MusePacketTweakRequestInteger(EntityPlayer player, int itemSlot, String moduleName, String tweakName, int tweakValue) {
//        this.player = player;
//        this.itemSlot = itemSlot;
//        this.moduleName = moduleName;
//        this.tweakName = tweakName;
//        this.tweakValue = tweakValue;
//    }
//
//    @Override
//    public IMusePackager getPackagerInstance() {
//        return getPackagerInstance();
//    }
//
//    @Override
//    public void write() {
//        writeInt(this.itemSlot);
//        writeString(this.moduleName);
//        writeString(this.tweakName);
//        writeInt(this.tweakValue);
//    }
//
//    @Override
//    public void handleServer(EntityPlayerMP player) {
//        if (moduleName != null && tweakName != null) {
//            ItemStack stack = player.inventory.getStackInSlot(itemSlot);
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//
//            if (itemTag != null && ModuleManager.INSTANCE.tagHasModule(itemTag, moduleName)) {
//                MuseNBTUtils.removeMuseValuesTag(stack);
//                itemTag.removeTag(NuminaNBTConstants.TAG_VALUES);
//                NBTTagCompound moduleTag = itemTag.getCompoundTag(moduleName);
//                moduleTag.setInteger(tweakName, tweakValue);
//            }
//        }
//    }
//
//    public static MusePacketTweakRequestPackager getPackagerInstance() {
//        return MusePacketTweakRequestPackager.INSTANCE;
//    }
//
//    public enum MusePacketTweakRequestPackager implements IMusePackager {
//        INSTANCE;
//
//        @Override
//        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
//            int itemSlot = readInt(datain);
//            String moduleName = readString(datain);
//            String tweakName = readString(datain);
//            int tweakValue = readInt(datain);
//            return new MusePacketTweakRequestInteger(player, itemSlot, moduleName, tweakName, tweakValue);
//        }
//    }
//}