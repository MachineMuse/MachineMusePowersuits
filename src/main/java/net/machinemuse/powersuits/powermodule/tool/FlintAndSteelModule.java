package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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
    public final ItemStack fas = new ItemStack(Items.FLINT_AND_STEEL);
    final Random ran = new Random();

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
    public ActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, playerIn.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        double energyConsumption = ModuleManager.computeModularProperty(stack, IGNITION_ENERGY_CONSUMPTION);
        if (energyConsumption < ElectricItemUtils.getPlayerEnergy(playerIn)) {
            pos = pos.offset(facing);
            if (!playerIn.canPlayerEdit(pos, facing, stack)) {
                return EnumActionResult.FAIL;
            } else {
                if (worldIn.isAirBlock(pos)) {
                    ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
                    worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, ran.nextFloat() * 0.4F + 0.8F);
                    worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
                }

                stack.damageItem(1, playerIn);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }
}
