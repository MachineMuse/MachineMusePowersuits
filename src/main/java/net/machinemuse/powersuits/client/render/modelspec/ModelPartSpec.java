package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.model.ModelHelper;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

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
    Map<EnumFacing, List<BakedQuad>> quadsPerFacing = new HashMap<>();

    public ModelPartSpec(ModelSpec modelSpec, MorphTarget morph, String partName, EntityEquipmentSlot slot, Integer defaultcolourindex, Boolean defaultglow, String displayName) {
        this.modelSpec = modelSpec;
        this.morph = morph;
        this.partName = partName;
        this.slot = slot;
        this.defaultcolourindex = (defaultcolourindex != null) ? defaultcolourindex : 0;
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
        this.displayName = displayName;
        this.extendedState = ModelHelper.getStateForPart(partName, (OBJModel.OBJBakedModel) modelSpec.getModel());
    }

    private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color) {
        for (BakedQuad bakedquad: quads) {
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, color);
        }
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, int color) {
        if (quadsPerFacing.get(side) == null) {
            for (EnumFacing facing: EnumFacing.values()) {
                quadsPerFacing.put(facing, modelSpec.getModel().getQuads(extendedState, facing, 0));
            }
            quadsPerFacing.put(null, modelSpec.getModel().getQuads(extendedState, null, 0));
        }
        return quadsPerFacing.get(side);
    }

    private List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, int color) {
        return ModelHelper.getColoredQuads(quadList, new Colour(color));
    }

    public void render(int color) {
        if (modelSpec.getModelpartList().contains(partName)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            //---
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            //------
            for (EnumFacing enumfacing : EnumFacing.values()) {
                this.renderQuads(vertexbuffer, getQuads((IBlockState) null, enumfacing, color), color);
            }
            this.renderQuads(vertexbuffer, getQuads((IBlockState) null, (EnumFacing) null, color), color);

            tessellator.draw();
            //------
            GlStateManager.popMatrix();
            //-------------------------------
        } else
            System.out.println("part name not in model part list: " + partName);


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