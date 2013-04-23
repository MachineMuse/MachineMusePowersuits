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

public class PickaxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_PICKAXE = "Pickaxe";
    public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeIron);
    public static final String PICKAXE_HARVEST_SPEED = "Pickaxe Harvest Speed";
    public static final String PICKAXE_ENERGY_CONSUMPTION = "Pickaxe Energy Consumption";

    public PickaxeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Item.ingotIron, 3));
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
    public String getName() {
        return MODULE_PICKAXE;
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
        if (ironPickaxe.canHarvestBlock(block) || ForgeHooks.canToolHarvestBlock(block, meta, ironPickaxe)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityPlayer player) {
        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION));
        return true;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), PICKAXE_HARVEST_SPEED);
    }
}
