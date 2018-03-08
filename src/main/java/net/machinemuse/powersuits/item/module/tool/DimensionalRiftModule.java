package net.machinemuse.powersuits.item.module.tool;


import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.constants.NuminaModuleConstants;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MPSTeleporter;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
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

/**
 * Created by Eximius88 on 2/3/14.
 */
public class DimensionalRiftModule extends PowerModuleBase implements IRightClickModule {
    public static final String DIMENSIONAL_RIFT_ENERGY_GENERATION = "Energy Consumption";
    public static final String DIMENSIONAL_RIFT_HEAT_GENERATION = "Heat Generation";

    public DimensionalRiftModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(DIMENSIONAL_RIFT_HEAT_GENERATION, 55);
        addBasePropertyDouble(DIMENSIONAL_RIFT_ENERGY_GENERATION, 20000);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        this.defaultTag.setBoolean(NuminaModuleConstants.ONLINE, false);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if ((playerIn.getRidingEntity() == null) && (playerIn.getPassengers().isEmpty()) && ((playerIn instanceof EntityPlayerMP))) {
            EntityPlayerMP player = (EntityPlayerMP) playerIn;
            if (player.dimension != -1) {
                player.setLocationAndAngles(0.5D, player.posY, 0.5D, player.rotationYaw, player.rotationPitch);
                player.mcServer.getPlayerList().transferPlayerToDimension(player, -1, new MPSTeleporter(player.mcServer.getWorld(-1)));
                ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, DIMENSIONAL_RIFT_ENERGY_GENERATION));
                MuseHeatUtils.heatPlayerLegacy(player, ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, DIMENSIONAL_RIFT_HEAT_GENERATION));
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
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, DIMENSIONAL_RIFT_ENERGY_GENERATION));
            MuseHeatUtils.heatPlayerLegacy(player, ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, DIMENSIONAL_RIFT_HEAT_GENERATION));
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
}