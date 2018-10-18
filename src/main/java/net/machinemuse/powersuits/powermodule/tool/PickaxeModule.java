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


public class PickaxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_PICKAXE = "Pickaxe";
    public static final String PICKAXE_HARVEST_SPEED = "Pickaxe Harvest Speed";
    public static final String PICKAXE_ENERGY_CONSUMPTION = "Pickaxe Energy Consumption";

    public PickaxeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.iron_ingot, 3));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(PICKAXE_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(PICKAXE_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", PICKAXE_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", PICKAXE_HARVEST_SPEED, 22);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_PICKAXE;
    }

    @Override
    public String getUnlocalizedName() { return "pickaxe";
    }

    @Override
    public String getDescription() {
        return "Picks are good for harder materials like stone and ore.";
    }

    @Override
    public String getTextureFile() {
        return "toolpick";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        return harvestCheck(stack, block, meta, player);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x,y,z);
        if (canHarvestBlock(stack, block, meta, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), PICKAXE_HARVEST_SPEED);
    }

    private static boolean istEffectiveHarvestTool(Block block, int metadata) {
        ItemStack emulatedTool = new ItemStack(Items.iron_pickaxe);
        String effectiveHarvestTool = block.getHarvestTool(metadata);
        if (Objects.equals(effectiveHarvestTool, "pickaxe")) {
            return block.getHarvestLevel(metadata) <= 2; // higher than 2 requires better then iron
        }

        // some blocks like stairs do no not have a tool assigned to them
        if (effectiveHarvestTool == null) {
            return emulatedTool.func_150997_a/*getStrVsBlock*/(block) >= ((ItemTool) emulatedTool.getItem()).func_150913_i/*getToolMaterial*/().getEfficiencyOnProperMaterial();
        }
        return false;
    }

    public static boolean harvestCheck(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (istEffectiveHarvestTool(block, meta)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }
}
