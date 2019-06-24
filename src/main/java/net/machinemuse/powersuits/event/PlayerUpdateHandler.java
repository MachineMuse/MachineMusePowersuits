package net.machinemuse.powersuits.event;

import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by Claire Semple on 9/8/2014.
 * <p>
 * Ported to Java by lehjr on 10/24/16.
 */
public class PlayerUpdateHandler {
    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();

            // only MPS modular items in this list
            List<ItemStack> modularItemsEquipped = MuseItemUtils.getModularItemsEquipped(player);


//            // FIXME: is this really nescessary... apparently it is
//            for (ItemStack stack : modularItemsEquipped) {
//                // Temporary Advanced Rocketry hack Not the best way but meh.
//                NBTTagList tagList = stack.getEnchantmentTagList();
//                if (tagList != null && !tagList.isEmpty()) {
//                    if (tagList.tagCount() == 1) {
//                        if (!(tagList.getCompoundTagAt(0).getShort("id") == 128))
//                            stack.getTagCompound().removeTag("ench");
//                    } else {
//                        NBTTagCompound ar = null;
//                        for (int i = 0; i < tagList.tagCount(); i++) {
//                            NBTTagCompound nbtTag = tagList.getCompoundTagAt(i);
//                            if ((nbtTag.getShort("id") == 128)) {
//                                ar = nbtTag;
//                            }
//                        }
//                        stack.getTagCompound().removeTag("ench");
//                        if (ar != null) {
//                            stack.getTagCompound().setTag("ench", ar);
//                        }
//                    }
//                }
//            }

//            Enchantment.getEnchantmentID(AdvancedRocketryAPI.enchantmentSpaceProtection);

            for (ItemStack itemStack : modularItemsEquipped) {
                for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IPlayerTickModule.class)) {
                    if (ModuleManager.INSTANCE.isValidForItem(itemStack, module) && ModuleManager.INSTANCE.itemHasModule(itemStack, module.getDataName())) {
                        if (ModuleManager.INSTANCE.isModuleOnline(itemStack, module.getDataName()))
                            ((IPlayerTickModule) module).onPlayerTickActive(player, itemStack);
                        else
                            ((IPlayerTickModule) module).onPlayerTickInactive(player, itemStack);
                    }
                }
            }

            // Sound update
            if (player.world.isRemote && NuminaConfig.useSounds()) {
                if (modularItemsEquipped.size() > 0) {
                    double velsq2 = MuseMathUtils.sumsq(player.motionX, player.motionY, player.motionZ) - 0.5;
                    if (player.isAirBorne && velsq2 > 0) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_GLIDER, SoundCategory.PLAYERS, (float) (velsq2 / 3), 1.0f, true);
                    } else {
                        Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_GLIDER);
                    }
                } else {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_GLIDER);
                }

                if (!(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemPowerArmorBoots))
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);

                if (!(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPowerArmorChestplate))
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);

                if (!(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemPowerArmorLeggings))
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
            }

            // Done this way so players can let their stuff cool in their inventory without having to equip it.
            List<ItemStack> modularItemsInInventory = MuseItemUtils.getModularItemsInInventory(player);
            if (modularItemsInInventory.size() > 0) {
                // Heat update
                double currHeat = MuseHeatUtils.getPlayerHeat(player);
                if (currHeat >= 0 && !player.world.isRemote) { // only apply serverside so change is not applied twice
                    double coolPlayerAmount = MusePlayerUtils.getPlayerCoolingBasedOnMaterial(player) * 0.55;  // cooling value adjustment. Too much or too little cooling makes the heat system useless.

                    // cooling value adjustment. Too much or too little cooling makes the heat system useless.

                    if (coolPlayerAmount > 0)
                        MuseHeatUtils.coolPlayer(player, coolPlayerAmount);

                    double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);

                    if (currHeat > maxHeat) {
                        player.attackEntityFrom(MuseHeatUtils.overheatDamage, (float) (Math.sqrt(currHeat - maxHeat)/* was (int) */ / 4));
                        player.setFire(1);
                    } else {
                        player.extinguish();
                    }
                }
            }
        }
    }
}