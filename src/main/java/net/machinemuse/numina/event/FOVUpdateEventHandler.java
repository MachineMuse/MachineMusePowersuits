package net.machinemuse.numina.event;

import net.machinemuse.numina.basemod.NuminaConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:07 PM, 10/17/13
 *
 * Ported to Java by lehjr on 10/10/16.
 */
public class FOVUpdateEventHandler {
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent e) {
        if (NuminaConfig.useFOVFix()) {
            IAttributeInstance attributeinstance = e.getEntity().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            e.setNewfov((float) (e.getFov() / ((attributeinstance.getAttributeValue() / e.getEntity().capabilities.getWalkSpeed() + 1.0) / 2.0)));
        }
    }
}


