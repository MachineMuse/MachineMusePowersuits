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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;
import java.util.Objects;


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

    private static boolean istEffectiveHarvestTool(Block block, int metadata)
    {
        ItemStack emulatedTool = new ItemStack(Items.iron_axe);

        if (emulatedTool.getItem().canHarvestBlock(block, emulatedTool))
            return true;

        String effectiveTool = block.getHarvestTool(metadata);

        // some blocks like stairs do no not have a tool assigned to them
        if (effectiveTool == null)
        {
            {
                if (emulatedTool.func_150997_a/*getStrVsBlock*/(block) >= ((ItemTool) emulatedTool.getItem()).func_150913_i/*getToolMaterial*/().getEfficiencyOnProperMaterial())
                {
                    return true;
                }
            }
        }
        return (Objects.equals(effectiveTool, "axe"));
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (istEffectiveHarvestTool(block, meta)) {
            return ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION);
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        if (canHarvestBlock(stack, block, meta, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), AXE_HARVEST_SPEED);
    }
}
