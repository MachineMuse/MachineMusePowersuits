package net.machinemuse.powersuits.common.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_AXE;


public class AxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {

    public static final String AXE_ENERGY_CONSUMPTION = "Axe Energy Consumption";
    public static final String AXE_HARVEST_SPEED = "Axe Harvest Speed";
    public static final String AXE_SEARCH_RADIUS = "Axe Search Radius";
    private static final ItemStack emulatedTool = new ItemStack(Items.IRON_AXE);


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
    public String getUnlocalizedName() {
        return "axe";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.computeModularProperty(event.getEntityPlayer().inventory.getCurrentItem(), AXE_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.axe;
    }
}