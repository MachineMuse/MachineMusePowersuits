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
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class DiamondPickUpgradeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_DIAMOND_PICK_UPGRADE = "Diamond Drill Upgrade";

    public DiamondPickUpgradeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addInstallCost(new ItemStack(Items.diamond, 3));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_DIAMOND_PICK_UPGRADE;
    }

    @Override
    public String getUnlocalizedName() { return "diamondPickUpgrade";
    }

    @Override
    public String getDescription() {
        return "Add diamonds to allow your pickaxe module to mine Obsidian. *REQUIRES PICKAXE MODULE TO WORK*";
    }

    @Override
    public String getTextureFile() {
        return "diamondupgrade1";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (!Items.iron_pickaxe.canHarvestBlock(block, stack)) {
            if (Items.diamond_pickaxe.canHarvestBlock(block, stack)) {
                return ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, PickaxeModule.PICKAXE_ENERGY_CONSUMPTION);
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x,y,z);
        if (canHarvestBlock(stack, block, meta, player) && !PickaxeModule.harvestCheck(stack, block, meta, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, PickaxeModule.PICKAXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed = (float) ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(),
                PickaxeModule.PICKAXE_HARVEST_SPEED);
    }
}