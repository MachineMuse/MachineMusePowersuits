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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
    public String getUnlocalizedName() {
        return "aquaAffinity";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (entityLiving.isInsideOfMaterial(Material.WATER) || !entityLiving.onGround) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving,
                    ModuleManager.computeModularProperty(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION));
        }
        return true;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();
        if (event.getNewSpeed() > 1
                && (player.isInsideOfMaterial(Material.WATER) || !player.onGround)
                && ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION)) {
            event.setNewSpeed((float) (event.getNewSpeed() * 5 * ModuleManager.computeModularProperty(stack, UNDERWATER_HARVEST_SPEED)));
        }
    }

    @Override
    public ItemStack getEmulatedTool() {
        return null;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aquaAffinity;
    }
}