package net.machinemuse.powersuits.client.modelspec;

import net.machinemuse.numina.utils.NBTTagAccessor;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.renderers.item.HighPolyArmor;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/6/16.
 */
@SideOnly(Side.CLIENT)
public class RenderPart extends ModelRenderer {
    ModelRenderer parent;

    public RenderPart(ModelBase base, ModelRenderer parent) {
        super (base);
        this.parent = parent;
    }
    @Override
    public void render(float scale) {
        NBTTagCompound renderSpec = HighPolyArmor.getInstance().getRenderSpec();
        int[] colours = renderSpec.getIntArray("colours");
        int partColor;
        List<BakedQuad> quadList;
        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            System.out.println("nbt: " + nbt.toString());

            PartSpec partSpec = ModelRegistry.getInstance().getPart(nbt);


            if (partSpec != null && partSpec instanceof ModelPartSpec) {
                if (partSpec.binding.getSlot().equals(HighPolyArmor.getInstance().getVisibleSection())
                        && partSpec.binding.getTarget().apply(HighPolyArmor.getInstance()).equals(parent)) {
                        quadList = ((ModelPartSpec)partSpec).getQuads();


                    if (quadList != null && !quadList.isEmpty()) {
                        int ix = partSpec.getColourIndex(nbt);
                        if (ix < colours.length && ix >= 0) partColor = colours[ix];
                        else partColor = Colour.WHITE.getInt();

                        // GLOW stuff on
                        if (((ModelPartSpec) partSpec).getGlow(nbt))
                            RenderState.glowOn();

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(scale, scale, scale);
                        applyTransform();
                        if (partSpec instanceof ModelPartSpec)
                            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        else if (partSpec instanceof TexturePartSpec)
                            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(((TexturePartSpec) partSpec).textureLocation));


                        Tessellator tess = Tessellator.getInstance();
                        BufferBuilder buffer = tess.getBuffer();
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

                        for (BakedQuad quad : quadList) {
                            buffer.addVertexData(quad.getVertexData());
                            ForgeHooksClient.putQuadColor(buffer, quad, partColor);
                        }

                        tess.draw();
                        GlStateManager.popMatrix();

                        //Glow stuff off
                        if (((ModelPartSpec) partSpec).getGlow(nbt))
                            RenderState.glowOff();
                    }
                }
            }
        }
    }

    private void applyTransform() {
        GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(offsetX, offsetY - 26, offsetZ);
    }
}