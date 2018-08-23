package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by User: Andrew2448
 * 7:13 PM 4/21/13
 */
public class LeafBlowerModule extends PowerModuleBase implements IRightClickModule {
    public LeafBlowerModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.IRON_INGOT, 3));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyDouble(MPSModuleConstants.LEAF_BLOWER_ENERGY_CONSUMPTION, 100, "J");
        addBasePropertyDouble(MPSModuleConstants.LEAF_BLOWER_RADIUS, 1, "m");
        addIntTradeoffProperty(MPSModuleConstants.LEAF_BLOWER_RADIUS, MPSModuleConstants.LEAF_BLOWER_RADIUS, 8, "m", 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_LEAF_BLOWER__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        int radius = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LEAF_BLOWER_RADIUS);
        if (useBlower(radius, itemStackIn, playerIn, worldIn, playerIn.getPosition()))
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);

        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    private boolean useBlower(int radius, ItemStack itemStack, EntityPlayer player, World world, BlockPos pos) {
        int totalEnergyDrain = 0;
        BlockPos newPos;
        for (int i = pos.getX() - radius; i < pos.getX() + radius ; i++) {
            for (int j = pos.getY() - radius; j < pos.getY() + radius; j++) {
                for (int k = pos.getZ() - radius; k < pos.getZ() + radius; k++) {
                    newPos = new BlockPos(i, j, k);
                    if (ToolHelpers.blockCheckAndHarvest(player, world, newPos)) {
                        totalEnergyDrain += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.LEAF_BLOWER_ENERGY_CONSUMPTION);
                    }
                }
            }
        }
        ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
        return true;
    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.leafBlower;
    }
}
