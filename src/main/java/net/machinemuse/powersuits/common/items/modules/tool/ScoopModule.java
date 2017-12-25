package net.machinemuse.powersuits.common.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
    public static final ItemStack emulatedTool = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("forestry", "scoop")), 1);

    public ScoopModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(emulatedTool);
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
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(emulatedTool).getParticleTexture();
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SCOOP_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (ForgeHooks.canToolHarvestBlock(worldIn, pos, emulatedTool)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.computeModularProperty(stack, SCOOP_ENERGY_CONSUMPTION));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.computeModularProperty(event.getEntityPlayer().inventory.getCurrentItem(), SCOOP_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }
}