package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;

//import net.minecraftforge.client.model.obj.OBJModel;
//
/**
 * Created by lehjr on 12/3/16.
 */
public class OBJMultiModel extends OBJModel {
//    public OBJMultiModel(MaterialLibrary matLib, ResourceLocation modelLocation, OBJModel.CustomData customData) {
//        super(matLib, modelLocation, customData);
//    }

    public OBJMultiModel(MaterialLibrary matLib, ResourceLocation modelLocation) {
        super(matLib, modelLocation);
    }




    public class OBJMultiBakedModel extends OBJBakedModel {

        public OBJMultiBakedModel(OBJModel this$0, IModelState model, VertexFormat state, ImmutableMap<String, TextureAtlasSprite> format) {
            super(this$0, model, state, format);
        }
    }



}


/*

    Time to figure out what information is needed here.
    ====================================================

    Lux Capacitor
   ---------------------------------------------------
         * render hooks -> item/entity/block
         * color individual parts but fetch as a whole
         * blockstate rotations
         * scale/"transform" (move the model to the correct location)
         *

    Armor Models
   ---------------------------------------------------
         * render hooks -> item(Inventory Icon)/ArmorSlot (actual model)
         * color individual parts but fetch as modelBiped Locations (leftArm, rightArm...)



    PowerFist
   ---------------------------------------------------
          * render hooks -> (Inventory Icon)/LeftHand/RightHand (actual model)
          * color individual parts, fetch as a whole model















 */