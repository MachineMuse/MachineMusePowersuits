package net.machinemuse.powersuits.client.modelspec;

import net.machinemuse.general.NBTTagAccessor;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.renderers.item.ArmorModelInstance;
import net.machinemuse.powersuits.client.renderers.item.IArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.List;

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
                if (part.slot == ((IArmorModel)(ArmorModelInstance.getInstance())).getVisibleSection() && part.morph.apply(ArmorModelInstance.getInstance()) == parent) {
                    List<BakedQuad> quadList = part.getQuads();
                    if (!quadList.isEmpty()) {
                        int ix = part.getColourIndex(nbt);
                        if (ix < colours.length && ix >= 0) partColor = colours[ix];
                        else partColor = Colour.WHITE.getInt();

                        // GLOW stuff on
                        if (part.getGlow(nbt)) RenderState.glowOn();

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(scale, scale, scale);
                        applyTransform();
                        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        part.modelSpec.applyOffsetAndRotation(); // not yet implemented

                        Tessellator tess = Tessellator.getInstance();
                        BufferBuilder buffer = tess.getBuffer();
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

                        for (BakedQuad quad : part.getQuads()) {
                            buffer.addVertexData(quad.getVertexData());
                            ForgeHooksClient.putQuadColor(buffer, quad, partColor);
                        }
                        tess.draw();

                        GlStateManager.popMatrix();

                        //Glow stuff off
                        if (part.getGlow(nbt)) RenderState.glowOff();
                    }
                }
            }
        }
    }

    private void applyTransform() {
//        float degrad = (float) (180F / Math.PI);
//        GL11.glTranslatef(rotationPointX, rotationPointY, rotationPointZ);
//        GL11.glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(rotateAngleX * degrad, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(offsetX, offsetY - 26, offsetZ);
    }
}