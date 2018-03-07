package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
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


public class AxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String AXE_ENERGY_CONSUMPTION = "Axe Energy Consumption";
    public static final String AXE_HARVEST_SPEED = "Axe Harvest Speed";
    public static final String AXE_SEARCH_RADIUS = "Axe Search Radius";
    private static final ItemStack emulatedTool = new ItemStack(Items.IRON_AXE);

    public AxeModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyInt(AXE_ENERGY_CONSUMPTION, 20, "RF");
        addBasePropertyDouble(AXE_HARVEST_SPEED, 8, "x");
        addTradeoffPropertyDouble("Overclock", AXE_ENERGY_CONSUMPTION, 950);
        addTradeoffPropertyDouble("Overclock", AXE_HARVEST_SPEED, 22);
        // Removed until further research can be done!
//        addTradeoffPropertyDouble("Radius", AXE_ENERGY_CONSUMPTION, 1000);
//        addTradeoffPropertyDouble("Radius", AXE_SEARCH_RADIUS, 3);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.getInstance().computeModularPropertyDouble(stack, AXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.getInstance().computeModularPropertyInteger(stack, AXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.getInstance().computeModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), AXE_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }
}