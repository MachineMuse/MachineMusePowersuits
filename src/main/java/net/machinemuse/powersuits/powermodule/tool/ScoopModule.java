package net.machinemuse.powersuits.powermodule.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

/**
 * Created by User: Sergey Popov aka Pinkbyte
 * Date: 9/08/15
 * Time: 5:53 PM
 */
public class ScoopModule extends PowerModuleBase implements IBlockBreakingModule {
    public static final String MODULE_SCOOP = "Scoop";
    public static final String SCOOP_HARVEST_SPEED = "Scoop Harvest Speed";
    public static final String SCOOP_ENERGY_CONSUMPTION = "Scoop Energy Consumption";
    public static final ItemStack scoop = GameRegistry.findItemStack("Forestry", "scoop", 1);

    public ScoopModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(scoop);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(SCOOP_ENERGY_CONSUMPTION, 2000, "J");
        addBaseProperty(SCOOP_HARVEST_SPEED, 5, "x");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_SCOOP;
    }

    @Override
    public String getUnlocalizedName() {
        return "scoop";
    }

    @Override
    public IIcon getIcon(ItemStack item) {
        return scoop.getIconIndex();
    }

    @Override
    public String getTextureFile() {
        return null;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (ForgeHooks.canToolHarvestBlock(block, meta, scoop)) {
            return ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SCOOP_ENERGY_CONSUMPTION);
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        if (canHarvestBlock(stack, block, meta, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, SCOOP_ENERGY_CONSUMPTION));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), SCOOP_HARVEST_SPEED);
    }
}
