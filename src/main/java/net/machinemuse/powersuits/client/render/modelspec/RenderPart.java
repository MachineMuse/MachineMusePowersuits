package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.client.render.modelspec.ModelPartSpec;
import net.machinemuse.numina.client.render.modelspec.ModelRegistry;
import net.machinemuse.numina.client.render.modelspec.PartSpecBase;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.client.model.item.armor.ArmorModelInstance;
import net.machinemuse.powersuits.client.model.item.armor.IArmorModel;
import net.machinemuse.powersuits.utils.nbt.NBTTagAccessor;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 * <p>
 * Ported to Java by lehjr on 11/6/16.
 */
@SideOnly(Side.CLIENT)
public class RenderPart extends ModelRenderer {
    ModelRenderer parent;

    public RenderPart(ModelBase base, ModelRenderer parent) {
        super(base);
        this.parent = parent;
    }

    @Override
    public void render(float scale) {
        NBTTagCompound renderSpec = ((IArmorModel) (ArmorModelInstance.getInstance())).getRenderSpec();
        if (renderSpec == null)
            return;

        int[] colours = renderSpec.getIntArray(ModelSpecTags.TAG_COLOURS);
        if (colours.length == 0)
            colours = new int[]{Colour.WHITE.getInt()};

        int partColor;
        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            PartSpecBase part = ModelRegistry.getInstance().getPart(nbt);
            if (part != null && part instanceof ModelPartSpec) {
                if (part.getBinding().getSlot() == ((IArmorModel) (ArmorModelInstance.getInstance())).getVisibleSection()
                        && part.getBinding().getTarget().apply(ArmorModelInstance.getInstance()) == parent) {
                    List<BakedQuad> quadList = ((ModelPartSpec) part).getQuads();
                    if (!quadList.isEmpty()) {
                        int ix = part.getColourIndex(nbt);

                        // checks the range of the index to avoid errors OpenGL or crashing
                        if (ix < colours.length && ix >= 0) partColor = colours[ix];
                        else partColor = Colour.WHITE.getInt();

                        // GLOW stuff on
                        if (((ModelPartSpec) part).getGlow(nbt))
                            RenderState.glowOn();

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(scale, scale, scale);
                        applyTransform();
                        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        Tessellator tess = Tessellator.getInstance();
                        BufferBuilder buffer = tess.getBuffer();
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

                        for (BakedQuad quad : ((ModelPartSpec) part).getQuads()) {
                            buffer.addVertexData(quad.getVertexData());
                            ForgeHooksClient.putQuadColor(buffer, quad, partColor);
                        }
                        tess.draw();

                        GlStateManager.popMatrix();

                        //Glow stuff off
                        if (((ModelPartSpec) part).getGlow(nbt))
                            RenderState.glowOff();
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