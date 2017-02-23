package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/18/17.
 *
 * This is isn't exactly pretty.
 */
public class PartNameToQuads {
//    IBakedModel model;
//
//    // Map of quads per group
//    Map<String, Map<EnumFacing, List<BakedQuad>>> quadsPerGroup = new HashMap<>();
//    public PartNameToQuads(ResourceLocation resourceLocationIn) {
//        this.model = ModelRegistry.getInstance().loadModel(resourceLocationIn);
//        System.out.println("location: " + resourceLocationIn.toString());
//    }
//
//
//
//    public Map<String, Map<EnumFacing, List<BakedQuad>>> getQuadMapPerModelGroup() {
//        Map<EnumFacing, List<BakedQuad>> quadsPerFacing;
//        List<String> groupList = new ArrayList<String>(model.getMatLib().getGroups().keySet());
//
//
//        for (String groupName : groupList) {
//            System.out.println("groupname: " + groupName);
//            quadsPerFacing = new QuadsForPart(groupList, groupName, model).getQuads();
//            quadsPerGroup.put(groupName, quadsPerFacing);
//        }
//        return quadsPerGroup;
//    }
//
//    public class QuadsForPart {
//        List<String> hidden;
//        String visible;
//        OBJModel model;
//        IBakedModel bakedModel;
//
//        public QuadsForPart(List<String> hiddenIn, String visibleIn, OBJModel modelIn) {
//            this.hidden = hiddenIn;
//            visible= visibleIn;
//            this.model = modelIn;
//            hidden.remove(visible);
//
//            for (String materialName: modelIn.getMatLib().getMaterialNames()) {
//                OBJModel.Material material = modelIn.getMatLib().getMaterial(materialName);
//                System.out.println("material:  " + materialName + " ---texture: " + material.getTexture().getTextureLocation().toString());
//                ResourceLocation location = material.getTexture().getTextureLocation();
//
//                if (location.getResourceDomain() == ModularPowersuits.MODID.toLowerCase()) {
//                    Minecraft mc = Minecraft.getMinecraft();
//                    try {
//                        mc.getTextureMapBlocks().registerSprite(material.getTexture().getTextureLocation());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//
//
//
////            for (Map.Entry<String, OBJModel.Material> e : modelIn.getMatLib().getMaterialNames()
////
////
////                    .materials.entrySet())
////            {
////                if (e.getValue().getTexture().getTextureLocation().getResourcePath().startsWith("#"))
////                {
////                    FMLLog.severe("OBJLoader: Unresolved texture '%s' for obj model '%s'", e.getValue().getTexture().getTextureLocation().getResourcePath(), modelLocation);
////                    builder.put(e.getKey(), missing);
////                }
////                else
////                {
////                    builder.put(e.getKey(), bakedTextureGetter.apply(e.getValue().getTexture().getTextureLocation()));
////                }
////            }
////
//
//
//
//
//            System.out.println("model texture: " + modelIn.getTextures().toString());
//
//
//

//
////                bakedModel = model.bake(state, DefaultVertexFormats.ITEM,
////                            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//
//
//                bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
//                        location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//
//            // this whole thing might me loading too early
//
////                this.bakedModel = modelIn.bake (
////                        TRSRTransformation.identity(),
////                        //state,
////                        DefaultVertexFormats.ITEM,
////                        new Function<ResourceLocation, TextureAtlasSprite>() {
////                            @Override
////                            public TextureAtlasSprite apply (ResourceLocation location) {
////                                Minecraft mc = Minecraft.getMinecraft ();
////                                System.out.println("texture location is: " + location.toString());
////                                return mc.getTextureMapBlocks().getAtlasSprite (location.toString());
////                            }
////                        });
//            } catch (Exception e) {
//                e.printStackTrace ();
//            }
//        }
//
//        private Map<EnumFacing, List<BakedQuad>> getQuads() {
//            Map<EnumFacing, List<BakedQuad>> retMap = new HashMap<>();
//
//            for (EnumFacing facing:  EnumFacing.values()) {
//                retMap.put(facing, this.bakedModel.getQuads(null, facing, 0));
//            }
//            retMap.put((EnumFacing)null, this.bakedModel.getQuads(null, (EnumFacing)null, 0));
//
//            return retMap;
//        }
//    }
}