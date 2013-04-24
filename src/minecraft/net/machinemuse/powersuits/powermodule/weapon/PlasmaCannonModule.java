package net.machinemuse.powersuits.powermodule.weapon;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.network.packets.MusePacketPlasmaBolt;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class PlasmaCannonModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_PLASMA_CANNON = "Plasma Cannon";
    public static final String PLASMA_CANNON_ENERGY_PER_TICK = "Plasma Energy Per Tick";
    public static final String PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE = "Plasma Damage At Full Charge";
    public static final String PLASMA_CANNON_EXPLOSIVENESS = "Plasma Explosiveness";

    public PlasmaCannonModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(PLASMA_CANNON_ENERGY_PER_TICK, 10, "J");
        addBaseProperty(PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 2, "pt");
        addTradeoffProperty("Amperage", PLASMA_CANNON_ENERGY_PER_TICK, 150, "J");
        addTradeoffProperty("Amperage", PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 38, "pt");
        addTradeoffProperty("Voltage", PLASMA_CANNON_ENERGY_PER_TICK, 50, "J");
        addTradeoffProperty("Voltage", PLASMA_CANNON_EXPLOSIVENESS, 0.5, "Creeper");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getName() {
        return MODULE_PLASMA_CANNON;
    }

    @Override
    public String getDescription() {
        return "Use electrical arcs in a containment field to superheat air to a plasma and launch it at enemies.";
    }

    @Override
    public String getTextureFile() {
        return "gravityweapon";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
        if (ElectricItemUtils.getPlayerEnergy(player) > 500) {
            player.setItemInUse(item, 72000);
        }
    }

    @Override
    public void onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
                                  float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        int chargeTicks = (int) MuseMathUtils.clampDouble(itemStack.getMaxItemUseDuration() - par4, 10, 50);

        if (!world.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_ENERGY_PER_TICK)
                    * chargeTicks;
            MuseHeatUtils.heatPlayer(player, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
                double explosiveness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_EXPLOSIVENESS);
                double damagingness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE);

                EntityPlasmaBolt plasmaBolt = new EntityPlasmaBolt(world, player, explosiveness, damagingness, chargeTicks);
                world.spawnEntityInWorld(plasmaBolt);
                MusePacketPlasmaBolt packet = new MusePacketPlasmaBolt((Player) player, plasmaBolt.entityId, plasmaBolt.size);
                PacketDispatcher.sendPacketToAllPlayers(packet.getPacket250());
            }
        }
    }

}
