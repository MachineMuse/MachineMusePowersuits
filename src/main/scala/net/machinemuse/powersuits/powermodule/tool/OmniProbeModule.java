package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

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
    private ItemStack teMultimeter;

    public OmniProbeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
        ItemStack tHighest = new ItemStack(Items.COMPARATOR);

        /* Project Red seems to do something odd with its debugger. Will have to look into this. */
        // if (Loader.isModLoaded("ProjRed|Core")) {
        //         prDebugger = GameRegistry.findItemStack("ProjRed|Core", "projectred.core.wiredebugger", 1);
        //         tHighest = prDebugger;
        // }

        if (ModCompatibility.isMFRLoaded()) {
            rednetMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "rednet.meter")), 1);
            tHighest = rednetMeter;
        }

        if (ModCompatibility.isRailcraftLoaded()) {
                rcMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("Railcraft", "tool.electric.meter")), 1);
                tHighest = rcMeter;
        }

        /* Will be added when ThermalExpansion's new conduit mod is released */
        // if (ModCompatibility.isThermalExpansionLoaded) {
        //     teMultimeter = GameRegistry.findItemStack("ThermalExpansion", "multimeter", 1);
        //     tHighest = teMultimeter
        // }

        if (ModCompatibility.isEnderIOLoaded()) {
                conduitProbe = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("EnderIO", "itemConduitProbe")), 1);
                tHighest = conduitProbe;
        }
        addInstallCost(tHighest);
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

    @Override
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, item);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        int block = Block.getIdFromBlock(world.getBlockState(pos).getBlock());

    /* Project Red seems to do something odd with its debugger. Will have to look into this. */
    // if (Loader.isModLoaded("ProjRed|Transmission")) {
      //    if ( world.getBlock(x, y, z) instanceof TWireCommons) {
    //
      //    }
    // }

    if (Loader.isModLoaded("MineFactoryReloaded")) {
            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "cable.redstone"))))
                    return rednetMeter.getItem().onItemUseFirst(itemStack, player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND) == EnumActionResult.PASS;
    }

    if (Loader.isModLoaded("Railcraft")) {
        if ((block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.alpha")))) ||
                (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.track")))) ||
                (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.epsilon")))) ||
                (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.delta"))))) {
                    return rcMeter.getItem().onItemUseFirst(itemStack, player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
            }
    }

    //  if (ModCompatability.isThermalExpansionLoaded()) {
    //     if (block == Block.getIdFromBlock(GameRegistry.findBlock("ThermalExpansion", .... ))) {
    //    return multiMeter.getItem().onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    //     }
    //  }

    if (Loader.isModLoaded("EnderIO")) {
            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("EnderIO", "blockConduitBundle")))) {
                    return conduitProbe.getItem().onItemUse(itemStack, player, world, pos, EnumHand.MAIN_HAND, side, hitX, hitY, hitZ) == EnumActionResult.PASS;
            }
    }
    return false;
}

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
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
    public TextureAtlasSprite getIcon(ItemStack item) {
        return super.getIcon(item);
    }

    @Override
    public String getUnlocalizedName() {
        return "omniProbe";
    }

    @Override
    public String getDescription() {
        return "A prototype multi-use probe integrated into your power tool.";
    }

    public float minF(float a, float b) {
        return a < b ? a : b;
    }

}
