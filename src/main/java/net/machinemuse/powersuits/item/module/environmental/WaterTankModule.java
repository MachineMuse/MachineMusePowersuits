package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.module.helpers.FluidUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;

/**
 * Created by User: Andrew2448
 * 4:35 PM 6/21/13
 */
public class WaterTankModule extends PowerModuleBase implements IPlayerTickModule {
    public static final String WATER_TANK_SIZE = "Tank Size";
    public static final String ACTIVATION_PERCENT = "Heat Activation Percent";
    final ItemStack bucketWater = new ItemStack(Items.WATER_BUCKET);

    public WaterTankModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TORSOONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(WATER_TANK_SIZE, 200);
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 1000);
        addBasePropertyDouble(ACTIVATION_PERCENT, 0.5);
        addTradeoffPropertyDouble("Activation Percent", ACTIVATION_PERCENT, 0.5, "%");
        addTradeoffPropertyDouble("Tank Size", WATER_TANK_SIZE, 800, " buckets");
        addTradeoffPropertyDouble("Tank Size", MPSModuleConstants.WEIGHT, 4000, "g");
        addInstallCost(bucketWater);
        addInstallCost(new ItemStack(Blocks.GLASS, 8));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(bucketWater).getParticleTexture();
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (FluidUtils.getWaterLevel(item) > ModuleManager.getInstance().computeModularPropertyDouble(item, WATER_TANK_SIZE)) {
            FluidUtils.setWaterLevel(item, ModuleManager.getInstance().computeModularPropertyDouble(item, WATER_TANK_SIZE));
        }

        // Fill tank if player is in water
        Block block = player.world.getBlockState(player.getPosition()).getBlock();
        if (((block == Blocks.WATER) || block == Blocks.FLOWING_WATER) && FluidUtils.getWaterLevel(item) < ModuleManager.getInstance().computeModularPropertyDouble(item, WATER_TANK_SIZE)) {
            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) + 1);
        }

        // Fill tank if raining
        int xCoord = MathHelper.floor(player.posX);
        int zCoord = MathHelper.floor(player.posZ);
        boolean isRaining = (player.world.getBiomeForCoordsBody(player.getPosition()).getRainfall() > 0) && (player.world.isRaining() || player.world.isThundering());
        if (isRaining && player.world.canBlockSeeSky(player.getPosition().add(0,1,0))
                && (player.world.getTotalWorldTime() % 5) == 0 && FluidUtils.getWaterLevel(item) < ModuleManager.getInstance().computeModularPropertyDouble(item, WATER_TANK_SIZE)) {
            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) + 1);
        }

        // Apply cooling
        double currentHeat = MuseHeatUtils.getPlayerHeatLegacy(player);
        double maxHeat = MuseHeatUtils.getMaxHeatLegacy(player);
        if ((currentHeat / maxHeat) >= ModuleManager.getInstance().computeModularPropertyDouble(item, ACTIVATION_PERCENT) && FluidUtils.getWaterLevel(item) > 0) {
            MuseHeatUtils.coolPlayerLegacy(player, 1);
            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) - 1);
            for (int i = 0; i < 4; i++) {
                player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX, player.posY + 0.5, player.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}