package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class AquaAffinityModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String AQUA_AFFINITY_ENERGY_CONSUMPTION = "Underwater Energy Consumption";
    public static final String UNDERWATER_HARVEST_SPEED = "Underwater Harvest Speed";

    public AquaAffinityModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addBasePropertyInt(AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "RF");
        addBasePropertyDouble(UNDERWATER_HARVEST_SPEED, 0.2, "%");
        addTradeoffPropertyInt("Power", AQUA_AFFINITY_ENERGY_CONSUMPTION, 40);
        addTradeoffPropertyDouble("Power", UNDERWATER_HARVEST_SPEED, 0.8);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_SPECIAL;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (entityLiving.isInsideOfMaterial(Material.WATER) || !entityLiving.onGround) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving,
                    ModuleManager.getInstance().computeModularPropertyInteger(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION));
        }
        return true;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();
        if (event.getNewSpeed() > 1
                && (player.isInsideOfMaterial(Material.WATER) || !player.onGround)
                && ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.getInstance().computeModularPropertyInteger(stack, AQUA_AFFINITY_ENERGY_CONSUMPTION)) {
            event.setNewSpeed((float) (event.getNewSpeed() * 5 * ModuleManager.getInstance().computeModularPropertyDouble(stack, UNDERWATER_HARVEST_SPEED)));
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