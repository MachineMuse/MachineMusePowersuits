//package net.machinemuse.powersuits.powermodule.environmental;
//
//import net.machinemuse.numina.api.module.EnumModuleCategory;
//import net.machinemuse.numina.api.module.EnumModuleTarget;
//import net.machinemuse.numina.api.module.IPlayerTickModule;
//import net.machinemuse.numina.utils.item.MuseItemUtils;
//import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
//import net.machinemuse.powersuits.api.module.ModuleManager;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.powersuits.utils.modulehelpers.FluidUtils;
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.math.MathHelper;
//import net.machinemuse.numina.utils.heat.MuseHeatUtils;
//
///**
// * Created by User: Andrew2448
// * 4:35 PM 6/21/13
// */
//public class WaterTankModule extends PowerModuleBase implements IPlayerTickModule {
//    final ItemStack bucketWater = new ItemStack(Items.WATER_BUCKET);
//
//    public WaterTankModule(EnumModuleTarget moduleTarget) {
//        super(moduleTarget);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(),bucketWater);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(),new ItemStack(Blocks.GLASS, 8));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
//
//        addBasePropertyDouble(MPSModuleConstants.WATER_TANK_SIZE, 100);
//
//        addBasePropertyDouble(MPSModuleConstants.ACTIVATION_PERCENT, 0.5);
//        addTradeoffPropertyDouble("Activation Percent", MPSModuleConstants.ACTIVATION_PERCENT, 0.5, "%");
//
//        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
//        addIntTradeoffProperty(MPSModuleConstants.WATER_TANK_SIZE, MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(bucketWater).getParticleTexture();
//    }
//
//    @Override
//    public EnumModuleCategory getCategory() {
//        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
//    }
//
//    @Override
//    public String getDataName() {
//        return MPSModuleConstants.MODULE_WATER_TANK__DATANAME;
//    }
//
//    @Override
//    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
////        if (FluidUtils.getWaterLevel(item) > ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.WATER_TANK_SIZE)) {
////            FluidUtils.setWaterLevel(item, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.WATER_TANK_SIZE));
////        }
////
////        // Fill tank if player is in water
////        Block block = player.world.getBlockState(player.getPosition()).getBlock();
////        if (((block == Blocks.WATER) || block == Blocks.FLOWING_WATER) && FluidUtils.getWaterLevel(item) < ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.WATER_TANK_SIZE)) {
////            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) + 1);
////        }
////
////        // Fill tank if raining
//////        int xCoord = MathHelper.floor(player.posX);
//////        int zCoord = MathHelper.floor(player.posZ);
////        boolean isRaining = (player.world.getBiomeForCoordsBody(player.getPosition()).getRainfall() > 0) && (player.world.isRaining() || player.world.isThundering());
////        if (isRaining && player.world.canBlockSeeSky(player.getPosition().add(0,1,0))
////                && (player.world.getTotalWorldTime() % 5) == 0 && FluidUtils.getWaterLevel(item) < ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.WATER_TANK_SIZE)) {
////            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) + 1);
////        }
////
////        // Apply cooling
////        double currentHeat = MuseHeatUtils.getPlayerHeat(player);
////        double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
////        if ((currentHeat / maxHeat) >= ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.ACTIVATION_PERCENT) && FluidUtils.getWaterLevel(item) > 0) {
////            MuseHeatUtils.coolPlayer(player, 1);
////            FluidUtils.setWaterLevel(item, FluidUtils.getWaterLevel(item) - 1);
////            for (int i = 0; i < 4; i++) {
////                player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX, player.posY + 0.5, player.posZ, 0.0D, 0.0D, 0.0D);
////            }
////        }
//    }
//
//    @Override
//    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//    }
//}