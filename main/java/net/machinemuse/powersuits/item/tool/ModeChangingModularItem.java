//package net.machinemuse.powersuits.item;
//
//import net.machinemuse.numina.api.module.IModule;
//import net.machinemuse.numina.api.module.ModuleManager;
//import net.machinemuse.numina.api.module.IRightClickModule;
//import net.machinemuse.numina.item.IModeChangingItemCapability;
//import net.machinemuse.numina.item.NuminaItemUtils;
//import net.machinemuse.numina.network.MusePacketModeChangeRequest;
//import net.machinemuse.numina.network.PacketSender;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 4:52 PM, 9/5/13
// *
// * Ported to Java by lehjr on 11/1/16.
// */
//public class ModeChangingModularItem implements IModeChangingItemCapability {
//    private static ModeChangingModularItem INSTANCE;
//
//    public static ModeChangingModularItem getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new ModeChangingModularItem();
//        }
//        return INSTANCE;
//    }
//
//    @Nullable
//    @Override
//    public IIcon getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
//        IModule module = ModuleManager.getModule(mode);
//        if (module != null)
//            return module.getIcon(stack);
//        return null;
//    }
//
//    @Override
//    public List<String> getValidModes(ItemStack stack) {
//        List<String> modes = new ArrayList<>();
//        for (IRightClickModule module : ModuleManager.getRightClickModules()) {
//            if (module.isValidForItem(stack))
//                if (ModuleManager.itemHasModule(stack, module.getDataName()))
//                    modes.add(module.getDataName());
//        }
//        return modes;
//    }
//
//    @Override
//    public String getActiveMode(ItemStack stack) {
//        List<String> validModes;
//        String modeFromNBT = NuminaItemUtils.getTagCompound(stack).getString("mode");
//        if (!modeFromNBT.isEmpty())
//            return modeFromNBT;
//        else {
//            validModes = getValidModes(stack);
//            return (!validModes.isEmpty()) ? validModes.get(0) : "";
//        }
//    }
//
//    @Override
//    public void setActiveMode(ItemStack stack, String newMode) {
//        NuminaItemUtils.getTagCompound(stack).setString("mode", newMode);
//    }
//
//    @Override
//    public void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
//        List<String> modes = this.getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(this.getActiveMode(stack)) + dMode, modes.size());
//            String newmode = (String)modes.get(newindex);
//            this.setActiveMode(stack, newmode);
//            PacketSender.sendToServer(new MusePacketModeChangeRequest(player, newmode, player.inventory.currentItem));
//        }
//    }
//
//    @Override
//    public String nextMode(ItemStack stack, EntityPlayer player) {
//        List<String> modes = getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) + 1, modes.size());
//            return (String)modes.get(newindex);
//        }
//        else {
//            return "";
//        }
//    }
//
//    @Override
//    public String prevMode(ItemStack stack, EntityPlayer player) {
//        List<String> modes = this.getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) - 1, modes.size());
//            return (String)modes.get(newindex);
//        }
//        else {
//            return "";
//        }
//    }
//
//    private static int clampMode(int selection, int modesSize) {
//        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
//    }
//}