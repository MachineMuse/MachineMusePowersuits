package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class DiamondPickUpgradeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final ItemStack emulatedTool = new ItemStack(Items.DIAMOND_PICKAXE);

    public DiamondPickUpgradeModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.DIAMOND, 3));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE__DATANAME;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (!ToolHelpers.isEffectiveTool(state, PickaxeModule.emulatedTool) && ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!ForgeHooks.canToolHarvestBlock(worldIn, pos, PickaxeModule.emulatedTool) &&
                ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.PICKAXE_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.diamondPickUpgrade;
    }
}