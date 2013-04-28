package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.powersuits.client.render.ModelPartSpec;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class ArmorModel extends ModelBiped {
    protected static ArmorModel instance;
    public Colour normalcolour;
    public Colour glowcolour;

    public static ArmorModel getInstance() {
        if (instance == null) {
            instance = new ArmorModel();
        }
        return instance;
    }

    public WavefrontObject armorHelm;
    public WavefrontObject armorArms;
    public WavefrontObject armorChest;
    public WavefrontObject armorLegs;
    public WavefrontObject armorBoots;

    public int visible;

    public ArmorModel() {
        this(0.0F, 0.0f, 64, 32);
    }

    private void logModelParts(IModelCustom modelc) {
        if (modelc instanceof WavefrontObject) {
            WavefrontObject model = (WavefrontObject) modelc;
            MuseLogger.logDebug(model.toString() + ":");
            for (GroupObject group : model.groupObjects) {
                MuseLogger.logDebug("-" + group.name);
            }
        }

    }

    public ArmorModel(float par1, float par2, int par3, int par4) {
        // New stuff
        try {
            this.armorHelm = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_helm.obj");
            this.armorArms = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_arms.obj");
            this.armorChest = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_chest.obj");
            this.armorLegs = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_pantaloons.obj");
            this.armorBoots = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_boots.obj");
        } catch (ModelFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // logModelParts(armorHelm);
        // logModelParts(armorArms);
        // logModelParts(armorChest);
        // logModelParts(armorLegs);
        // logModelParts(armorBoots);
        setInitialOffsets(bipedHead, 0.0F, 0.0F + par2, 0.0F);
        setInitialOffsets(bipedBody, 0.0F, 0.0F + par2, 0.0F);
        setInitialOffsets(bipedRightArm, 5, 2.0F + par2, 0.0F);
        setInitialOffsets(bipedLeftArm, -5, 2.0F + par2, 0.0F);
        setInitialOffsets(bipedRightLeg, 2, 12.0F + par2, 0.0F);
        setInitialOffsets(bipedLeftLeg, -2, 12.0F + par2, 0.0F);
    }

    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.field_82906_o = x;
        r.field_82907_q = y;
        r.field_82908_p = z;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
        try {
            EntityLiving entLive = (EntityLiving) entity;
            ItemStack stack = entLive.getCurrentItemOrArmor(0);
            this.heldItemRight = (stack != null) ? 1 : 0;
            this.isSneak = entLive.isSneaking();
            EntityPlayer entityPlayer;
            this.aimedBow = ((EntityPlayer) entLive).getItemInUse() != null;
            // if (entity.)
        } catch (Exception e) {
        }
        this.setRotationAngles(par2, par3, par4, par5, par6, scale, entity);
        Minecraft.getMinecraft().renderEngine.bindTexture(Config.ARMOR_TEXTURE_PATH);
        GL11.glPushMatrix();
        if (visible == 0) {
            renderParts(scale, bipedHead, armorHelm, normalcolour, false, "helm_main;helm_tube_entry1;helm_main;helm_tubes;helm_tube_entry2".split(";"));
            renderParts(scale, bipedHead, armorHelm, glowcolour, true, "visor".split(";"));
        }

        if (visible == 1) {
            renderParts(scale, bipedBody, armorChest, normalcolour, false, "belt;chest_main;polySurface36;backpack;chest_padding".split(";"));
            renderParts(scale, bipedBody, armorChest, glowcolour, true, "crystal_belt".split(";"));


            renderParts(scale, bipedRightArm, armorArms, normalcolour, false, "arms3".split(";"));
            renderParts(scale, bipedRightArm, armorArms, glowcolour, true, "crystal_shoulder_2".split(";"));

            renderParts(scale, bipedLeftArm, armorArms, normalcolour, false, "arms2".split(";"));
            renderParts(scale, bipedLeftArm, armorArms, glowcolour, true, "crystal_shoulder_1".split(";"));
        }
        if (visible == 2) {
            renderParts(scale, bipedRightLeg, armorLegs, normalcolour, false, "leg1".split(";"));
//        renderParts(scale, bipedRightLeg, armorLegs, glowcolour, true, "visor".split(";"));

            renderParts(scale, bipedLeftLeg, armorLegs, normalcolour, false, "leg2".split(";"));
//        renderParts(scale, bipedLeftLeg, armorLegs, glowcolour, true, "visor".split(";"));
        }
        if (visible == 3) {
            renderParts(scale, bipedRightLeg, armorBoots, normalcolour, false, "boots1".split(";"));
//        renderParts(scale, bipedRightLeg, armorLegs, glowcolour, true, "visor".split(";"));

            renderParts(scale, bipedLeftLeg, armorBoots, normalcolour, false, "boots2".split(";"));
//        renderParts(scale, bipedLeftLeg, armorLegs, glowcolour, true, "visor".split(";"));
        }

        GL11.glPopMatrix();
    }

    public void renderParts(float scale, ModelRenderer binding, WavefrontObject model, Colour colour, boolean glow, String[] parts) {

        if (!binding.isHidden && binding.showModel) {
            GL11.glPushMatrix();
            GL11.glTranslatef(binding.rotationPointX * scale, binding.rotationPointY * scale, binding.rotationPointZ * scale);
            GL11.glRotatef(binding.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(binding.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(binding.rotateAngleX * (180F / (float) Math.PI) + 180, 1.0F, 0.0F, 0.0F);

            GL11.glTranslated(
                    binding.field_82906_o * scale,
                    binding.field_82907_q * scale - 1.625,
                    binding.field_82908_p * scale
            );
            GL11.glScaled(scale, scale, scale);
            new ModelPartSpec(model, parts, colour, glow, "Parts").render();

            GL11.glPopMatrix();

        }

    }
}
