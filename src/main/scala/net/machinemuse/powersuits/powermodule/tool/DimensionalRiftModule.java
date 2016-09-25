package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class DimensionalRiftModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_DIMENSIONAL_RIFT = "Dimensional Tear Generator";
    public static final String DIMENSIONAL_RIFT_ENERGY_GENERATION = "Energy Consumption";
    public static final String DIMENSIONAL_RIFT_HEAT_GENERATION = "Heat Generation";

    public DimensionalRiftModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(DIMENSIONAL_RIFT_HEAT_GENERATION, 55);
        addBaseProperty(DIMENSIONAL_RIFT_ENERGY_GENERATION, 20000);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, false);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_DIMENSIONAL_RIFT;
    }

    @Override
    public String getUnlocalizedName() {
        return "dimRiftGen";
    }

    @Override
    public String getDescription() {
        return "Generate a tear in the space-time continuum that will teleport the player to its relative coordinates in the nether or overworld.";
    }

    @Override
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand) {
        if ((player.getRidingEntity() == null) && (player.getPassengers().isEmpty()) && ((player instanceof EntityPlayerMP))) {
            EntityPlayerMP thePlayer = (EntityPlayerMP) player;
            if (thePlayer.dimension != -1) {
                thePlayer.setLocationAndAngles(0.5D, thePlayer.posY, 0.5D, thePlayer.rotationYaw, thePlayer.rotationPitch);
                thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, -1, new MPSTeleporter(thePlayer.mcServer.worldServerForDimension(-1)));
                ElectricItemUtils.drainPlayerEnergy(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_ENERGY_GENERATION));
                MuseHeatUtils.heatPlayer(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_HEAT_GENERATION));
            } else if (thePlayer.dimension == -1 || thePlayer.dimension == 1)
                thePlayer.setLocationAndAngles(0.5D, thePlayer.posY, 0.5D, thePlayer.rotationYaw, thePlayer.rotationPitch);
            thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new MPSTeleporter(thePlayer.mcServer.worldServerForDimension(0)));
            if (thePlayer.dimension == 0) {
                BlockPos coords = (thePlayer instanceof EntityPlayer) ? (thePlayer).getBedLocation(thePlayer.dimension) : null;
                if ((coords == null) || ((coords.getX() == 0) && (coords.getY() == 0) && (coords.getZ() == 0))) {
                    coords = world.getSpawnPoint();
                }
                int yPos = coords.getY();
                while ((world.getBlockState(new BlockPos(coords.getX(), yPos, coords.getZ())).getBlock() != Blocks.AIR) &&
                        (world.getBlockState(new BlockPos(coords.getX(), yPos + 1, coords.getZ())) != Blocks.AIR)) {
                    yPos++;
                }
                (thePlayer).setPositionAndUpdate(coords.getX() + 0.5D, yPos, coords.getZ() + 0.5D);
            }

            ElectricItemUtils.drainPlayerEnergy(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_ENERGY_GENERATION));
            MuseHeatUtils.heatPlayer(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_HEAT_GENERATION));
        }
        return ActionResult.newResult(EnumActionResult.PASS, item);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
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
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.dimRiftGen;
    }
}
