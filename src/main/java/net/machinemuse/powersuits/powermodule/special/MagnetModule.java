package net.machinemuse.powersuits.powermodule.special;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MagnetModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public MagnetModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.magnet, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addBasePropertyDouble(MPSModuleConstants.MAGNET_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble("Power", MPSModuleConstants.MAGNET_ENERGY_CONSUMPTION, 2000);
        addBasePropertyDouble(MPSModuleConstants.MAGNET_RADIUS, 5);
        addTradeoffPropertyDouble("Power", MPSModuleConstants.MAGNET_RADIUS, 10);

        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty(MPSModuleConstants.MAGNET_RADIUS, MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);

    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_MAGNET__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack stack) {
        if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.MAGNET_ENERGY_CONSUMPTION)) {
            if ((player.world.getTotalWorldTime() % 20) == 0) {
                ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.MAGNET_ENERGY_CONSUMPTION));
            }
            int range = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.MAGNET_RADIUS);
            World world = player.world;
            AxisAlignedBB bounds = player.getEntityBoundingBox().expand(range, range, range);
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                bounds.expand(0.2000000029802322D, 0.2000000029802322D, 0.2000000029802322D);
                if (stack.getItemDamage() >> 1 >= 7) {
                    List<EntityArrow> arrows = world.getEntitiesWithinAABB(EntityArrow.class, bounds);
                    for (EntityArrow arrow : arrows) {
                        if ((arrow.pickupStatus == EntityArrow.PickupStatus.ALLOWED) && (world.rand.nextInt(6) == 0)) {
                            EntityItem replacement = new EntityItem(world, arrow.posX, arrow.posY, arrow.posZ, new ItemStack(Items.ARROW));
                            world.spawnEntity(replacement);
                        }
                        world.removeEntity(arrow);
                    }
                }
            }
            List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
            for (EntityItem e : list) {
                if (!e.cannotPickup()) {
//                    getAge() >= 10
                    double x = player.posX - e.posX;
                    double y = player.posY - e.posY;
                    double z = player.posZ - e.posZ;

                    double length = Math.sqrt(x * x + y * y + z * z) * 0.75D;

                    x = x / length + player.motionX * 22.0D;
                    y = y / length + player.motionY / 22.0D;
                    z = z / length + player.motionZ * 22.0D;

                    e.motionX = x;
                    e.motionY = y;
                    e.motionZ = z;
                    e.isAirBorne = true;
                    if (e.collidedHorizontally) {
                        e.motionY += 1.0D;
                    }
                    if (world.rand.nextInt(20) == 0) {
                        float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
                        world.playSound(e.posX, e.posY, e.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.PLAYERS, 0.6F, pitch, true);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.magnet;
    }
}
