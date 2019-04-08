package net.machinemuse.powersuits.client.model.item;

import com.google.common.collect.ImmutableList;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.client.helper.ModelHelper;
import net.machinemuse.powersuits.client.helper.ModelTransformCalibration;
import net.machinemuse.powersuits.client.render.modelspec.ModelPartSpec;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
import net.machinemuse.powersuits.client.render.modelspec.PartSpecBase;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.machinemuse.powersuits.utils.nbt.NBTTagAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

/**
 * Created by lehjr on 12/19/16.
 */
@SideOnly(Side.CLIENT)
public class ModelPowerFist implements IBakedModel {















//    @Override
//    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        if (side != null) return Collections.EMPTY_LIST;
//        try {
//            return ModelPowerFistHelper.INSTANCE.colouredPowerFistQuadMap.get(
//                    new ModelPowerFistHelper.PowerFistQuadMapKey(colour, modelcameraTransformType, isFiring));
//        } catch (Exception e) {
//            MuseLogger.logException("failed to loadButton get quads from cache: ", e);
//            return Collections.EMPTY_LIST;
//        }
//    }


    // todo: implement cache

//    private final LoadingCache<ModelPowerFist.CachKey, List<BakedQuad>> cache = CacheBuilder
//            .newBuilder()
//            .maximumSize(40)
//            .build(new CacheLoader<ModelPowerFist.CachKey, List<BakedQuad>>() {
//                @Override
//                public List<BakedQuad> loadButton(ModelPowerFist.CachKey key) throws Exception {
//                    return null;
//                }
//            });
//
//
//    static class CachKey {
//        public final String partName;
//        public final ItemCameraTransforms.TransformType transformType;
//        public final Colour colour;
//        public final boolean glow;
//
//        CachKey(final String partName,
//                final ItemCameraTransforms.TransformType transformType,
//                final Colour colour,
//                final boolean glow){
//            this.transformType=transformType;
//            this.partName=partName;
//            this.colour=colour;
//            this.glow=glow;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(partName, transformType, colour, glow);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (obj == null || getClass() != obj.getClass()) {
//                return false;
//            }
//            final CachKey other = (CachKey) obj;
//            return Objects.equals(this.partName, other.partName)
//                    && Objects.equals(this.transformType, other.transformType)
//                    && Objects.equals(this.colour, other.colour)
//                    && Objects.equals(this.glow, other.glow);
//        }
//    }



}