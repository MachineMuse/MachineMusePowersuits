package net.machinemuse.powersuits.item;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.numina.item.ModeChangingItem;
import net.machinemuse.numina.item.NuminaItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 *
 * Ported to Java by lehjr on 11/1/16.
 */
public class ModeChangingModularItem implements IModeChangingModularItem {
    private static ModeChangingModularItem INSTANCE;

    public static ModeChangingModularItem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModeChangingModularItem();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public IIcon getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
        IPowerModule module = ModuleManager.getModule(mode);
        if (module != null)
            return module.getIcon(stack);
        return null;
    }

    @Override
    public List<String> getValidModes(ItemStack stack, EntityPlayer player) {
        return getValidModes(stack);
    }

    @Override
    public List<String> getValidModes(ItemStack stack) {
        List<String> modes = new ArrayList<>();
        for (IRightClickModule module : ModuleManager.getRightClickModules()) {
            if (module.isValidForItem(stack))
                if (ModuleManager.itemHasModule(stack, module.getDataName()))
                    modes.add(module.getDataName());
        }
        return modes;
    }

    @Override
    public String getActiveMode(ItemStack stack) {
        List<String> validModes;

        String modeFromNBT = NuminaItemUtils.getTagCompound(stack).getString("mode");
        if (!modeFromNBT.isEmpty())
            return modeFromNBT;
        else {
            validModes = getValidModes(stack);
            if (!validModes.isEmpty())
                return validModes.get(0);
            else
                return "";
        }
    }

    public static void cycleModeForItem(ItemStack stack, EntityPlayer player, int dMode) {
        Item item;
        if (stack != null) {
            item = stack.getItem();
            if (item instanceof IModeChangingModularItem)
                ((IModeChangingModularItem) item).cycleMode(stack, player, dMode);
        }
    }

    /* IModeChangingItem -------------------------------------------------------------------------- */

    @Override
    public void setActiveMode(ItemStack stack, String newMode) {
        ModeChangingItem.getInstance().setActiveMode(stack, newMode);
    }

    @Override
    public String getActiveMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().getActiveMode(stack, player);
    }

    @Override
    public void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
        ModeChangingModularItem.getInstance().cycleMode(stack, player, dMode);
    }

    @Override
    public String nextMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().nextMode(stack, player);
    }

    @Override
    public String prevMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().prevMode(stack, player);
    }
}