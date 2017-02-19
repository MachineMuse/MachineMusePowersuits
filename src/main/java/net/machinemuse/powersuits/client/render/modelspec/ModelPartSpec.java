package net.machinemuse.powersuits.client.render.modelspec;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelPartSpec
{
    public ModelSpec modelSpec;
    public MorphTarget morph;
    public String partName;
    public EntityEquipmentSlot slot;
    public int defaultcolourindex;
    public boolean defaultglow;
    public String displayName;
    Map<EnumFacing, List<BakedQuad>> quadsPerFacing;



    public ModelPartSpec(ModelSpec modelSpec, MorphTarget morph, String partName, EntityEquipmentSlot slot, Integer defaultcolourindex, Boolean defaultglow, String displayName) {
        this.modelSpec = modelSpec;
        this.morph = morph;
        this.partName = partName;
        this.slot = slot;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
        this.displayName = displayName;
    }

    public void render(){
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.renderQuads(vertexbuffer, quadsPerFacing.get(enumfacing), defaultcolourindex);
        }

        this.renderQuads(vertexbuffer, quadsPerFacing.get((EnumFacing) null), defaultcolourindex);
        tessellator.draw();

    }

    private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color)
    {
//        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = (BakedQuad)quads.get(i);
            int k = color;

//            if (flag && bakedquad.hasTintIndex())
//            {
//                k = this.itemColors.getColorFromItemstack(stack, bakedquad.getTintIndex());
//
//                if (EntityRenderer.anaglyphEnable)
//                {
//                    k = TextureUtil.anaglyphColor(k);
//                }
//
//                k = k | -16777216;
//            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }






    public String getTexture(NBTTagCompound nbt) {
        return (nbt.hasKey("texture") ?  nbt.getString("texture") :  modelSpec.textures[0]);
    }

    public void setTexture(NBTTagCompound nbt, String s) {
        if (s.equals("") || s.equalsIgnoreCase(modelSpec.textures[0])) {
            nbt.removeTag("texture");
        }
        else {
            nbt.setString("texture", s);
        }
    }

    public int getColourIndex(NBTTagCompound nbt) {
        return nbt.hasKey("colourindex") ? nbt.getInteger("colourindex") : this.defaultcolourindex;
    }

    public void setColourIndex(NBTTagCompound nbt, int c) {
        if (c == this.defaultcolourindex) {
            nbt.removeTag("colourindex");
        }
        else {
            nbt.setInteger("colourindex", c);
        }
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey("glow") ? nbt.getBoolean("glow") : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) {
            nbt.removeTag("glow");
        }
        else {
            nbt.setBoolean("glow", g);
        }
    }

    public void setModel(NBTTagCompound nbt, ModelSpec model) {
        String modelString = ModelRegistry.getInstance().inverse().get(model);
        setModel(nbt, ((modelString != null) ? modelString : ""));
    }

    public void setModel(NBTTagCompound nbt, String modelname) {
        nbt.setString("model", modelname);
    }

    public void setPart(NBTTagCompound nbt) {
        nbt.setString("part", this.partName);
    }

    public void setPartAndModel(NBTTagCompound nbt) {
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, String tex, Boolean glow, Integer c) {
        this.setPart(nbt);
        this.setModel(nbt, this.modelSpec);
        this.setTexture(nbt, (tex != null) ? tex : "");
        this.setGlow(nbt, (glow != null) ? glow : false);
        this.setColourIndex(nbt, (c != null) ? c : defaultcolourindex);
        return nbt;
    }
}