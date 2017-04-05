package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
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

import java.util.List;

public class ShovelModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_SHOVEL = "Shovel";
    public static final String SHOVEL_HARVEST_SPEED = "Shovel Harvest Speed";
    public static final String SHOVEL_ENERGY_CONSUMPTION = "Shovel Energy Consumption";
    private static final ItemStack emulatedTool = new ItemStack(Items.IRON_SHOVEL);

    public ShovelModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(SHOVEL_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(SHOVEL_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", SHOVEL_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", SHOVEL_HARVEST_SPEED, 22);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_SHOVEL;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.computeModularProperty(event.getEntityPlayer().inventory.getCurrentItem(), SHOVEL_HARVEST_SPEED)));
    }

    @Override
    public String getUnlocalizedName() {
        return "shovel";
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.shovel;
    }
}