package net.machinemuse.powersuits.powermodule.tool;


import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if ((playerIn.getRidingEntity() == null) && (playerIn.getPassengers().isEmpty()) && ((playerIn instanceof EntityPlayerMP))) {
            EntityPlayerMP player = (EntityPlayerMP) playerIn;
            if (player.dimension != -1) {
                player.setLocationAndAngles(0.5D, player.posY, 0.5D, player.rotationYaw, player.rotationPitch);
                player.mcServer.getPlayerList().transferPlayerToDimension(player, -1, new MPSTeleporter(player.mcServer.getWorld(-1)));
                ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStackIn, DIMENSIONAL_RIFT_ENERGY_GENERATION));
                MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(itemStackIn, DIMENSIONAL_RIFT_HEAT_GENERATION));
            } else if (player.dimension == -1 || player.dimension == 1)
                player.setLocationAndAngles(0.5D, player.posY, 0.5D, player.rotationYaw, player.rotationPitch);
            player.mcServer.getPlayerList().transferPlayerToDimension(player, 0, new MPSTeleporter(player.mcServer.getWorld(0)));
            if (player.dimension == 0) {
                BlockPos coords = (player instanceof EntityPlayer) ? (player).getBedLocation(player.dimension) : null;
                if ((coords == null) || ((coords.getX() == 0) && (coords.getY() == 0) && (coords.getZ() == 0))) {
                    coords = worldIn.getSpawnPoint();
                }
                int yPos = coords.getY();
                while ((worldIn.getBlockState(new BlockPos(coords.getX(), yPos, coords.getZ())).getBlock() != Blocks.AIR) &&
                        (worldIn.getBlockState(new BlockPos(coords.getX(), yPos + 1, coords.getZ())) != Blocks.AIR)) {
                    yPos++;
                }
                (player).setPositionAndUpdate(coords.getX() + 0.5D, yPos, coords.getZ() + 0.5D);
            }
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStackIn, DIMENSIONAL_RIFT_ENERGY_GENERATION));
            MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(itemStackIn, DIMENSIONAL_RIFT_HEAT_GENERATION));
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    @Override
    public String getUnlocalizedName() {
        return "dimRiftGen";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.dimRiftGen;
    }
}
