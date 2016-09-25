package net.machinemuse.powersuits.block;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leon on 9/14/16.
 */
public class ModelLuxCapatitor {
    private static ResourceLocation modelLocation = new ResourceLocation(ModularPowersuits.MODID().toLowerCase(), "models/block/lightCore.obj");
//    private static ResourceLocation modelLocation = new ResourceLocation(ModularPowersuits.MODID().toLowerCase(), "models/block/luxcapacitor.obj");

    private static final String materialName = "Material__3.003"; // material of the lens

    private static Map<String, String> customData = new HashMap<String, String>();
    public static final ModelLuxCapatitor instance = new ModelLuxCapatitor();

    private ModelLuxCapatitor(){

    }

    private Map<Integer, Map<EnumFacing, IBakedModel>> registeredModels = new HashMap<>();


    private OBJModel getOBJModel(){
        // this is to fix the texture orientation
        if (!customData.containsKey("flip-v"))
            customData.put("flip-v", "true");

        OBJModel lightModel = null;
        try {
            lightModel = (OBJModel) OBJLoader.INSTANCE.loadModel(modelLocation);
            lightModel = (OBJModel) lightModel.process(ImmutableMap.copyOf(customData));
        } catch (Exception e) {
            System.out.println("loading model faild!!");
            lightModel = (OBJModel) ModelLoaderRegistry.getMissingModel();
        }
        if (lightModel == null)
            lightModel = (OBJModel) ModelLoaderRegistry.getMissingModel();

        return lightModel;
    }

    /**
     * We need our own because the default set is based on the default=facing north
     * Our model is default facing up
     */
    public TRSRTransformation getTransform(EnumFacing side) {
        Matrix4f matrix;

        switch(side.getOpposite()) {
            case DOWN:
                matrix = ModelRotation.X180_Y0.getMatrix();
                break;
            case UP:
                matrix = TRSRTransformation.identity().getMatrix();
                break;
            case NORTH:
                matrix = ModelRotation.X90_Y0.getMatrix();
                break;
            case SOUTH:
                matrix = ModelRotation.X270_Y0.getMatrix();
                break;
            case WEST:
                matrix = ModelRotation.X90_Y270.getMatrix();
                break;
            case EAST:
                matrix = ModelRotation.X90_Y90.getMatrix();
                break;
            default:
                matrix = new Matrix4f();
        }
        return new TRSRTransformation(matrix);
    }


    private IBakedModel bakedLuxCapModel(TRSRTransformation transformation, EnumFacing facing, Vector4f colorVec) {
        OBJModel model = getOBJModel();

        if (model.getMatLib().getMaterialNames().contains(materialName))
            model.getMatLib().getMaterial(materialName).setColor(colorVec);
        else
            System.out.println("Material for LuxCapacitor Lens not found: " + materialName);

        IBakedModel bakedModel = model.bake(transformation, DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        return bakedModel;
    }

    public IBakedModel getModel(Colour color, EnumFacing facing) {
        Vector4f colorVec = new Vector4f();

        colorVec.w = (float) color.a;
        colorVec.x = (float) color.r;
        colorVec.y = (float) color.g;
        colorVec.z = (float) color.b;

        IBakedModel luxCapBakedModel = null;
        try {
            luxCapBakedModel = registeredModels.get(color).get(facing);
        } catch (Exception e) {

        }

        if (luxCapBakedModel == null) {
            Map<EnumFacing, IBakedModel> tempMap = new HashMap<>();
            IBakedModel tempBaked;

            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                EnumFacing side = EnumFacing.getFront(i);
                tempBaked = bakedLuxCapModel(getTransform(side), side, colorVec);
                if (side == facing)
                    luxCapBakedModel = tempBaked;
                tempMap.put(side, tempBaked);
            }
            registeredModels.put(color.getInt(), tempMap);
        }
        return luxCapBakedModel;
    }
}
