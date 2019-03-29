package net.machinemuse.numina.client.render.modelspec;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:09 AM, 29/04/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 */
public enum MorphTarget {
    Head("HEAD", EntityEquipmentSlot.HEAD),
    Body("BODY", EntityEquipmentSlot.CHEST),
    RightArm("RIGHTARM", EntityEquipmentSlot.CHEST),
    LeftArm("LEFTARM", EntityEquipmentSlot.CHEST),
    RightLeg("RIGHTLEG", EntityEquipmentSlot.LEGS),
    LeftLeg("LEFTLEG", EntityEquipmentSlot.LEGS),
    RightFoot("RIGHTFOOT", EntityEquipmentSlot.FEET),
    LeftFoot("LEFTFOOT", EntityEquipmentSlot.FEET),

    /**
     * Note that these may be reversed and special checks are needed for rendering
     * hand-dependant models.
     */
    RightHand("RIGHTHAND", EntityEquipmentSlot.MAINHAND),
    Lefthand("LEFTHAND", EntityEquipmentSlot.OFFHAND);

    String name;
    EntityEquipmentSlot slot;

    MorphTarget(String name, EntityEquipmentSlot slot) {
        this.name = name;
        this.slot = slot;
    }

    public static MorphTarget getMorph(final String name) {
        return Arrays.stream(values()).filter(morph -> name.toUpperCase().equals(morph.name)).findAny().orElseGet(null);
    }

    public ModelRenderer apply(ModelBiped m) {
        switch (this) {
            case Head:
                return m.bipedHead;

            case Body:
                return m.bipedBody;

            case RightHand:
            case RightArm:
                return m.bipedRightArm;

            case Lefthand:
            case LeftArm:
                return m.bipedLeftArm;

            case RightFoot:
            case RightLeg:
                return m.bipedRightLeg;

            case LeftFoot:
            case LeftLeg:
                return m.bipedLeftLeg;

            default:
                return null;
        }
    }
}