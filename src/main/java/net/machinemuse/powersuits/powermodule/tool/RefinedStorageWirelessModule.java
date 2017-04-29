package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by leon on 4/26/17.
 */
public class RefinedStorageWirelessModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_REF_STOR_WIRELESS = "Refined Storage Wireless Grid";
    public static final String REF_STOR_WIRELESS_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final ItemStack emulatedTool = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("refinedstorage", "wireless_grid")), 1, 0);



//<refinedstorage:wireless_grid>.withTag({Energy: 3050, SearchBoxMode: 0, SortingType: 0, ControllerZ: -124, ControllerY: 63, SortingDirection: 1, ControllerX: -846, DimensionID: 0, ViewType: 0})
// <refinedstorage:wireless_grid>.withTag({Energy: 3200})

    public RefinedStorageWirelessModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(emulatedTool);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return "refstorwirelessgrid";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {




        return null;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }
}
