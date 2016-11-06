//package net.machinemuse.powersuits.client.render.modelspec;
//
//import net.minecraft.client.model.ModelBiped;
//import net.minecraft.client.model.ModelRenderer;
//
///**
// * Ported to Java by lehjr on 11/5/16.
// */
//public class ArmorMorphTarget {
//    public interface MorphTarget {
//        String name = null;
//        float degrad = (float) (180F / Math.PI);
//
//        ModelRenderer apply(ModelBiped m);
//    }
//
//    public static class Cloak implements MorphTarget {
//        String name = "Cloak";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedCloak;
//        }
//    }
//
//    public static class Head implements MorphTarget {
//        String name = "Head";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedHead;
//        }
//    }
//
//    public static class Body implements MorphTarget {
//        String name = "Body";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedBody;
//        }
//    }
//
//    public static class RightArm implements MorphTarget {
//        String name = "RightArm";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedRightArm;
//        }
//    }
//
//    public static class LeftArm implements MorphTarget {
//        String name = "LeftArm";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedLeftArm;
//        }
//    }
//
//    public static class RightLeg implements MorphTarget {
//        String name = "RightLeg";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedRightLeg;
//        }
//    }
//
//    public static class LeftLeg implements MorphTarget {
//        String name = "LeftLeg";
//
//        public ModelRenderer apply(ModelBiped m) {
//            return m.bipedLeftLeg;
//        }
//    }
//}