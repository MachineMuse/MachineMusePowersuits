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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class ShovelModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_SHOVEL = "Shovel";
    public static final ItemStack ironShovel = new ItemStack(Item.shovelIron);
    public static final String SHOVEL_HARVEST_SPEED = "Shovel Harvest Speed";
    public static final String SHOVEL_ENERGY_CONSUMPTION = "Shovel Energy Consumption";

    public ShovelModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Item.ingotIron, 3));
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
    public String getName() {
        return MODULE_SHOVEL;
    }

    @Override
    public String getDescription() {
        return "Shovels are good for soft materials like dirt and sand.";
    }

    @Override
    public String getTextureFile() {
        return "toolshovel";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (ironShovel.canHarvestBlock(block) || ForgeHooks.canToolHarvestBlock(block, meta, ironShovel)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityPlayer player) {
        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, SHOVEL_ENERGY_CONSUMPTION));
        return true;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), SHOVEL_HARVEST_SPEED);
    }
}
