package net.machinemuse.powersuits.powermodule.tool;

import com.raoulvdberge.refinedstorage.api.network.item.INetworkItem;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemHandler;
import com.raoulvdberge.refinedstorage.apiimpl.network.item.NetworkItemWirelessGrid;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by leon on 4/26/17.
 */
public class RefinedStorageWirelessModule extends PowerModuleBase implements IRightClickModule {
    //    public static ItemStack emulatedTool;
    public static final ResourceLocation wirelessGridRegName = new ResourceLocation("refinedstorage", "wireless_grid");
    public static final ResourceLocation wirelessCraftingGridRegName = new ResourceLocation("refinedstorage", "wireless_crafting_grid");

    public RefinedStorageWirelessModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
//        if (ModCompatibility.isWirelessCraftingGridLoaded())
//            emulatedTool = new ItemStack( Item.REGISTRY.getObject(wirelessCraftingGridRegName), 1, 0);
//        else
//            emulatedTool = new ItemStack( Item.REGISTRY.getObject(wirelessGridRegName), 1, 0);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), getEmulatedTool());
    }

    public static NBTTagCompound getModululeTag(ItemStack itemStackIn) {
        return itemStackIn.getTagCompound().getCompoundTag("mmmpsmod").getCompoundTag("Refined Storage Wireless Grid");
    }

    @Nonnull
    public static INetworkItem provide(INetworkItemHandler handler, EntityPlayer player, ItemStack itemStackIn, int slot) {
        ItemStack emulatedTool = getEmulatedTool();
        NBTTagCompound tag = getModululeTag(itemStackIn);
        emulatedTool.setTagCompound(tag);
        //FIXME: no longer exists, probably the least of the problems
//        if (ModCompatibility.isWirelessCraftingGridLoaded())
//            return new NetworkItemWirelessCraftingGrid(handler, player, emulatedTool);
        return new NetworkItemWirelessGrid(handler, player, emulatedTool, slot);
    }

    static ItemStack getEmulatedTool() {
        if (ModCompatibility.isWirelessCraftingGridLoaded())
            return new ItemStack(Item.REGISTRY.getObject(wirelessCraftingGridRegName), 1, 0);
        else
            return new ItemStack(Item.REGISTRY.getObject(wirelessGridRegName), 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_REF_STOR_WIRELESS__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        NBTTagCompound tag = getModululeTag(itemStackIn);
        ItemStack emulatedTool = getEmulatedTool();

        if (tag != null) {
            if (!isModuleTagValid(tag)) {
                tag = initializeDefaults(tag);
            }

            if (!isModuleTagSet(tag)) {
                return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
            }

            int energy = (int) MuseMathUtils.clampDouble(ElectricItemUtils.getPlayerEnergy(playerIn) * NuminaConfig.INSTANCE.getRSRatio(), 0, 3500);
            tag.setInteger("Energy", energy);
            emulatedTool.setTagCompound(tag);
            ActionResult result = emulatedTool.getItem().onItemRightClick(worldIn, playerIn, hand);
            double energyUsed = ((energy - emulatedTool.getTagCompound().getInteger("Energy")) * NuminaConfig.INSTANCE.getRSRatio());
            ElectricItemUtils.drainPlayerEnergy(playerIn, (int) energyUsed);
            return ActionResult.newResult(result.getType(), itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    /*
     * sets up the nbt tags needed to use the device and stores them in the power fists's tags to be passed back during right click.
     * The wireless grid and wireless crafting grid are a bit different but we can handle both the same way.
     */
    @Override
    public EnumActionResult onItemUse(ItemStack itemStackIn, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        NBTTagCompound tag = getModululeTag(itemStackIn);
        ItemStack emulatedTool = getEmulatedTool();
        emulatedTool.setTagCompound(tag);
        EnumActionResult result = emulatedTool.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        NBTTagCompound tag2 = emulatedTool.getTagCompound();

        // maybe loop through a set of string keys instead?
        if (tag2 != null) {
            if (tag2.hasKey("Initialized"))
                tag.setInteger("Initialized", 1);

            if (tag2.hasKey("SearchBoxMode"))
                tag.setInteger("SearchBoxMode", tag2.getInteger("SearchBoxMode"));

            if (tag2.hasKey("SortingType"))
                tag.setInteger("SortingType", tag2.getInteger("SortingType"));

            if (tag2.hasKey("DimensionID"))
                tag.setInteger("DimensionID", tag2.getInteger("DimensionID"));

            if (tag2.hasKey("ViewType"))
                tag.setInteger("ViewType", tag2.getInteger("ViewType"));

            if (tag2.hasKey("GridX"))
                tag.setInteger("GridX", tag2.getInteger("GridX"));

            if (tag2.hasKey("GridY"))
                tag.setInteger("GridY", tag2.getInteger("GridY"));

            if (tag2.hasKey("GridZ"))
                tag.setInteger("GridZ", tag2.getInteger("GridZ"));

            if (tag2.hasKey("ControllerX"))
                tag.setInteger("ControllerX", tag2.getInteger("ControllerX"));

            if (tag2.hasKey("ControllerY"))
                tag.setInteger("ControllerY", tag2.getInteger("ControllerY"));

            if (tag2.hasKey("ControllerZ"))
                tag.setInteger("ControllerZ", tag2.getInteger("ControllerZ"));
        }
        return result;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(getEmulatedTool()).getParticleTexture();
    }

    private NBTTagCompound initializeDefaults(NBTTagCompound nbt) {
        if (nbt == null)
            nbt = new NBTTagCompound();
        if (!nbt.hasKey("ViewType"))
            nbt.setInteger("ViewType", 0);
        if (!nbt.hasKey("SortingDirection"))
            nbt.setInteger("SortingDirection", 1);
        if (!nbt.hasKey("SortingType"))
            nbt.setInteger("SortingType", 0);
        if (!nbt.hasKey("SearchBoxMode"))
            nbt.setInteger("SearchBoxMode", 0);
        return nbt;
    }

    public boolean isModuleTagSet(NBTTagCompound nbt) {
        if (nbt == null)
            return false;

        return nbt.hasKey("ControllerY")
                && nbt.hasKey("ControllerY")
                && nbt.hasKey("ControllerZ")
                && nbt.hasKey("DimensionID");
    }

    public boolean isModuleTagValid(NBTTagCompound nbt) {
        if (nbt == null)
            return false;

        return nbt.hasKey("ViewType")
                && nbt.hasKey("SortingDirection")
                && nbt.hasKey("SortingType")
                && nbt.hasKey("SearchBoxMode");
    }
}
