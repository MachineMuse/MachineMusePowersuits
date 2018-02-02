package net.machinemuse.powersuits.common.items.modules.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.NuminaSettings;
import net.machinemuse.powersuits.client.control.PlayerInputMap;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.player.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_JETBOOTS;

public class JetBootsModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {

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
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (player.isInWater()) {
            return;
        }
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        boolean hasFlightControl = ModuleManager.itemHasActiveModule(helmet, MPSConstants.MODULE_FLIGHT_CONTROL);
        double jetEnergy = ModuleManager.computeModularProperty(item, JET_ENERGY_CONSUMPTION);
        double thrust = ModuleManager.computeModularProperty(item, JET_THRUST);

        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
            thrust *= MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
            if (hasFlightControl && thrust > 0) {
                thrust = MusePlayerUtils.thrust(player, thrust, true);
                if ((player.world.isRemote) && NuminaSettings.useSounds) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else if (jumpkey && player.motionY < 0.5) {
                thrust = MusePlayerUtils.thrust(player, thrust, false);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaSettings.useSounds) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else {
                if ((player.world.isRemote) && NuminaSettings.useSounds) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
                }
            }
        } else {
            if (player.world.isRemote && NuminaSettings.useSounds) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.world.isRemote && NuminaSettings.useSounds) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jetBoots;
    }
}
