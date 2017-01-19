package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;

/**
 * Ported to Java by lehjr on 12/27/16.
 */
public class ModelLuxCapacitor implements IBakedModel, IPerspectiveAwareModel {
    int lensColor;
    IBakedModel baseModel;
    Colour defaultItemColour = new Colour(0.4F, 0.2F, 0.9F);



    ItemCameraTransforms.TransformType cameraTransformType;

    Map<Colour, IBakedModel> modelCache = new HashMap<>();




    public ModelLuxCapacitor(IBakedModel baseModel, int lensColor) {
        this.lensColor = lensColor;
        this.baseModel = baseModel;
    }

    public IBakedModel getColoredModel(Colour colour) {
//        System.out.println("looking for color: " + colour.hexColour());


        IBakedModel colouredModel = modelCache.get(colour);
        if (colouredModel == null) {
            OBJModel.OBJBakedModel bakedOBJ = (OBJModel.OBJBakedModel) baseModel;
//            bakedOBJ.getModel().getMatLib().getMaterial("LensMaterial").setColor(colour.toVector4f());
//            bakedOBJ.scheduleRebake();
            colouredModel = bakedOBJ;
            modelCache.put(colour, colouredModel);
        }
        return colouredModel;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        this.cameraTransformType = cameraTransformTypeIn;
        Matrix4f matrix;

        if (baseModel != null && baseModel instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) baseModel).handlePerspective(cameraTransformTypeIn).getValue();

//            System.out.println("model matrix: " + matrix.toString());



        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }





        /*
            matrix is used for transform


         */



        // TRSRTransformation(matrix);



        return Pair.of(this, matrix);
    }

    public void setLensColor(Colour colour) {

    }




    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        /*
            TODO: add some code for coloring the lens with a workaround for null blockstates for items and entities
         */


        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;





        List<BakedQuad> combinedQuadsList = new ArrayList(baseModel.getQuads(state, side, rand));


//        for (BakedQuad burntQuads: combinedQuadsList) {
//            if (burntQuads.hasTintIndex())
//                System.out.println("baked quad tint index is " + burntQuads.getTintIndex());
//            else
//                System.out.println("baked quad has no tint index");
//            System.out.println("baked quad vertex format is " + burntQuads.getFormat().toString());
//        }

//[11:58:13] [Client thread/INFO]: [net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor:getQuads:93]: baked quad vertex format is format: 5 elements: 3,Position,Float 4,Vertex Color,Unsigned Byte 2,UV,Float 3,Normal,Byte 1,Padding,Byte
//                [11:58:13] [Client thread/INFO]: [net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor:getQuads:92]: baked quad has no tint index
//
//

        if (side != null || state == null) {
            return getColoredModel(defaultItemColour).getQuads(state, side, rand);


//            return baseModel.getQuads(state, side, rand);
        }

        Colour colour =  ((IExtendedBlockState)state).getValue(BlockLuxCapacitor.COLOR);

        System.out.println("Color is: " + colour.hexColour());



        return getColoredModel(colour).getQuads(state, side, rand);



//        if (state instanceof IExtendedBlockState)
//            System.out.println("color is " + ((IExtendedBlockState)state).getValue(BlockLuxCapacitor.COLOR).hexColour());
//        UnlistedPropertyColor thingie = ((BlockLuxCapacitor)(state.getBlock())).COLOR;
//        System.out.println("color is " + (thingie.getName()));



//        combinedQuadsList.addAll(getChessPiecesQuads(numberOfChessPieces));



//        return combinedQuadsList;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseModel.getOverrides();
    }
}