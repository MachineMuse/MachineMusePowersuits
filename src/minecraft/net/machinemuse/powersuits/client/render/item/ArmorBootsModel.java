package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class ArmorBootsModel extends ArmorModel {
    protected static ArmorBootsModel instance;

    public static ArmorBootsModel getInstance() {
        if (instance == null) {
            instance = new ArmorBootsModel();
        }
        return instance;
    }

    public WavefrontObject armorBoots;

    public ArmorBootsModel() {
        this(0.0F, 0.0f, 64, 32);
    }

    public ArmorBootsModel(float par1, float par2, int par3, int par4) {
        // New stuff
        this.armorBoots = (WavefrontObject) AdvancedModelLoader.loadModel("/mods/mmmPowersuits/models/mps_boots.obj");

        this.bipedRightLeg = new ArmorPartRenderer(this, armorBoots, "boots1", "")
                .setInitialOffsets(0, 12.0F + par2, 0.0F);

        this.bipedLeftLeg = new ArmorPartRenderer(this, armorBoots, "boots2", "")
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
        this.bipedRightLeg.render(par7);
        this.bipedLeftLeg.render(par7);
        GL11.glPopMatrix();
    }

}
