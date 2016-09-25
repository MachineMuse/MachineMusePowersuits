package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by User: Andrew2448
 * 10:48 PM 6/11/13
 */
public class FlintAndSteelModule extends PowerModuleBase implements IRightClickModule {

    public static final String MODULE_FLINT_AND_STEEL = "Flint and Steel";
    public static final String IGNITION_ENERGY_CONSUMPTION = "Ignition Energy Consumption";
    public ItemStack fas = new ItemStack(Items.FLINT_AND_STEEL);
    Random ran = new Random();

    public FlintAndSteelModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addInstallCost(fas);
        addBaseProperty(IGNITION_ENERGY_CONSUMPTION, 1000, "J");
    }


    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(fas).getParticleTexture();
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_FLINT_AND_STEEL;
    }

    @Override
    public String getUnlocalizedName() {
        return "flintAndSteel";
    }

    @Override
    public String getDescription() {
        return "A portable igniter that creates fire through the power of energy.";
    }

    @Override
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, item);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        double energyConsumption = ModuleManager.computeModularProperty(itemStack, IGNITION_ENERGY_CONSUMPTION);
        if (energyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
            pos = pos.add(/* X */ (facing == EnumFacing.EAST ? 1 : facing == EnumFacing.WEST ? -1 : 0),
                        /* Y */ (facing == EnumFacing.UP ? 1 : facing == EnumFacing.DOWN ? -1 : 0),
                        /* Z */(facing == EnumFacing.SOUTH ? 1 : facing == EnumFacing.NORTH ? -1 : 0));

            if (player.canPlayerEdit(pos, facing, itemStack)) {
                Block clickedBlock = world.getBlockState(pos).getBlock();
                if (clickedBlock == Blocks.AIR) {
                    ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
                    world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, ran.nextFloat() * 0.4F + 0.8F, false);
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
            }
        }
        return EnumActionResult.SUCCESS;
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
        return EnumAction.BOW;
    }
}
