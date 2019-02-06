package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.capabilities.player.CapabilityPlayerValues;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Capability handler
 *
 * This class is responsible for attaching our capabilities
 */
public class CapabilityHandler {
    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event) {
        if (!(event.getObject() instanceof EntityPlayer)) return;
        event.addCapability(new ResourceLocation(ModularPowersuits.MODID, "IPlayerValues"), new CapabilityPlayerValues());
    }
}