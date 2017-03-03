package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.model.ModelHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelPartSpec
{
    public ModelSpec modelSpec;
    public MorphTarget morph;
    public String partName;
    public EntityEquipmentSlot slot;
    public int defaultcolourindex; // NOT A COLOR but an index of a list
    public boolean defaultglow;
    public String displayName;
    IExtendedBlockState extendedState;

    public ModelPartSpec(ModelSpec modelSpec, MorphTarget morph, String partName, EntityEquipmentSlot slot, Integer defaultcolourindex, Boolean defaultglow, String displayName) {
        this.modelSpec = modelSpec;
        this.morph = morph;
        this.partName = partName;
        this.slot = slot;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
        this.displayName = displayName;
        // Extended state is used to isolate the quads for the model "group"(part) associated with this
        this.extendedState = ModelHelper.getStateForPart(partName, (OBJModel.OBJBakedModel) modelSpec.getModel());
    }

    public void renderPrep(VertexBuffer buffer, List<BakedQuad> quadList, int color) {
        for (BakedQuad quad : quadList) {
            buffer.addVertexData(quad.getVertexData());
            ForgeHooksClient.putQuadColor(buffer, quad, color);
        }
    }

    public void render(Colour color) {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        List<BakedQuad> quadList;
        for (EnumFacing facing : EnumFacing.values()) {
            quadList = modelSpec.getModel().getQuads(extendedState, facing, 0);;
            renderPrep(buffer, quadList, color.getInt());
        }
        quadList = modelSpec.getModel().getQuads(extendedState, null, 0);
        renderPrep(buffer, quadList, color.getInt());
        tess.draw();
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
//        this.setTexture(nbt, (tex != null) ? tex : "");
        this.setGlow(nbt, (glow != null) ? glow : false);
        this.setColourIndex(nbt, (c != null) ? c : defaultcolourindex);
        return nbt;
    }
}