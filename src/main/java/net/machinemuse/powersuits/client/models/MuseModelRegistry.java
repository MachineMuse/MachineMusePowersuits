//package net.machinemuse.powersuits.client.models;
//
//import com.google.common.collect.BiMap;
//import com.google.common.collect.HashBiMap;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//
//@SideOnly(Side.CLIENT)
//public class MuseModelRegistry {
//    private static MuseModelRegistry INSTANCE;
//
//    public static MuseModelRegistry getInstance() {
//        if (INSTANCE == null) {
//            synchronized (MuseModelRegistry.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new MuseModelRegistry();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    private MuseModelRegistry() {
//    }
//
//    private static BiMap<IBakedModel, ResourceLocation> registry = HashBiMap.create();
//
//    @Nullable
//    public static IBakedModel getModelforResource(ResourceLocation location) {
//        if (registry.containsValue(location))
//            return registry.inverse().get(location);
//        return null;
//    }
//
//    @Nullable
//    public static ResourceLocation getResourceLocationforIBakedmode(IBakedModel bakedModel) {
//        if (registry.containsKey(bakedModel))
//            return registry.get(bakedModel);
//        return null;
//    }
//
//    public static void putIBakedModel(IBakedModel bakeModel, ResourceLocation location) {
//        registry.put(bakeModel, location);
//    }
//
//
//
//
//
//
//
//
//
//
//
////    public ModelSpec getModel(NBTTagCompound nbt) {
////        return get(nbt.getString("model"));
////    }
////
////    public ModelPartSpec getPart(NBTTagCompound nbt, ModelSpec model) {
////        return model.get(nbt.getString("part"));
////    }
////
////    public ModelPartSpec getPart(NBTTagCompound nbt) {
////        return getPart(nbt, getModel(nbt));
////    }
////
////    public NBTTagCompound getSpecTag(NBTTagCompound museRenderTag, ModelPartSpec spec) {
////        String name = makeName(spec);
////        return (museRenderTag.hasKey(name)) ? (museRenderTag.getCompoundTag(name)) : null;
////    }
////
////    public String makeName(ModelPartSpec spec) {
////        return spec.modelSpec.getOwnName() + "." + spec.partName;
////    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
