package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import java.util.List;


public class AxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_AXE = "Axe";
    public static final String AXE_ENERGY_CONSUMPTION = "Axe Energy Consumption";
    public static final String AXE_HARVEST_SPEED = "Axe Harvest Speed";
    public static final String AXE_SEARCH_RADIUS = "Axe Search Radius";

    public AxeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(AXE_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(AXE_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", AXE_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", AXE_HARVEST_SPEED, 22);
        // Removed until further research can be done!
//        addTradeoffProperty("Radius", AXE_ENERGY_CONSUMPTION, 1000);
//        addTradeoffProperty("Radius", AXE_SEARCH_RADIUS, 3);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_AXE;
    }

    @Override
    public String getUnlocalizedName() { return "axe";
    }

    @Override
    public String getDescription() {
        return "Axes are mostly for chopping trees.";
    }

    @Override
    public String getTextureFile() {
        return "toolaxe";
    }

    private static boolean istEffectiveHarvestTool(IBlockState state)
    {
        Block block = state.getBlock();
        ItemStack emulatedTool = new ItemStack(Items.IRON_AXE);

        if (emulatedTool.getItem().canHarvestBlock(state, emulatedTool))
            return true;

        String effectiveTool = block.getHarvestTool(state);

        // some blocks like stairs do no not have a tool assigned to them
        if (effectiveTool == null)
        {
            {
                if (emulatedTool.getStrVsBlock(state) >= ((ItemTool) emulatedTool.getItem()).getToolMaterial().getEfficiencyOnProperMaterial())
                {
                    return true;
                }
            }
        }
        return effectiveTool == "axe";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (istEffectiveHarvestTool(state)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player) {
        if (canHarvestBlock(stack, pos, state, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.computeModularProperty(event.getEntityPlayer().getHeldItemMainhand(), AXE_HARVEST_SPEED)));
    }
}
