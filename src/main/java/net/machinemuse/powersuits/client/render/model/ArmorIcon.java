package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leon on 4/10/17.
 */
public class ArmorIcon implements IBakedModel, IPerspectiveAwareModel {
    ItemCameraTransforms.TransformType cameraTransformType;
    ItemStack itemStack;
    Item item;
    Colour colour;
    World world;
    EntityLivingBase entity;
    IBakedModel iconModel;
    static Map<Colour, List<BakedQuad>> coloredQuadMap = new HashMap<>();

    public List<BakedQuad> getcoloredQuadList(Colour colour){
        List<BakedQuad> bakedQuadList =coloredQuadMap.get(colour);
        if (bakedQuadList == null) {
            bakedQuadList = iconModel.getQuads(null, null, 0);
            bakedQuadList = ModelHelper.getColoredQuads(bakedQuadList, colour);
            coloredQuadMap.put(colour, bakedQuadList);
        }
        return bakedQuadList;
    }

    public ArmorIcon(IBakedModel iconModelIn) {
        if (iconModelIn instanceof ArmorIcon)
            iconModel = ((ArmorIcon) iconModelIn).iconModel;
        else
            iconModel = iconModelIn;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        Matrix4f matrix;
        if (iconModel != null && iconModel instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) iconModel).handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == null)
            return getcoloredQuadList(colour);
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return iconModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return iconModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return iconModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return iconModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ArmorIconItemOverrideList();
    }

    public class ArmorIconItemOverrideList extends ItemOverrideList {
        public ArmorIconItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stackIn, World worldIn, EntityLivingBase entityIn) {
            itemStack = stackIn;
            world = worldIn;
            entity = entityIn;
            item = itemStack.getItem();
            colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
            return originalModel;
        }
    }

}
