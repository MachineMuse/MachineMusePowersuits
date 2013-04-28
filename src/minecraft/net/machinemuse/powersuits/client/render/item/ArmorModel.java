package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
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
        } catch (ModelFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // logModelParts(armorHelm);
        // logModelParts(armorArms);
        // logModelParts(armorChest);
        // logModelParts(armorLegs);
        // logModelParts(armorBoots);

        this.bipedHead = new ArmorPartRenderer(this, armorHelm, "helm_main;helm_tube_entry1;helm_main;helm_tubes;helm_tube_entry2", "visor")
                .setInitialOffsets(0.0F, 0.0F + par2, 0.0F);

        this.bipedBody = new ArmorPartRenderer(this, armorChest, "belt;chest_main;polySurface36;backpack;chest_padding", "crystal_belt")
                .setInitialOffsets(0.0F, 0.0F + par2, 0.0F);

        this.bipedRightArm = new ArmorPartRenderer(this, armorArms, "arms3", "crystal_shoulder_2")
                .setInitialOffsets(-5, 2.0F + par2, 0.0F);

        this.bipedLeftArm = new ArmorPartRenderer(this, armorArms, "arms2", "crystal_shoulder_1")
                .setInitialOffsets(5, 2.0F + par2, 0.0F);

        this.bipedRightLeg = new ArmorPartRenderer(this, armorLegs, "leg1", "")
                .setInitialOffsets(0, 12.0F + par2, 0.0F);

        this.bipedLeftLeg = new ArmorPartRenderer(this, armorLegs, "leg2", "")
                .setInitialOffsets(0, 12.0F + par2, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        try {
            EntityLiving entity = (EntityLiving) par1Entity;
            ItemStack stack = entity.getCurrentItemOrArmor(0);
            this.heldItemRight = (stack != null) ? 1 : 0;
            this.isSneak = entity.isSneaking();
            EntityPlayer entityPlayer;
            this.aimedBow = ((EntityPlayer) entity).getItemInUse() != null;
            // if (entity.)
        } catch (Exception e) {
        }
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        Minecraft.getMinecraft().renderEngine.bindTexture(Config.ARMOR_TEXTURE_PATH);
        GL11.glPushMatrix();
        this.bipedHead.render(par7);
        this.bipedBody.render(par7);
        this.bipedRightArm.render(par7);
        this.bipedLeftArm.render(par7);
        this.bipedRightLeg.render(par7);
        this.bipedLeftLeg.render(par7);
        GL11.glPopMatrix();
    }
}
