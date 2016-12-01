package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
    public String getDescription() {
        return "A portable igniter that creates fire through the power of energy.";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return null;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        double energyConsumption = ModuleManager.computeModularProperty(itemStack, IGNITION_ENERGY_CONSUMPTION);
//        if (energyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
//            x += (side == 5 ? 1 : side == 4 ? -1 : 0);
//            y += (side == 1 ? 1 : side == 0 ? -1 : 0);
//            z += (side == 3 ? 1 : side == 2 ? -1 : 0);
//
//            if (player.canPlayerEdit(x, y, z, side, itemStack)) {
//                Block clickedBlock = world.getBlock(x, y, z);
//
//                if (clickedBlock == Blocks.air) {
//                    ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
//                    world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, ran.nextFloat() * 0.4F + 0.8F);
//                    world.setBlock(x, y, z, Blocks.fire);
//                }
//            }
//        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }
}