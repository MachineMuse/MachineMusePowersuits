package net.machinemuse.powersuits.client.model.helper;

import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class ModelHelper {
    static {
        new ModelHelper();
    }

    // One pass just to register the textures called from texture stitch event
    // another to register the models called from model bake event (second run)
    public static void loadArmorModels(@Nullable TextureMap map) {
        ArrayList<String> resourceList = new ArrayList<String>() {{
            add("/assets/powersuits/modelspec/armor2.xml");
            add("/assets/powersuits/modelspec/default_armor.xml");
            add("/assets/powersuits/modelspec/default_armorskin.xml");
            add("/assets/powersuits/modelspec/armor_skin2.xml");
            add("/assets/powersuits/modelspec/default_powerfist.xml");
        }};

        for (String resourceString : resourceList) {
            parseSpecFile(resourceString, map);
        }

        ModelPowerFistHelper.INSTANCE.loadPowerFistModels(map);
    }

    public static void parseSpecFile(String resourceString, @Nullable TextureMap map) {
        URL resource = ModelHelper.class.getResource(resourceString);
        ModelSpecXMLReader.INSTANCE.parseFile(resource, map);
    }
}