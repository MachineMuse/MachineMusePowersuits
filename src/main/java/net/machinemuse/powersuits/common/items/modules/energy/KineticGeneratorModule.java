package net.machinemuse.powersuits.common.items.modules.energy;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_KINETIC_GENERATOR;

public class KineticGeneratorModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {

    public static final String KINETIC_ENERGY_GENERATION = "Energy Per 5 Blocks";
    public static final String KINETIC_HEAT_GENERATION = "Heat Generation";

    public KineticGeneratorModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(KINETIC_HEAT_GENERATION, 5);
        addBaseProperty(MuseCommonStrings.WEIGHT, 1000);
        addBaseProperty(KINETIC_ENERGY_GENERATION, 200);
        addTradeoffProperty("Energy Generated", KINETIC_ENERGY_GENERATION, 600, " Joules");
        addTradeoffProperty("Energy Generated", MuseCommonStrings.WEIGHT, 3000, "g");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_KINETIC_GENERATOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "kineticGenerator";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (!player.isAirBorne) {
            NBTTagCompound tag = MuseItemUtils.getMuseItemTag(item);
            boolean isNotWalking = (player.getRidingEntity() != null) || (player.isInWater());
            if ((!tag.hasKey("x")) || (isNotWalking))
                tag.setInteger("x", (int) player.posX);
            if ((!tag.hasKey("z")) || (isNotWalking))
                tag.setInteger("z", (int) player.posZ);
            double distance = Math.sqrt((tag.getInteger("x") - (int) player.posX) * (tag.getInteger("x") - (int) player.posX) + (tag.getInteger("z") - (int) player.posZ) * (tag.getInteger("z") - (int) player.posZ));
            if (distance >= 5.0) {
                tag.setInteger("x", (int) player.posX);
                tag.setInteger("z", (int) player.posZ);
                if (player.isSprinting()) {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, KINETIC_ENERGY_GENERATION));
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, KINETIC_HEAT_GENERATION));
                } else {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(item, KINETIC_ENERGY_GENERATION) / 2);
                    MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(item, KINETIC_HEAT_GENERATION) / 2);
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.kineticGenerator;
    }
}