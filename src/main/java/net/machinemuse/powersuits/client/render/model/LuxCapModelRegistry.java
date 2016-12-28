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

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 * Ported to Java by lehjr on 12/27/16.
 */
public class LuxCapModelRegistry {
    private static Map<String, String> customData = new HashMap<String, String>();
    private Map<EnumFacing, BakedQuad> lenseModelMap = new HashMap<>();
    private Map<EnumFacing, BakedQuad> baseModelMap = new HashMap<>();
    private Map<Integer, Map<EnumFacing, BakedQuad>> completeModels = new HashMap<>();

    private static LuxCapModelRegistry ourInstance = new LuxCapModelRegistry();

    public static LuxCapModelRegistry getInstance() {
        return ourInstance;
    }

    private LuxCapModelRegistry() {
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

        matrix.setScale(0.0625F);
        matrix.setTranslation(new Vector3f(0,-8, 0));



        //        glTranslated(x + 0.5, y + 0.5, z + 0.5);
//        double scale = 0.0625;
//        glScaled(scale, scale, scale);
//        switch (tileentity.side) {
//            case DOWN:
//                glTranslated(0, -8, 0);
//                break;
//            case EAST:
//                glRotatef(90, 0, 0, 1);
//                glTranslated(0, -8, 0);
//                break;
//            case NORTH:
//                glRotatef(90, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case SOUTH:
//                glRotatef(-90, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case UP:
//                glRotatef(180, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case WEST:
//                glRotatef(-90, 0, 0, 1);
//                glTranslated(0, -8, 0);
//                break;
//            default:
//                break;
//
//        }






        return new TRSRTransformation(matrix);
    }





}
