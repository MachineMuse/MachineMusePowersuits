package net.machinemuse.powersuits.powermodule.misc;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.PropertyModifierIntLinearAdditive;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class MagnetModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_MAGNET = "Magnet";
    public static final String MAGNET_ENERGY_CONSUMPTION = "Energy Consumption";

    public static final String MAGNET_RADIUS = "Magnet Radius";

    public MagnetModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(MuseCommonStrings.WEIGHT, 1000);
        addBaseProperty(MAGNET_RADIUS, 5);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.magnet, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addBaseProperty(MAGNET_ENERGY_CONSUMPTION, 200);
        addIntTradeoffProperty(MAGNET_RADIUS, MAGNET_RADIUS, 10, "m", 1, 0);
    }

    public PowerModuleBase addIntTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        return addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, multiplier, roundTo, offset));
    }

    @Override
    public String getTextureFile() {
        return "magnetmodule";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_MAGNET;
    }

    @Override
    public String getUnlocalizedName() {
        return "magnet";
    }

    @Override
    public String getDescription() {
        return "Generates a magnetic field strong enough to attract items towards the player.         WARNING:                   This module drains power continuously. Turn it off when not needed. (Keybind menu: k)";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack stack) {
        if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, MAGNET_ENERGY_CONSUMPTION)) {
            if ((player.worldObj.getTotalWorldTime() % 20) == 0) {
                ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, MAGNET_ENERGY_CONSUMPTION));
            }
            int range = (int) ModuleManager.computeModularProperty(stack, MAGNET_RADIUS);
            World world = player.worldObj;
            AxisAlignedBB bounds = player.boundingBox.expand(range, range, range);
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                bounds.expand(0.2000000029802322D, 0.2000000029802322D, 0.2000000029802322D);
                if (stack.getItemDamage() >> 1 >= 7) {
                    List<EntityArrow> arrows = world.getEntitiesWithinAABB(EntityArrow.class, bounds);
                    for (EntityArrow arrow : arrows) {
                        if ((arrow.canBePickedUp == 1) && (world.rand.nextInt(6) == 0)) {
                            EntityItem replacement = new EntityItem(world, arrow.posX, arrow.posY, arrow.posZ, new ItemStack(Items.arrow));
                            world.spawnEntityInWorld(replacement);
                        }
                        world.removeEntity(arrow);
                    }
                }
            }
            List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
            for (EntityItem e : list) {
                if (e.age >= 10) {
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
                    if (e.isCollidedHorizontally) {
                        e.motionY += 1.0D;
                    }
                    if (world.rand.nextInt(20) == 0) {
                        float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
                        world.playSoundEffect(e.posX, e.posY, e.posZ, "mob.endermen.portal", 0.6F, pitch);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
