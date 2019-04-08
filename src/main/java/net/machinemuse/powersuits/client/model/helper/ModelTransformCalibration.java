package net.machinemuse.powersuits.client.model.helper;

import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ModelTransformCalibration {
    static float xOffest_original;
    static float yOffest_original;
    static float zOffest_original;

    public static float xOffest;
    public static float yOffest;
    public static float zOffest;

    static float angleX_original;
    static float angleY_original;
    static float angleZ_original;

    public static float angleX;
    public static float angleY;
    public static float angleZ;

    static float scalemodifier_original;

    public static float scalemodifier;

    public static boolean tap;

    public ModelTransformCalibration() {
        this(0,0,0,0,0,0,0.625f);
    }

    // unreliable
//    public ModelTransformCalibration(TRSRTransformation transform) {
//        this(
//                transform.getTranslation().x * 16,
//                transform.getTranslation().y * 16,
//                transform.getTranslation().z * 16,
//
//                TRSRTransformation.toYXZDegrees(transform.getLeftRot()).x,
//                TRSRTransformation.toYXZDegrees(transform.getLeftRot()).y,
//                TRSRTransformation.toYXZDegrees(transform.getLeftRot()).z,
//
//                transform.getScale().x
//        );
//    }

    public ModelTransformCalibration(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
        this.xOffest = this.xOffest_original = transformX;
        this.yOffest = this.yOffest_original = transformY;
        this.zOffest = this.zOffest_original = transformZ;

        this.angleX = this.angleX_original = angleX;
        this.angleY = this.angleY_original = angleY;
        this.angleZ = this.angleZ_original = angleZ;

        this.scalemodifier = this.scalemodifier_original = scale;

        this.tap = false;
    }

    static boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(Minecraft.getInstance().mainWindow.getHandle(), key) == GLFW.GLFW_PRESS;
    }

    //----------------------------------
    /*
     * Only used for setting up scale, rotation, and relative placement coordinates
     */
    public static void transformCalibration() {
        int numsegments = 16;
        if (!tap) {

            if (isKeyPressed(GLFW.GLFW_KEY_INSERT)) {
                xOffest += 0.1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_DELETE)) {
                xOffest -= 0.1;
                tap = true;
            }

            if (isKeyPressed(GLFW.GLFW_KEY_HOME)) {
                yOffest += 0.1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_END)) {
                yOffest -= 0.1;
                tap = true;
            }

            if (isKeyPressed(GLFW.GLFW_KEY_PAGE_UP )) {
                zOffest += 0.1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_PAGE_DOWN)) {
                zOffest -= 0.1;
                tap = true;
            }

            if (isKeyPressed(GLFW.GLFW_KEY_KP_4)) {
                angleX += 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_5)) {
                angleY += 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_6)) {
                angleZ += 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_1)) {
                angleX -= 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_2)) {
                angleY -= 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_3)) {
                angleZ -= 1;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_KP_8)) {
                xOffest = xOffest_original;
                yOffest = yOffest_original;
                zOffest = zOffest_original;

                angleX = angleX_original;
                angleY = angleY_original;
                angleZ = angleZ_original;

                scalemodifier = scalemodifier_original;

                tap = true;
            }
            // this probably needs a bit more work, int's are too big.
            if (isKeyPressed(GLFW.GLFW_KEY_SCROLL_LOCK)) {
                scalemodifier += 0.01f;
                tap = true;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_PAUSE)) {
                scalemodifier -= 0.01f;
                tap = true;
            }

            if (isKeyPressed(GLFW.GLFW_KEY_KP_0)) {

                System.out.println("FIXME: check consistency between using every coinstructor");

                System.out.println("xOffest: " + xOffest + ", yOffest: " + yOffest + ", zOffest: " + zOffest);
                System.out.println("xrot: " + angleX + ", yrot: " + angleY + ", zrot: " + angleZ);
                System.out.println("scaleModifier: " + scalemodifier);

                System.out.println("MuseModelHelper.get(" + xOffest +", " + yOffest + ", " + zOffest + ", " + angleX + ", " + angleY+ ", " + angleZ + ", " + scalemodifier + ")" );


                tap = true;
            }
        } else {
            if (!isKeyPressed(GLFW.GLFW_KEY_KP_0) && !isKeyPressed(GLFW.GLFW_KEY_KP_1) && !isKeyPressed(GLFW.GLFW_KEY_KP_2)
                    && !isKeyPressed(GLFW.GLFW_KEY_KP_3) && !isKeyPressed(GLFW.GLFW_KEY_KP_4)
                    && !isKeyPressed(GLFW.GLFW_KEY_KP_5) && !isKeyPressed(GLFW.GLFW_KEY_KP_6)) {
                tap = false;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tap = false;
            }
        }
    }

    public TRSRTransformation getTransform() {
        transformCalibration();
        return MuseModelHelper.get(xOffest, yOffest, zOffest, angleX, angleY, angleZ, scalemodifier);
    }


    /**

     OFFSET
     --------------------------------------------------------------------------
                        |                       |
     INSERT + xOffest   |    HOME + yOffest     |   PAGE_UP + zOffest
     DELETE - xOffest   |    END - yOffest      |   PAGE_DOWN -zOffest
                        |                       |
     --------------------------------------------------------------------------
     ROTATION
     --------------------------------------------------------------------------
                        |                       |
     NUM_PAD_4 + xRot   |   NUM_PAD_5 + yRot    |   NUM_PAD_6 + zRot
     NUM_PAD_1 - xRot   |   NUM_PAD_2 - yRot    |   NUM_PAD_3 - zRot
                        |                       |
     --------------------------------------------------------------------------
                        |                       |
     SCROLL_LOCK +scale | NUM_PAD_8 = reset     | L_SHIFT tap = false
        PAUSE - scale   | NUM_PAD_0 = print


     */


}