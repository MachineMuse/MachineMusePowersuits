package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.general.sound.SoundDictionary;
import net.machinemuse.numina.basemod.NuminaConfig;
import net.machinemuse.numina.sound.Musique;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class JetBootsModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_JETBOOTS = "Jet Boots";
    public static final String JET_ENERGY_CONSUMPTION = "Jetboots Energy Consumption";
    public static final String JET_THRUST = "Jetboots Thrust";

    public JetBootsModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 2));
        addBaseProperty(JET_ENERGY_CONSUMPTION, 0);
        addBaseProperty(JET_THRUST, 0);
        addTradeoffProperty("Thrust", JET_ENERGY_CONSUMPTION, 75);
        addTradeoffProperty("Thrust", JET_THRUST, 0.08);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_JETBOOTS;
    }

    @Override
    public String getUnlocalizedName() {
        return "jetBoots";
    }

    @Override
    public String getDescription() {
        return "Jet boots are not as strong as a jetpack, but they should at least be strong enough to counteract gravity.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        ItemStack chest = player.inventory.armorItemInSlot(1);
        if (player.isInWater()) {
            return;
        }
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        ItemStack helmet = player.inventory.armorItemInSlot(3);
        boolean hasFlightControl = ModuleManager.itemHasActiveModule(helmet, FlightControlModule.MODULE_FLIGHT_CONTROL);
        double jetEnergy = ModuleManager.computeModularProperty(item, JET_ENERGY_CONSUMPTION);
        double thrust = ModuleManager.computeModularProperty(item, JET_THRUST);

        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
            thrust *= MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
            if (hasFlightControl && thrust > 0) {
                thrust = MusePlayerUtils.thrust(player, thrust, true);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, (float) (thrust * 12.5), 1.0f, true, SoundCategory.PLAYERS);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else if (jumpkey && player.motionY < 0.5) {
                thrust = MusePlayerUtils.thrust(player, thrust, false);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, (float) (thrust * 12.5), 1.0f, true, SoundCategory.PLAYERS);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else {
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
                }
            }
        } else {
            if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side
                .CLIENT) && NuminaConfig.useSounds()) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jetBoots;
    }
}
