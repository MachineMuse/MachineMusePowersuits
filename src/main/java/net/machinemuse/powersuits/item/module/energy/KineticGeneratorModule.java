package net.machinemuse.powersuits.item.module.energy;


import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KineticGeneratorModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String KINETIC_ENERGY_GENERATION = "Energy Per 5 Blocks";
    public static final String KINETIC_HEAT_GENERATION = "Heat Generation";

    public KineticGeneratorModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.LEGSONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(KINETIC_HEAT_GENERATION, 5);
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 1000);
        addBasePropertyDouble(KINETIC_ENERGY_GENERATION, 200);
        addTradeoffPropertyDouble("Energy Generated", KINETIC_ENERGY_GENERATION, 600, " Joules");
        addTradeoffPropertyDouble("Energy Generated", MPSModuleConstants.WEIGHT, 3000, "g");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENERGY;
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
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(item, KINETIC_ENERGY_GENERATION));
                    MuseHeatUtils.heatPlayerLegacy(player, ModuleManager.getInstance().computeModularPropertyDouble(item, KINETIC_HEAT_GENERATION));
                } else {
                    ElectricItemUtils.givePlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(item, KINETIC_ENERGY_GENERATION) / 2);
                    MuseHeatUtils.heatPlayerLegacy(player, ModuleManager.getInstance().computeModularPropertyDouble(item, KINETIC_HEAT_GENERATION) / 2);
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