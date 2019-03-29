package net.machinemuse.numina.client.model.helper;

import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ModelTransformCalibration {
    public static int xtap;
    public static int ytap;
    public static int ztap;
    public static float xOffest;
    public static float yOffest;
    public static float zOffest;
    public static float scalemodifier;
    public static boolean tap;


    public ModelTransformCalibration() {
        this.xtap = 0;
        this.ytap = 0;
        this.ztap = 0;
        this.xOffest = 0;
        this.yOffest = 0;
        this.zOffest = 0;
        this.scalemodifier = 0.625f;
        this.tap = false;
    }

    //----------------------------------
    /*
     * Only used for setting up scale, rotation, and relative placement coordinates
     */
    public static void transformCalibration() {
        int numsegments = 16;
        if (!tap) {

            if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
                xOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                xOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
                yOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
                yOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
                zOffest += 0.1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
                zOffest -= 0.1;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
                xtap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                ytap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
                ztap += 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
                xtap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
                ytap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                ztap -= 1;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
                xtap = 0;
                ytap = 0;
                ztap = 0;

                xOffest = 0;
                yOffest = 0;
                zOffest = 0;

                scalemodifier = 1;

                tap = true;
            }
            // this probably needs a bit more work, int's are too big.
            if (Keyboard.isKeyDown(Keyboard.KEY_SCROLL)) {
                scalemodifier -= 0.01f;
                tap = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_PAUSE)) {
                scalemodifier += 0.01f;
                tap = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
                System.out.println("xrot: " + xtap + ", yrot: " + ytap + ", zrot: " + ztap);

                System.out.println("xOffest: " + xOffest + ", yOffest: " + yOffest + ", zOffest: " + zOffest);

                System.out.println("scaleModifier: " + scalemodifier);

                tap = true;
            }
        } else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)
                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)
                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                tap = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tap = false;
            }
        }
    }

    public TRSRTransformation getTransform() {
        transformCalibration();
        return MuseModelHelper.get(xOffest, yOffest, zOffest, xtap, ytap, ztap, scalemodifier);
    }
}
