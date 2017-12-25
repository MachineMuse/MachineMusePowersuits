package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.client.models.ModelHelper;
import net.machinemuse.powersuits.client.models.obj.OBJModelPlus;
import net.minecraft.client.renderer.block.model.BakedQuad;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is where the Model/Texture spec's are stored
 *
 * Texture spec is a default setting for skinned armor, while ModelSpec is for high poly models
 * including the armor and power fist. Newly crafted items will start with these settings and can be
 * changed in the revamped cosmetic window of the TinkerTable
 *
 */
public class ModelSpecRegistry {
    private static ModelSpecRegistry INSTANCE;

    public static ModelSpecRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelSpecRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelSpecRegistry();
                }
            }
        }
        return INSTANCE;
    }
    // hash value of the spec name is used for the key
    Map<Integer, TextureSpec> ARMOR_SKIN_REGISTRY;
    Map<Integer, ModelSpec> ARMOR_MODEL_SPEC_REGISTRY;
    Map<Integer, ModelSpec> POWERFIST_MODEL_SPEC_REGISTRY;
    Map<Integer, String> NAME_MAP;
    Map<String, Binding> MODEL_HASH_DOT_PARTNAME_BINDING_MAP;




    private ModelSpecRegistry() {
        ARMOR_SKIN_REGISTRY= new HashMap<>();
        ARMOR_MODEL_SPEC_REGISTRY = new HashMap<>();
        POWERFIST_MODEL_SPEC_REGISTRY = new HashMap<>();
        NAME_MAP = new HashMap<>();
        MODEL_HASH_DOT_PARTNAME_BINDING_MAP = new HashMap<>();
    }

    public static LoadingCache<ModelPartSpec, List<BakedQuad>> BAKED_QUAD_REGISTRY = CacheBuilder.newBuilder()
            .maximumSize(140)
            .build(new CacheLoader<ModelPartSpec, List<BakedQuad>>() {





                @Override
                public List<BakedQuad> load(ModelPartSpec partSpec) throws Exception {
                    System.out.println("location: ".concat(partSpec.getModelLocation()));
                    OBJModelPlus.OBJBakedModelPus bakedModel = BakedModelRegistry.getInstance().REGISTRY.get(partSpec.getModelLocation());
                    System.out.println("baked model: ".concat(bakedModel.toString()));

                    System.out.println("partname: ".concat(partSpec.getPartName()));

                    List<BakedQuad> quadList = bakedModel.getQuadsforPart(partSpec.getPartName());

                    System.out.println("quadlist size: " + quadList.size());

//                    return quadList;


                    return ModelHelper.getColoredQuadsWithGlow(quadList, EnumColour.getColourEnumFromIndex(partSpec.getColourindex()).getColour(), partSpec.isGlow());
                }
            });













    public void putSpec(String specName, Spec spec) {
        final int hash = specName.hashCode();

        switch(spec.getSpecType()) {
            case ARMOR_SKIN:
                ARMOR_SKIN_REGISTRY.put(hash, (TextureSpec) spec);
                break;
            case ARMOR_MODEL:
                ARMOR_MODEL_SPEC_REGISTRY.put(hash, (ModelSpec) spec);
                break;
            case POWER_FIST:
                POWERFIST_MODEL_SPEC_REGISTRY.put(hash, (ModelSpec) spec);
                break;
        }
        NAME_MAP.put(hash, specName);


        System.out.println("hash: " + hash);
        System.out.println("specName: " + specName);
        System.out.println("name lookup from hash: " + NAME_MAP.get(hash));
    }

    public TextureSpec getDefaultTextureSpec() {
        return ARMOR_SKIN_REGISTRY.entrySet().stream()
                .filter(map -> map.getValue().isDefault())
                .map(map->map.getValue()).findFirst().get();
    }

    public ModelSpec getDefaultArmorModelSpec() {
        return ARMOR_MODEL_SPEC_REGISTRY.entrySet().stream()
                .filter(map -> map.getValue().isDefault())
                .map(map->map.getValue()).findFirst().get();
    }

    @Nullable
    public ModelSpec getDefaultPowerFistModelSpec () {
        return POWERFIST_MODEL_SPEC_REGISTRY.entrySet().stream()
                .filter(map -> map.getValue().isDefault())
                .map(map->map.getValue()).findFirst().orElseGet(null);
    }

    public ModelSpec getPowerFistModemSpec(String specName) {
        return POWERFIST_MODEL_SPEC_REGISTRY.getOrDefault(specName, getDefaultPowerFistModelSpec());
    }

    public Binding getBindingforPartSpec(ModelPartSpec partspec) {
        return MODEL_HASH_DOT_PARTNAME_BINDING_MAP.get(
                new StringBuilder("").append(partspec.getModelHash()).append(".").append(partspec.getPartName()).toString());
    }

    public void setBinding(String modelLocationHashDotPartName, Binding binding) {
        MODEL_HASH_DOT_PARTNAME_BINDING_MAP.put(modelLocationHashDotPartName, binding);
    }

    public void putPath(String path) {
        NAME_MAP.put(path.hashCode(), path);
    }
}