package net.machinemuse.powersuits.event;

import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

public class MovementManager {
    public static final Map<String, Double> playerJumpMultipliers = new HashMap();
    /**
     * Gravity, in meters per tick per tick.
     */
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;

    public static double getPlayerJumpMultiplier(EntityPlayer player) {
        if (playerJumpMultipliers.containsKey(player.getCommandSenderEntity().getName())) {
            return playerJumpMultipliers.get(player.getCommandSenderEntity().getName());
        } else {
            return 0;
        }
    }

    public static void setPlayerJumpTicks(EntityPlayer player, double number) {
        playerJumpMultipliers.put(player.getCommandSenderEntity().getName(), number);
    }

    public static double computeFallHeightFromVelocity(double velocity) {
        double ticks = velocity / DEFAULT_GRAVITY;
        return -0.5 * DEFAULT_GRAVITY * ticks * ticks;
    }

    // moved here so it is still accessible if sprint assist module isn't installed.
    public static void setMovementModifier(ItemStack itemStack, double multiplier, EntityPlayer player) {
        // reduce player speed according to Kinetic Energy Generator setting
        if (ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME)) {
            double movementResistance = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE);
            multiplier -= movementResistance;
        }

        // player walking speed: 0.10000000149011612
        // player sprintint speed: 0.13000001
        double additive = multiplier * (player.isSprinting() ? 0.13 : 0.1)/2;
        NBTTagCompound itemNBT = MuseNBTUtils.getNBTTag(itemStack);
        boolean hasAttribute = false;
        if (itemNBT.hasKey("AttributeModifiers", Constants.NBT.TAG_LIST)) {
            NBTTagList nbttaglist = itemNBT.getTagList("AttributeModifiers", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound attributeTag = nbttaglist.getCompoundTagAt(i);
                if (attributeTag.getString("Name").equals(SharedMonsterAttributes.MOVEMENT_SPEED.getName())) {
                    attributeTag.setDouble("Amount", additive);
                    hasAttribute = true;
                    break;
                }
            }
        }
        if (!hasAttribute && additive != 0)
            itemStack.addAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), additive, 0), EntityEquipmentSlot.LEGS);
    }

    @SubscribeEvent
    public void handleLivingJumpEvent(LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

            if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor
                    && ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_JUMP_ASSIST__DATANAME)) {
                double jumpAssist = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_MULTIPLIER) * 2;
                double drain = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION);
                int avail = ElectricItemUtils.getPlayerEnergy(player);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JUMP_ASSIST, SoundCategory.PLAYERS, (float) (jumpAssist / 8.0), (float) 1, false);
                }
                if (drain < avail) {
                    ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                    setPlayerJumpTicks(player, jumpAssist);
                    double jumpCompensationRatio = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_FOOD_COMPENSATION);
                    if (player.isSprinting()) {
                        player.getFoodStats().addExhaustion((float) (-0.8 * jumpCompensationRatio));
                    } else {
                        player.getFoodStats().addExhaustion((float) (-0.2 * jumpCompensationRatio));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void handleFallEvent(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer && event.getDistance() > 3.0) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!boots.isEmpty()) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(boots, MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME)) {
                    double distanceAbsorb = event.getDistance() * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER);
                    if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_GUI_INSTALL, SoundCategory.PLAYERS, (float) (distanceAbsorb), (float) 1, false);
                    }

                    double drain = distanceAbsorb * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION);
                    int avail = ElectricItemUtils.getPlayerEnergy(player);
                    if (drain < avail) {
                        ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                        event.setDistance((float) (event.getDistance() - distanceAbsorb));
                    }
                }

            }
        }
    }
}