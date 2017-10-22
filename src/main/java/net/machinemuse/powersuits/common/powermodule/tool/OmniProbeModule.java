package net.machinemuse.powersuits.common.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

//import mrtjp.projectred.transmission.bundledwires.TWireCommons;

/**
 * Created by User: Korynkai
 * 6:30 PM 2014-11-17
 *
 * TODO: Fix ProjectRed (may require PR to ProjectRed)
 */
public class OmniProbeModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    public static final String MODULE_OMNIPROBE = "Prototype OmniProbe";
    private ItemStack conduitProbe;
    private ItemStack rednetMeter;
    private ItemStack cpmPSD;
    private ItemStack rcMeter;
    private ItemStack prDebugger;
//    private ItemStack teMultimeter;

    public OmniProbeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
        ItemStack tHighest = new ItemStack(Items.COMPARATOR);

        if (ModCompatibility.isMFRLoaded()) {
            rednetMeter = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "rednet.meter")), 1);
            tHighest = rednetMeter;
        }

        if (ModCompatibility.isRailcraftLoaded()) {
            rcMeter = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("Railcraft", "tool.electric.meter")), 1);
            tHighest = rcMeter;
        }

        /* Will be added when ThermalExpansion's new conduit mod is released */
        // if (ModCompatibility.isThermalExpansionLoaded) {
        //     teMultimeter = GameRegistry.findItemStack("ThermalExpansion", "multimeter", 1);
        //     tHighest = teMultimeter
        // }

        if (ModCompatibility.isEnderIOLoaded()) {
            conduitProbe = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("EnderIO", "itemConduitProbe")), 1);
            tHighest = conduitProbe;
        }
        addInstallCost(tHighest);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_OMNIPROBE;
    }

    @Override
    public String getUnlocalizedName() {
        return "omniProbe";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        int block = Block.getIdFromBlock(world.getBlockState(pos).getBlock());

        if (Loader.isModLoaded("MineFactoryReloaded")) {
            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "cable.redstone"))))
                return rednetMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
        }

        if (Loader.isModLoaded("Railcraft")) {
            if ((block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.alpha")))) ||
                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.track")))) ||
                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.epsilon")))) ||
                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.delta"))))) {
                return rcMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
            }
        }

        if (Loader.isModLoaded("EnderIO")) {
            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("EnderIO", "blockConduitBundle")))) {
                return conduitProbe.getItem().onItemUse(player, world, pos, EnumHand.MAIN_HAND, side, hitX, hitY, hitZ);
            }
        }
        return EnumActionResult.PASS;
    }
    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (!MuseItemUtils.getEIOFacadeTransparency(item)) {
            MuseItemUtils.setEIONoCompete(item, MODULE_OMNIPROBE);
            MuseItemUtils.setEIOFacadeTransparency(item, true);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if ((MuseItemUtils.getEIONoCompete(item) != null) && (!MuseItemUtils.getEIONoCompete(item).isEmpty())) {
            if (MuseItemUtils.getEIONoCompete(item).equals(MODULE_OMNIPROBE)) {
                MuseItemUtils.setEIONoCompete(item, "");
                if (MuseItemUtils.getEIOFacadeTransparency(item)) {
                    MuseItemUtils.setEIOFacadeTransparency(item, false);
                }
            }
        } else {
            if (MuseItemUtils.getEIOFacadeTransparency(item)) {
                MuseItemUtils.setEIOFacadeTransparency(item, false);
            }
        }
    }

    public float minF(float a, float b) {
        return a < b ? a : b;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.omniProbe;
    }
}
