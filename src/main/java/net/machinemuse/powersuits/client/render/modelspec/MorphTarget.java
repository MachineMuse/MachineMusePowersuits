package net.machinemuse.powersuits.client.render.modelspec;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:09 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public enum MorphTarget {
    Cloak,
    Head,
    Body,
    RightArm,
    LeftArm,
    RightLeg,
    LeftLeg;

    public ModelRenderer apply(ModelBiped m) {
        switch(this) {
            case Cloak:
                return m.bipedCloak;

            case Head:
                return m.bipedHead;

            case Body:
                return m.bipedBody;

            case RightArm:
                return m.bipedRightArm;

            case LeftArm:
                return m.bipedLeftArm;

            case RightLeg:
                return m.bipedRightLeg;

            case LeftLeg:
                return m.bipedLeftLeg;

            default:
                return null;
        }
    }
}