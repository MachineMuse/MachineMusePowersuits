package net.machinemuse.powersuits.client.render.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.helper.ModelHelper;
import net.machinemuse.powersuits.item.*;
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
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by leon on 4/10/17.
 */
public class ArmorIcon implements IBakedModel {
    ItemCameraTransforms.TransformType cameraTransformType;
    ItemStack itemStack;
    Item item;
    Colour colour;
    World world;
    EntityLivingBase entity;
    static IBakedModel headIconModel;
    static IBakedModel chestIconModel;
    static IBakedModel legsIconModel;
    static IBakedModel feetIconModel;

    static LoadingCache<Colour, List<BakedQuad>> armorHeadIconCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(headIconModel.getQuads(null, null,0), colour);
                }
            });

    static LoadingCache<Colour, List<BakedQuad>> armorChestIconCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(chestIconModel.getQuads(null, null,0), colour);
                }
            });

    static LoadingCache<Colour, List<BakedQuad>> armorLegsIconCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(legsIconModel.getQuads(null, null,0), colour);
                }
            });

    static LoadingCache<Colour, List<BakedQuad>> armorFeetIconCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<Colour, List<BakedQuad>>() {
                public List<BakedQuad> load(Colour colour) { // no checked exception //  throws Exception {
                    return ModelHelper.getColoredQuads(feetIconModel.getQuads(null, null,0), colour);
                }
            });

    public ArmorIcon(IBakedModel headIconModelIn,
                     IBakedModel chestIconModelIn,
                     IBakedModel legsIconModelIn,
                     IBakedModel feetIconModelIn) {
        if (headIconModelIn instanceof ArmorIcon)
            headIconModel = ((ArmorIcon) headIconModelIn).headIconModel;
        else
            headIconModel = headIconModelIn;

        if (chestIconModelIn instanceof ArmorIcon)
            chestIconModel = ((ArmorIcon) chestIconModelIn).chestIconModel;
        else
            chestIconModel = chestIconModelIn;

        if (legsIconModelIn instanceof ArmorIcon)
            legsIconModel = ((ArmorIcon) legsIconModelIn).legsIconModel;
        else
            legsIconModel = legsIconModelIn;

        if (feetIconModelIn instanceof ArmorIcon)
            feetIconModel = ((ArmorIcon) feetIconModelIn).feetIconModel;
        else
            feetIconModel = feetIconModelIn;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        Matrix4f matrix;
        if (headIconModel != null) {
            matrix = headIconModel.handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == null)
            try {
                if( item instanceof ItemPowerArmorHelmet)
                    return armorHeadIconCache.get(colour);
                else if (item instanceof ItemPowerArmorChestplate)
                    return armorChestIconCache.get(colour);
                else if(item instanceof ItemPowerArmorLeggings)
                    return armorLegsIconCache.get(colour);
                else if (item instanceof ItemPowerArmorBoots)
                    return armorFeetIconCache.get(colour);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return headIconModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return headIconModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return headIconModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return headIconModel.getItemCameraTransforms();
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
