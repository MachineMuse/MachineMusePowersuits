package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 * Ported to Java by lehjr on 12/27/16.
 */
public class ModelLuxCapLens {
    private static Map<String, String> customData = new HashMap<String, String>();
    private Map<EnumFacing, BakedQuad> lenseModelMap = new HashMap<>();
    private Map<EnumFacing, BakedQuad> baseModelMap = new HashMap<>();
    private Map<Integer, Map<EnumFacing, BakedQuad>> completeModels = new HashMap<>();

    private static ModelLuxCapLens ourInstance = new ModelLuxCapLens();

    public static ModelLuxCapLens getInstance() {
        return ourInstance;
    }

    private ModelLuxCapLens() {
    }


    private OBJModel getOBJModel(ResourceLocation modelLocation){
        // this is to fix the texture orientation
        if (!customData.containsKey("flip-v"))
            customData.put("flip-v", "true");

        OBJModel imodel = null;
        try {
            imodel = (OBJModel) OBJLoader.INSTANCE.loadModel(modelLocation);
            imodel = (OBJModel) imodel.process(ImmutableMap.copyOf(customData));
        } catch (Exception e) {
            System.out.println("loading model failed!!");
            imodel = (OBJModel) ModelLoaderRegistry.getMissingModel();
        }
        if (imodel == null)
            imodel = (OBJModel) ModelLoaderRegistry.getMissingModel();

        return imodel;
    }




/*
                        // block/block
                        else if (transform.equals("forge:default-block"))
                        {
                            TRSRTransformation thirdperson = get(0, 2.5f, 0, 75, 45, 0, 0.375f);
                            ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
                            builder.put(TransformType.GUI,                     get(0, 0, 0, 30, 225, 0, 0.625f));
                            builder.put(TransformType.GROUND,                  get(0, 3, 0, 0, 0, 0, 0.25f));
                            builder.put(TransformType.FIXED,                   get(0, 0, 0, 0, 0, 0, 0.5f));
                            builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
                            builder.put(TransformType.THIRD_PERSON_LEFT_HAND,  leftify(thirdperson));
                            builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4f));
                            builder.put(TransformType.FIRST_PERSON_LEFT_HAND,  get(0, 0, 0, 0, 225, 0, 0.4f));
                            ret.state = Optional.<IModelState>of(new SimpleModelState(builder.build()));
                        }


                            / **
     * convert transformation from assuming center-block system to corner-block system
     * /
public static TRSRTransformation blockCenterToCorner(TRSRTransformation transform)
{
    Matrix4f ret = new Matrix4f(transform.getMatrix()), tmp = new Matrix4f();
    tmp.setIdentity();
    tmp.m03 = tmp.m13 = tmp.m23 = .5f;
    ret.mul(tmp, ret);
    tmp.m03 = tmp.m13 = tmp.m23 = -.5f;
    ret.mul(tmp);
    return new TRSRTransformation(ret);
}

                                    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
            {
                return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                    new Vector3f(tx / 16, ty / 16, tz / 16),
                    TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
                    new Vector3f(s, s, s),
                    null));
            }



 */





    /**
     * We need our own because the default set is based on the default=facing north
     * Our model is default facing up
     */
    public TRSRTransformation getTransform(EnumFacing side) {
        Matrix4f matrix;

        switch(side.getOpposite()) {
            case DOWN:
                matrix = TRSRTransformation.identity().getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, -0.40f, 0.0f));
                matrix.setScale(0.0625f);
                break;
            case UP:
                matrix = ModelRotation.X180_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.40f, 0.0f));
                matrix.setScale(0.0625f);
                break;
            case NORTH:
                matrix = ModelRotation.X90_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.0f, -0.4f));
                matrix.setScale(0.0625f);
                break;
            case SOUTH:
                matrix = ModelRotation.X270_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.0f, 0.4f));
                matrix.setScale(0.0625f);
                break;
            case WEST:
                matrix = ModelRotation.X90_Y270.getMatrix();
                matrix.setTranslation(new Vector3f(-0.4f, 0.0f, -0.0f));
                matrix.setScale(0.0625f);
                break;
            case EAST:
                matrix = ModelRotation.X90_Y90.getMatrix();

                matrix.setScale(0.0625f);
                break;
            default:
                matrix = new Matrix4f();
        }
        return new TRSRTransformation(matrix);
    }
}