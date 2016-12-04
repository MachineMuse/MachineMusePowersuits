package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ChiselModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_CHISEL = "Chisel";
    public static final String CHISEL_HARVEST_SPEED = "CHISEL Harvest Speed";
    public static final String CHISEL_ENERGY_CONSUMPTION = "CHISEL Energy Consumption";

    // Fixme put actual item.
    private static final ItemStack emulatedTool = new ItemStack(
            Item.REGISTRY.getObject(new ResourceLocation("ic2", "electric_treetap")), 1);

    public ChiselModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(GameRegistry.findItem("minecraft", "obsidian"), 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(CHISEL_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(CHISEL_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", CHISEL_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", CHISEL_HARVEST_SPEED, 22);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_CHISEL;
    }

    @Override
    public String getUnlocalizedName() {
        return "chisel";
    }

    @Override
    public String getDescription() {
        return "This won't let you chisel blocks, but it will at least let you harvest them.";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, CHISEL_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.computeModularProperty(stack, CHISEL_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.computeModularProperty(event.getEntityPlayer().inventory.getCurrentItem(), CHISEL_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool; // FIXME TOO!!
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return super.getIcon(item); // FIXME!!!
    }
}