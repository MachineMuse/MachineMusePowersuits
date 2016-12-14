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
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class AquaAffinityModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_AQUA_AFFINITY = "Aqua Affinity";
    public static final String AQUA_AFFINITY_ENERGY_CONSUMPTION = "Underwater Energy Consumption";
    public static final String UNDERWATER_HARVEST_SPEED = "Underwater Harvest Speed";

    public AquaAffinityModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addBaseProperty(AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "J");
        addBaseProperty(UNDERWATER_HARVEST_SPEED, 0.2, "%");
        addTradeoffProperty("Power", AQUA_AFFINITY_ENERGY_CONSUMPTION, 100);
        addTradeoffProperty("Power", UNDERWATER_HARVEST_SPEED, 0.8);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_AQUA_AFFINITY;
    }

    @Override
    public String getUnlocalizedName() { return "aquaAffinity";
    }

    @Override
    public String getDescription() {
        return "Reduces the speed penalty for using your tool underwater.";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityPlayer player) {
        if (player.isInsideOfMaterial(Material.water) || !player.onGround) {
            ElectricItemUtils.drainPlayerEnergy(player,
                    ModuleManager.computeModularProperty(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION));
        }
        return true;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if (event.newSpeed > 1
                && (player.isInsideOfMaterial(Material.water) || !player.onGround)
                && ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION)) {
            event.newSpeed *= 5 * ModuleManager.computeModularProperty(stack, UNDERWATER_HARVEST_SPEED);
        }
    }

    @Override
    public String getTextureFile() {
        return "aquaaffinity";
    }
}
