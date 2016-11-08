//package net.machinemuse.powersuits.client.render.modelspec;
//
///**
// * Ported to Java by lehjr on 11/8/16.
// */
//public enum MorphTarget {
//    Cloak("Cloak", m.bipedCloak),
//    Head("Head", m.bipedHead),
//    Body("Body", m.bipedBody),
//    RightArm("RightArm", m.bipedRightArm),
//    LeftArm("LeftArm", m.bipedRightArm),
//    RightLeg("RightLe",  m.bipedRightLeg),
//    LeftLeg("LeftLeg", m.bipedLeftLeg);
//
//
//    public MorphTarget(String name, ModelBiped)
//
//
//
//
//
////    sealed trait MorphTarget {
////        val name: String
////        val degrad = 180F / Math.PI.asInstanceOf[Float]
////
////        def apply(m: ModelBiped): ModelRenderer
////    }
////
////case object Cloak extends MorphTarget {
////        val name = "Cloak"
////
////        def apply(m: ModelBiped) = m.bipedCloak
////    }
////
////case object Head extends MorphTarget {
////        val name = "Head"
////
////        def apply(m: ModelBiped) = m.bipedHead
////    }
////
////case object Body extends MorphTarget {
////        val name = "Body"
////
////        def apply(m: ModelBiped) = m.bipedBody
////    }
////
////case object RightArm extends MorphTarget {
////        val name = "RightArm"
////
////        def apply(m: ModelBiped) = m.bipedRightArm
////    }
////
////case object LeftArm extends MorphTarget {
////        val name = "LeftArm"
////
////        def apply(m: ModelBiped) = m.bipedLeftArm
////    }
////
////case object RightLeg extends MorphTarget {
////        val name = "RightLeg"
////
////        def apply(m: ModelBiped) = m.bipedRightLeg
////    }
////
////case object LeftLeg extends MorphTarget {
////        val name = "LeftLeg"
////
////        def apply(m: ModelBiped) = m.bipedLeftLeg
////}
