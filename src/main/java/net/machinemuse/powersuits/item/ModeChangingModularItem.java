//package net.machinemuse.powersuits.item;
//
//import net.machinemuse.api.ModuleManager;
//import net.machinemuse.api.moduletrigger.IRightClickModule;
//import net.machinemuse.numina.item.NuminaItemUtils;
//import net.machinemuse.numina.network.MusePacketModeChangeRequest;
//import net.machinemuse.numina.network.PacketSender;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.IIcon;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 4:52 PM, 9/5/13
// *
// * Ported to Java by lehjr on 12/12/16.
// */
//public class ModeChangingModularItem implements IModeChangingModularItem {
//    private static ModeChangingModularItem ourInstance = new ModeChangingModularItem();
//
//    public static ModeChangingModularItem getInstance() {
//        return ourInstance;
//    }
//
//    private ModeChangingModularItem() {
//    }
//
//    @Override
//    public void setActiveMode(ItemStack itemStack, String newMode) {
//        NuminaItemUtils.getTagCompound(itemStack).setString("mode", newMode);
//    }
//
//    @Override
//    public String getActiveMode(ItemStack itemStack, EntityPlayer player) {
//        return getActiveMode(itemStack);
//    }
//
//    @Override
//    public void cycleMode(ItemStack itemStack, EntityPlayer player, int dMode) {
//        List<String> modes = getValidModes(itemStack, player);
//        if (!modes.isEmpty()) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) + dMode, modes.size());
//            String newmode = modes.get(newindex);
//            setActiveMode(itemStack, newmode);
//            PacketSender.sendToServer(new MusePacketModeChangeRequest(player,newmode, player.inventory.currentItem));
//        }
//    }
//
//    @Override
//    public String nextMode(ItemStack itemStack, EntityPlayer player) {
//        List<String> modes = getValidModes(itemStack, player);
//        if (!modes.isEmpty()) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) + 1, modes.size());
//            return modes.get(newindex);
//        } else {
//            return "";
//        }
//    }
//
//    @Override
//    public String prevMode(ItemStack itemStack, EntityPlayer player) {
//        List<String> modes = getValidModes(itemStack, player);
//        if (!modes.isEmpty()) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) - 1, modes.size());
//            return modes.get(newindex);
//        } else {
//            return "";
//        }
//    }
//
//    @Override
//    public void cycleModeForItem(ItemStack itemStack, EntityPlayer player, int dMode) {
//        if (itemStack != null) {
//            this.cycleMode(itemStack, player, dMode);
//        }
//    }
//
//    @Override
//    public IIcon getModeIcon(String mode, ItemStack itemStack, EntityPlayer player) {
//        if (!mode.isEmpty())
//            return ModuleManager.getModule(mode).getIcon(itemStack);
//        return null;
//    }
//
//
//    private int clampMode(int selection, int modesSize) {
//        if (selection > 0) {
//            return selection % modesSize;
//        } else {
//            return (selection + modesSize * (-selection)) % modesSize;
//        }
//    }
////-------------
//
//
//    @Override
//    public List<String> getValidModes(ItemStack stack, EntityPlayer player) {
//        return getValidModes(stack);
//    }
//
//    @Override
//    public List<String> getValidModes(ItemStack itemStack) {
////        List<String> validModes = new ArrayList<>();
////
////        for (IRightClickModule module : ModuleManager.getRightClickModules()) {
////            if (module.isValidForItem(itemStack))
////                if (ModuleManager.itemHasActiveModule(itemStack, module.getDataName()))
////                    validModes.add(module.getDataName());
////        }
//        return ModuleManager.getValidModes(itemStack);
//    }
//
//    @Override
//    public String getActiveMode(ItemStack itemStack) {
//        String modeFromNBT = NuminaItemUtils.getTagCompound(itemStack).getString("mode");
//        if (!modeFromNBT.isEmpty()) {
//            return modeFromNBT;
//        } else {
//            List<String> validModes = getValidModes(itemStack);
//            if (!validModes.isEmpty()) {
//                return validModes.get(0);
//            } else {
//                return "";
//            }
//        }
//    }
//}