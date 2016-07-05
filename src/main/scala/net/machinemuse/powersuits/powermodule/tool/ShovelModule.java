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

import java.util.*;

public class ShovelModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_SHOVEL = "Shovel";
    public static final String SHOVEL_HARVEST_SPEED = "Shovel Harvest Speed";
    public static final String SHOVEL_ENERGY_CONSUMPTION = "Shovel Energy Consumption";

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
    public String getUnlocalizedName() { return "shovel";
    }

    @Override
    public String getDescription() {
        return "Shovels are good for soft materials like dirt and sand.";
    }


    private boolean istEffectiveHarvestTool(IBlockState state)
    {
        ItemStack emulatedTool = new ItemStack(Items.IRON_SHOVEL);
        Block block = state.getBlock();

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
        return effectiveTool == "shovel";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (istEffectiveHarvestTool(state)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player) {
        if (canHarvestBlock(stack, pos, state, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.computeModularProperty(event.getEntityPlayer().getHeldItemMainhand(), SHOVEL_HARVEST_SPEED)));
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.shovel;
    }
}
