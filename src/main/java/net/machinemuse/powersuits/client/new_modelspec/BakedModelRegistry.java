package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.client.models.obj.OBJModelPlus;
import net.machinemuse.powersuits.client.models.obj.OBJPlusLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BakedModelRegistry {
    private static BakedModelRegistry INSTANCE;

    public static BakedModelRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (BakedModelRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BakedModelRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private static Map<String, IModelState> iModelStateMap;
    private static Map<Integer, String> modelLocationMap;

    private BakedModelRegistry() {
        iModelStateMap = new HashMap<>();
        modelLocationMap = new HashMap<>();
    }

    public static LoadingCache<String, OBJModelPlus.OBJBakedModelPus> REGISTRY = CacheBuilder.newBuilder()
            .maximumSize(40)
            .build(new CacheLoader<String, OBJModelPlus.OBJBakedModelPus>() {

                @Override
                public OBJModelPlus.OBJBakedModelPus load(String modelLocation) throws Exception {
                    return getBakedModel(modelLocation);
                }

                IModel getModel(String location) {
                    IModel model;
                    try {
                        model = OBJPlusLoader.INSTANCE.loadModel(new ResourceLocation(location));
                        model = model.process(ImmutableMap.of("flip-v", "true"));
                    } catch (Exception e) {
                        model = ModelLoaderRegistry.getMissingModel();
                        MuseLogger.logError("Failed to load model. " + e);
                    }
                    return model;
                }

                OBJModelPlus.OBJBakedModelPus getBakedModel(String modelLocation) {
                    IModel model = getModel(modelLocation);
                    if (model instanceof OBJModelPlus) {
                        return (OBJModelPlus.OBJBakedModelPus) model.bake(getIModelState(modelLocation), DefaultVertexFormats.ITEM,
                                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
                    }
                    return null;
                }
            });

    public static void putIModelState(String location, IModelState state) {
        if (!iModelStateMap.keySet().contains(location))
            iModelStateMap.put(location, state);
    }

    public static void replaceIModelState(String location, IModelState state) {
        iModelStateMap.put(location, state);
    }

    public static IModelState getIModelState(String location) {
        return iModelStateMap.getOrDefault(location, TRSRTransformation.identity());
    }

    public static void putModelLocation(String modelLocation) {
        modelLocationMap.put(modelLocation.hashCode(), modelLocation);
    }

    public static String getBakedModleLocation(int hash) {
        return modelLocationMap.get(hash);
    }

    public static String getBakedModelLocation(String hash) {
        return modelLocationMap.get(Integer.parseInt(hash));
    }
}