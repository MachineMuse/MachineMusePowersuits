package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
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

public class ChiselModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String CHISEL_HARVEST_SPEED = "CHISEL Harvest Speed";
    public static final String CHISEL_ENERGY_CONSUMPTION = "CHISEL Energy Consumption";

    // TODO Fixme put actual item.
    private static final ItemStack emulatedTool = new ItemStack(
            Item.REGISTRY.getObject(new ResourceLocation("chisel", "chisel_iron")), 1);
    public ChiselModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        //        addInstallCost(new ItemStack(GameRegistry.findItem("minecraft", "obsidian"), 2)); // depreciated, left for now for reference
        addInstallCost(new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyDouble(CHISEL_ENERGY_CONSUMPTION, 50, "J");
        addBasePropertyDouble(CHISEL_HARVEST_SPEED, 8, "x");
        addTradeoffPropertyInt("Overclock", CHISEL_ENERGY_CONSUMPTION, 380);
        addTradeoffPropertyDouble("Overclock", CHISEL_HARVEST_SPEED, 22);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.getInstance().computeModularPropertyInteger(stack, CHISEL_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.getInstance().computeModularPropertyInteger(stack, CHISEL_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.getInstance().computeModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), CHISEL_HARVEST_SPEED)));
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