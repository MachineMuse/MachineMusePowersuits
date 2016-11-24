package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        ItemStack tHighest = GameRegistry.findItemStack("minecraft", "comparator", 1);

        /* Project Red seems to do something odd with its debugger. Will have to look into this. */
        // if (Loader.isModLoaded("ProjRed|Core")) {
        //         prDebugger = GameRegistry.findItemStack("ProjRed|Core", "projectred.core.wiredebugger", 1);
        //         tHighest = prDebugger;
        // }

        if (ModCompatibility.isMFRLoaded()) {
            rednetMeter = GameRegistry.findItemStack("MineFactoryReloaded", "rednet.meter", 1);
            tHighest = rednetMeter;
        }

        if (ModCompatibility.isRailcraftLoaded()) {
            rcMeter = GameRegistry.findItemStack("Railcraft", "tool.electric.meter", 1);
            tHighest = rcMeter;
        }

        /* Will be added when ThermalExpansion's new conduit mod is released */
        // if (ModCompatibility.isThermalExpansionLoaded) {
        //     teMultimeter = GameRegistry.findItemStack("ThermalExpansion", "multimeter", 1);
        //     tHighest = teMultimeter
        // }

        if (ModCompatibility.isEnderIOLoaded()) {
            conduitProbe = GameRegistry.findItemStack("EnderIO", "itemConduitProbe", 1);
            tHighest = conduitProbe;
        }
        addInstallCost(tHighest);
    }

    @Override
    public String getTextureFile() {
        return "omniprobe";
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
    public String getDescription() {
        return "A prototype multi-use probe integrated into your power tool.";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return null;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

        int block = Block.getIdFromBlock(world.getBlock(x, y, z));

        /* Project Red seems to do something odd with its debugger. Will have to look into this. */
        // if (Loader.isModLoaded("ProjRed|Transmission")) {
        //    if ( world.getBlock(x, y, z) instanceof TWireCommons) {
        //
        //    }
        // }

        if (Loader.isModLoaded("MineFactoryReloaded")) {
            if (block == Block.getIdFromBlock(GameRegistry.findBlock("MineFactoryReloaded", "cable.redstone")))
                return rednetMeter.getItem().onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }

        if (Loader.isModLoaded("Railcraft")) {
            if ((block == Block.getIdFromBlock(GameRegistry.findBlock("Railcraft", "tile.railcraft.machine.alpha"))) ||
                    (block == Block.getIdFromBlock(GameRegistry.findBlock("Railcraft", "tile.railcraft.track"))) ||
                    (block == Block.getIdFromBlock(GameRegistry.findBlock("Railcraft", "tile.railcraft.machine.epsilon"))) ||
                    (block == Block.getIdFromBlock(GameRegistry.findBlock("Railcraft", "tile.railcraft.machine.delta")))) {
                return rcMeter.getItem().onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        //  if (ModCompatability.isThermalExpansionLoaded()) {
        //     if (block == Block.getIdFromBlock(GameRegistry.findBlock("ThermalExpansion", .... ))) {
        //    return multiMeter.getItem().onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        //     }
        //  }

        if (Loader.isModLoaded("EnderIO")) {
            if (block == Block.getIdFromBlock(GameRegistry.findBlock("EnderIO", "blockConduitBundle"))) {
                return conduitProbe.getItem().onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }
        return false;
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

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return super.getIcon(item); // FIXME!!!
    }
}