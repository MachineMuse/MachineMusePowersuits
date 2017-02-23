package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.general.NBTTagAccessor;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.powersuits.client.render.item.ArmorModelInstance;
import net.machinemuse.powersuits.client.render.item.IArmorModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelPart;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/6/16.
 */
public class RenderPart extends ModelRenderer {
    ModelRenderer parent;

    public RenderPart(ModelBase base, ModelRenderer parent) {
        super (base);
        this.parent = parent;
    }

    @Override
    public void render(float scale) {
        NBTTagCompound renderSpec = ((IArmorModel)(ArmorModelInstance.getInstance())).getRenderSpec();
        int[] colours = renderSpec.getIntArray("colours");
        int partColor;
        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            ModelPartSpec part = ModelRegistry.getInstance().getPart(nbt);
            if (part !=null) {
                //Todo: check some examples, like immersive engineering:
                // https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/render/TileRenderWatermill.java
                // https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/ClientUtils.java

                //-------------------------------
                float prevBrightX = OpenGlHelper.lastBrightnessX;
                float prevBrightY = OpenGlHelper.lastBrightnessY;

                // GLOW stuff on
                if (part.getGlow(nbt)) {
                    GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
                    RenderHelper.disableStandardItemLighting();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                }

                GL11.glPushMatrix();
                GL11.glScaled(scale, scale, scale);
                MuseTextureUtils.bindTexture(part.getTexture(nbt));
//                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                applyTransform();

                int ix = part.getColourIndex(nbt);
                if (ix < colours.length && ix >= 0) {
//                    Colour.doGLByInt(colours[ix]);
                    partColor = colours[ix];
                } else
                    partColor = Colour.WHITE.getInt();
                part.modelSpec.applyOffsetAndRotation(); // not yet implemented

//                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                part.render(partColor); // <---- render the thing
//                part.modelSpec.getModel().renderPart(part.partName);
                Colour.WHITE.doGL();
                GL11.glPopMatrix();

                // GLOW stuff off
                if (part.getGlow(nbt)) {
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevBrightX, prevBrightY);
                    GL11.glPopAttrib();
                }
            } else {
                System.out.println("part is null!!!!!");
            }
        }
    }

    private void applyTransform() {
//        float degrad = (float) (180F / Math.PI);
//        GL11.glTranslatef(rotationPointX, rotationPointY, rotationPointZ);
//        GL11.glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(rotateAngleX * degrad, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
        GL11.glTranslated(offsetX, offsetY - 26, offsetZ);
    }
}