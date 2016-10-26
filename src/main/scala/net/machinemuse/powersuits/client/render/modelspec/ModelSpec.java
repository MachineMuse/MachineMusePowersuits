//package net.machinemuse.powersuits.client.render.modelspec;
//
//import net.machinemuse.numina.scala.MuseRegistry;
//import net.minecraft.util.Vec3;
//import net.minecraftforge.client.model.obj.WavefrontObject;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 7:44 AM, 4/28/13
// *
// * Ported to Java by lehjr on 10/23/16.
// */
//public class ModelSpec extends MuseRegistry<ModelPartSpec> {
//    private final WavefrontObject model;
//    private final String[] textures;
//    private final Vec3 offset;
//    private final Vec3 rotation;
//    private final String filename;
//
//    public ModelSpec(final WavefrontObject model, final String[] textures, final Vec3 offset, final Vec3 rotation, final String filename) {
//        this.model = model;
//        this.textures = textures;
//        this.offset = offset;
//        this.rotation = rotation;
//        this.filename = filename;
//    }
//
//
//
//
//
//
////    class ModelSpec(val model: WavefrontObject,
////    val textures: Array[String],
////    val offset: Option[Vec3],
////    val rotation: Option[Vec3],
////    val filename: String
////        ) extends MuseRegistry[ModelPartSpec] {
////        def applyOffsetAndRotation() = {
////                // TODO: Implement
////        }
////
////        def getOwnName = {
////                ModelRegistry.getName(this).getOrElse("")
////        }
////    }
//
//
//
//
//
//    public WavefrontObject model() {
//        return this.model;
//    }
//
//    public String[] textures() {
//        return this.textures;
//    }
//
//    public Vec3 offset() {
//        return this.offset;
//    }
//
//    public Vec3 rotation() {
//        return this.rotation;
//    }
//
//    public String filename() {
//        return this.filename;
//    }
//
//    public void applyOffsetAndRotation() {
//    }
//
//    public String getOwnName() {
//        return (ModelRegistry.getName(this.) != null) ? ModelRegistry.getName(this) : ("");
//
//
//        return (String)((MuseBiMap<Object, T>)ModelRegistry$.MODULE$).getName((T)this).getOrElse((Function0)new ModelSpec$$anonfun$getOwnName.ModelSpec$$anonfun$getOwnName$1(this));
//    }
//
//
//}
//
