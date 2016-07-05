package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;


public class PickaxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_PICKAXE = "Pickaxe";
    public static final String PICKAXE_HARVEST_SPEED = "Pickaxe Harvest Speed";
    public static final String PICKAXE_ENERGY_CONSUMPTION = "Pickaxe Energy Consumption";

    public PickaxeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 3));
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
    public boolean canHarvestBlock(ItemStack stack, BlockPos pos, IBlockState state, EntityPlayer player) {
        return harvestCheck(stack, state, player);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player) {
        if (canHarvestBlock(stack, pos, state, player)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.computeModularProperty(event.getEntityPlayer().getHeldItemMainhand(), PICKAXE_HARVEST_SPEED)));
    }

    private static boolean istEffectiveHarvestTool(IBlockState state) {
        ItemStack emulatedTool = new ItemStack(Items.IRON_PICKAXE);
        String effectiveHarvestTool = state.getBlock().getHarvestTool(state);
        if (effectiveHarvestTool == "pickaxe") {
            return state.getBlock().getHarvestLevel(state) <= 2; // higher than 2 requires better then iron
        }

        // some blocks like stairs do no not have a tool assigned to them
        if (effectiveHarvestTool == null) {
           if (emulatedTool.getStrVsBlock(state) >= ((ItemTool) emulatedTool.getItem()).getToolMaterial().getEfficiencyOnProperMaterial())
           {
               return true;
           }
        }
        return false;
    }

    public static boolean harvestCheck(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (istEffectiveHarvestTool(state)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, PICKAXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.pickaxe;
    }
}