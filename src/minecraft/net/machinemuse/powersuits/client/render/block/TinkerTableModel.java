package net.machinemuse.powersuits.client.render.block;

import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TinkerTableModel extends ModelBase {
    // public float onGround;
    // public boolean isRiding = false;
    protected ModelBase model = new ModelBase() {
    };

    private static final Random random = new Random();
    /**
     * This is a list of all the boxes (ModelRenderer.class) in the current
     * model.
     */
    // public List boxList = new ArrayList();
    // public boolean isChild = true;

    /**
     * A mapping for all texture offsets
     */
    // private Map modelTextureMap = new HashMap();
    // public int textureWidth = 64;
    // public int textureHeight = 32;

    ModelRenderer cube;
    ModelRenderer screen3;
    ModelRenderer screen2;
    ModelRenderer screen1;
    ModelRenderer middletable;
    ModelRenderer uppertable;
    ModelRenderer particles;
    ModelRenderer footbase;
    ModelRenderer foot1;
    ModelRenderer fatfoot2;
    ModelRenderer fatfoot1;
    ModelRenderer backsupport;
    ModelRenderer tank3;
    ModelRenderer tank2;
    ModelRenderer tank1;
    ModelRenderer wireshort4;
    ModelRenderer wireshort3;
    ModelRenderer wireshort2;
    ModelRenderer Wireshort1;
    ModelRenderer Wirelong1;

    /**
     *
     */
    public TinkerTableModel() {
        textureWidth = 112;
        textureHeight = 70;

        cube = new ModelRenderer(this, 96, 20);
        cube.addBox(-2F, -2F, -2F, 4, 4, 4);
        cube.setTextureSize(112, 70);
        cube.mirror = true;
        screen3 = new ModelRenderer(this, 1, 1);
        screen3.addBox(0F, 0F, 0F, 11, 0, 14);
        screen3.setRotationPoint(-9.666667F, 3.466667F, -7F);
        screen3.setTextureSize(112, 70);
        screen3.mirror = true;
        screen2 = new ModelRenderer(this, 0, 32);
        screen2.addBox(0F, 0F, 0F, 8, 0, 11);
        screen2.setRotationPoint(2F, 4.966667F, -6F);
        screen2.setTextureSize(112, 70);
        screen2.mirror = true;
        screen1 = new ModelRenderer(this, 3, 20);
        screen1.addBox(0F, 0F, 0F, 14, 0, 7);
        screen1.setRotationPoint(-6F, 2.466667F, 3F);
        screen1.setTextureSize(112, 70);
        screen1.mirror = true;
        setRotation(screen1, 0F, 0F, 0F);
        middletable = new ModelRenderer(this, 40, 49);
        middletable.addBox(-5F, 1F, -5F, 17, 3, 18);
        middletable.setRotationPoint(-4F, 10F, -4F);
        middletable.setTextureSize(112, 70);
        middletable.mirror = true;
        setRotation(middletable, 0F, 0F, 0F);
        uppertable = new ModelRenderer(this, 56, 28);
        uppertable.addBox(0F, 0F, 0F, 12, 5, 16);
        uppertable.setRotationPoint(-8F, 10F, -8F);
        uppertable.setTextureSize(112, 70);
        uppertable.mirror = true;
        setRotation(uppertable, 0F, 0F, 0F);
        particles = new ModelRenderer(this, 90, 0);
        particles.addBox(0F, 0F, 0F, 6, 7, 5);
        particles.setRotationPoint(-3F, 15F, -3F);
        particles.setTextureSize(112, 70);
        particles.mirror = true;
        setRotation(particles, 0F, 0F, 0F);
        footbase = new ModelRenderer(this, 0, 54);
        footbase.addBox(-5F, 8F, -5F, 12, 2, 11);
        footbase.setRotationPoint(-1F, 14F, -1F);
        footbase.setTextureSize(112, 70);
        footbase.mirror = true;
        setRotation(footbase, 0F, 0F, 0F);
        foot1 = new ModelRenderer(this, 82, 13);
        foot1.addBox(-5F, 8F, -5F, 4, 3, 3);
        foot1.setRotationPoint(-2F, 13F, 3F);
        foot1.setTextureSize(112, 70);
        foot1.mirror = true;
        setRotation(foot1, 0F, 0F, 0F);
        fatfoot2 = new ModelRenderer(this, 96, 13);
        fatfoot2.addBox(-5F, 8F, -5F, 4, 3, 4);
        fatfoot2.setRotationPoint(3F, 13F, 1F);
        fatfoot2.setTextureSize(112, 70);
        fatfoot2.mirror = true;
        setRotation(fatfoot2, 0F, 1.570796F, 0F);
        fatfoot1 = new ModelRenderer(this, 96, 13);
        fatfoot1.addBox(-5F, 8F, -5F, 4, 3, 4);
        fatfoot1.setRotationPoint(3F, 13F, -8F);
        fatfoot1.setTextureSize(112, 70);
        fatfoot1.mirror = true;
        setRotation(fatfoot1, 0F, 1.570796F, 0F);
        backsupport = new ModelRenderer(this, 38, 34);
        backsupport.addBox(0F, 0F, -2F, 2, 8, 7);
        backsupport.setRotationPoint(3F, 14F, -2F);
        backsupport.setTextureSize(112, 70);
        backsupport.mirror = true;
        setRotation(backsupport, 0F, 0F, 0F);
        tank3 = new ModelRenderer(this, 51, 18);
        tank3.addBox(0F, 0F, 0F, 3, 5, 3);
        tank3.setRotationPoint(6F, 10F, 3F);
        tank3.setTextureSize(112, 70);
        tank3.mirror = true;
        setRotation(tank3, 0F, 0F, 0F);
        tank2 = new ModelRenderer(this, 51, 18);
        tank2.addBox(0F, 0F, 0F, 3, 5, 3);
        tank2.setRotationPoint(6F, 10F, -2F);
        tank2.setTextureSize(112, 70);
        tank2.mirror = true;
        setRotation(tank2, 0F, 0F, 0F);
        tank1 = new ModelRenderer(this, 51, 18);
        tank1.addBox(0F, 0F, 0F, 3, 5, 3);
        tank1.setRotationPoint(6F, 10F, -7F);
        tank1.setTextureSize(112, 70);
        tank1.mirror = true;
        setRotation(tank1, 0F, 0F, 0F);
        wireshort4 = new ModelRenderer(this, 71, 15);
        wireshort4.addBox(0F, 0F, 5F, 1, 2, 1);
        wireshort4.setRotationPoint(7F, 15F, -1F);
        wireshort4.setTextureSize(112, 70);
        wireshort4.mirror = true;
        setRotation(wireshort4, 0F, 0F, 0F);
        wireshort3 = new ModelRenderer(this, 71, 15);
        wireshort3.addBox(0F, 0F, 0F, 1, 2, 1);
        wireshort3.setRotationPoint(7F, 15F, -6F);
        wireshort3.setTextureSize(112, 70);
        wireshort3.mirror = true;
        setRotation(wireshort3, 0F, 0F, 0F);
        wireshort2 = new ModelRenderer(this, 69, 13);
        wireshort2.addBox(0F, 0F, 0F, 2, 1, 1);
        wireshort2.setRotationPoint(5F, 17F, -1F);
        wireshort2.setTextureSize(112, 70);
        wireshort2.mirror = true;
        setRotation(wireshort2, 0F, 0F, 0F);
        Wireshort1 = new ModelRenderer(this, 71, 15);
        Wireshort1.addBox(0F, 0F, 0F, 1, 2, 1);
        Wireshort1.setRotationPoint(7F, 15F, -1F);
        Wireshort1.setTextureSize(112, 70);
        Wireshort1.mirror = true;
        setRotation(Wireshort1, 0F, 0F, 0F);
        Wirelong1 = new ModelRenderer(this, 77, 1);
        Wirelong1.addBox(0F, 0F, 0F, 1, 1, 11);
        Wirelong1.setRotationPoint(7F, 17F, -6F);
        Wirelong1.setTextureSize(112, 70);
        Wirelong1.mirror = true;
        setRotation(Wirelong1, 0F, 0F, 0F);
    }

    public void doRender(Entity entity, double x, double y, double z, float f,
                         float f1) {
        f = 0.0625f;
        MuseRenderer.blendingOn();
        int timestep = (int) ((System.currentTimeMillis()) % 10000);
        double angle = timestep * Math.PI / 5000.0;
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 1.0f, 0, 0);
        GL11.glTranslatef(0.5f, -1.5f, -0.5f);
        middletable.render(f);
        uppertable.render(f);
        footbase.render(f);
        foot1.render(f);
        fatfoot2.render(f);
        fatfoot1.render(f);
        backsupport.render(f);
        tank3.render(f);
        tank2.render(f);
        tank1.render(f);
        wireshort4.render(f);
        wireshort3.render(f);
        wireshort2.render(f);
        Wireshort1.render(f);
        Wirelong1.render(f);

        MuseRenderer.glowOn();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.5f, 1.05f, 0.5f);
        GL11.glTranslated(0, 0.02f * Math.sin(angle * 3), 0);
        // GLRotate uses degrees instead of radians for some reason grr
        GL11.glRotatef((float) (angle * 57.2957795131), 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(45f, 1.0f, 0.0f, 0.0f);
        // arctangent of 0.5.
        GL11.glRotatef(35.2643897f, 0, 1, 1);
        // cube.render(0.0625f);
        GL11.glColor4d(1, 1, 1, 0.8);
        cube.render(f / 2.0f);
        // cube.render(0.016000f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 1.0f, 0, 0);
        GL11.glTranslatef(0.5f, -1.5f, -0.5f);
        screen3.render(f);
        screen2.render(f);
        screen1.render(f);
        particles.render(f);
        GL11.glPopMatrix();
        // GL11.glPushMatrix();
        // if (Minecraft.getMinecraft().isFancyGraphicsEnabled()) {
        // if (f1 != 0) {
        // GL11.glDisable(GL11.GL_CULL_FACE);
        // for (int i = 0; i < 1; i++) {
        // drawScanLine(angle);
        // }
        // }
        // }
        // GL11.glPopMatrix();
        MuseRenderer.glowOff();
        MuseRenderer.blendingOff();
    }

    private void drawScanLine(double angle) {
        float xtarg = random.nextFloat();
        float ytarg = random.nextFloat();
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4d(0.0, 1.0, 0.2, 1.0);
        GL11.glVertex3d(0.5, 1.05 + 0.02f * Math.sin(angle * 3), 0.5);
        GL11.glVertex3d(xtarg, 1.2f, ytarg);
        GL11.glEnd();
        // MuseRenderer.drawGradientRect3D(
        // Vec3.createVectorHelper(Math.floor(xtarg * 16.0) / 16.0, 1.201,
        // Math.floor(ytarg * 16.0) / 16.0),
        // Vec3.createVectorHelper(1.0 / 16.0, 0, 1.0 / 16.0),
        // new Colour(0.0f, 1.0f, 0.0f, 0.4f),
        // new Colour(0.0f, 1.0f, 0.0f, 0.4f));
    }

    /**
     * Sets the model's various rotation angles then renders the model.
     */
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4,
                       float par5, float par6, float par7) {
        model.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }

    // @Override
    // protected void setTextureOffset(String par1Str, int par2, int par3)
    // {
    // this.modelTextureMap.put(par1Str, new TextureOffset(par2, par3));
    // }
    //
    // @Override
    // public TextureOffset getTextureOffset(String par1Str)
    // {
    // return (TextureOffset) this.modelTextureMap.get(par1Str);
    // }
    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
