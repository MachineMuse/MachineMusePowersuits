package net.machinemuse_old.powersuits.powermodule.tool;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.ElectricItemUtils;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class ChiselModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_CHISEL = "Chisel";
    public static final String CHISEL_HARVEST_SPEED = "CHISEL Harvest Speed";
    public static final String CHISEL_ENERGY_CONSUMPTION = "CHISEL Energy Consumption";

    // TODO Fixme put actual item.
    private static final ItemStack emulatedTool = new ItemStack(
            Item.REGISTRY.getObject(new ResourceLocation("chisel", "chisel_iron")), 1);
    public ChiselModule(List<IModularItem> validItems) {
        super(validItems);
        //        addInstallCost(new ItemStack(GameRegistry.findItem("minecraft", "obsidian"), 2)); // depreciated, left for now for reference
        addInstallCost(new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 2));
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
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(emulatedTool).getParticleTexture();
    }
}