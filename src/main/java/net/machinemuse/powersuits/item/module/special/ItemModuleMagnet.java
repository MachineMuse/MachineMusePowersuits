package net.machinemuse.powersuits.item.module.special;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleMagnet extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {
    public ItemModuleMagnet(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_SPECIAL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.magnet, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

//        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 0, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.ENERGY_CONSUMPTION, 2000);
//        addBasePropertyDouble(MPSModuleConstants.MAGNET_RADIUS, 5);
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.MAGNET_RADIUS, 10);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack stack) {
//        if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ENERGY_CONSUMPTION)) {
//            boolean isServerSide = !player.world.isRemote;
//
//            if ((player.world.getTotalWorldTime() % 20) == 0 && isServerSide) {
//                ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ENERGY_CONSUMPTION));
//            }
//            int range = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.MAGNET_RADIUS);
//            World world = player.world;
//            AxisAlignedBB bounds = player.getEntityBoundingBox().grow(range);
//
//
//            if (isServerSide) {
//                bounds.expand(0.2000000029802322D, 0.2000000029802322D, 0.2000000029802322D);
//                if (stack.getItemDamage() >> 1 >= 7) {
//                    List<EntityArrow> arrows = world.getEntitiesWithinAABB(EntityArrow.class, bounds);
//                    for (EntityArrow arrow : arrows) {
//                        if ((arrow.pickupStatus == EntityArrow.PickupStatus.ALLOWED) && (world.rand.nextInt(6) == 0)) {
//                            EntityItem replacement = new EntityItem(world, arrow.posX, arrow.posY, arrow.posZ, new ItemStack(Items.ARROW));
//                            world.spawnEntity(replacement);
//                        }
//                        world.removeEntity(arrow);
//                    }
//                }
//            }
//
//            for (EntityItem e : world.getEntitiesWithinAABB(EntityItem.class, bounds)) {
//                if (!e.isDead && !e.getItem().isEmpty() && !e.cannotPickup()) {
//                    if (isServerSide) {
//                        double x = player.posX - e.posX;
//                        double y = player.posY - e.posY;
//                        double z = player.posZ - e.posZ;
//
//                        double length = Math.sqrt(x * x + y * y + z * z) * 0.75D;
//
//                        x = x / length + player.motionX * 22.0D;
//                        y = y / length + player.motionY / 22.0D;
//                        z = z / length + player.motionZ * 22.0D;
//
//                        e.motionX = x;
//                        e.motionY = y;
//                        e.motionZ = z;
//                        e.isAirBorne = true;
//                        if (e.collidedHorizontally) {
//                            e.motionY += 1.0D;
//                        }
//                    } else if (world.rand.nextInt(20) == 0) {
//                        float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
//                        world.playSound(e.posX, e.posY, e.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.PLAYERS, 0.6F, pitch, true);
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
