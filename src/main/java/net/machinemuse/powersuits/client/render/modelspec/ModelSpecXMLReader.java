package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.model.MPSOBJLoader;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.net.URL;

import static net.machinemuse.powersuits.client.render.modelspec.MorphTarget.*;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
@SideOnly(Side.CLIENT)
public class ModelSpecXMLReader {
    boolean registerModels;

    private ModelSpecXMLReader() {
    }

    private static ModelSpecXMLReader INSTANCE;

    public static ModelSpecXMLReader getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ModelSpecXMLReader();
        return INSTANCE;
    }

    public void parseFile(URL file, boolean registerModelsIn) {
        this.registerModels = registerModelsIn;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xml = dBuilder.parse(new InputSource(file.openStream()));
            xml.getDocumentElement().normalize();

            NodeList modelNodeList = xml.getElementsByTagName("model");
            for (int temp = 0; temp < modelNodeList.getLength(); temp++) {
                Node modelNode = modelNodeList.item(temp);

                if (modelNode.getNodeType() == Node.ELEMENT_NODE) {
                    parseModel(modelNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseModel(Node modelnode) {
        String file;
//        String[] texturesArray;
        Vec3d offset;
        Vec3d rotation;

        if (modelnode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) modelnode;
            file = eElement.getAttribute("file");
            // this is just used for texture registration now.
//            texturesArray = eElement.getAttribute("textures").split(",");

            // These are null because they are not used in the files
            offset = parseVector(eElement.getAttribute("offset"));
            rotation = parseVector(eElement.getAttribute("rotation"));

            // register the textures
            if (!registerModels) {
                try {
                    MPSOBJLoader.INSTANCE.registerModelSprites(new ResourceLocation(file));
                } catch (Exception ignored) {
                }
            } else {
                IBakedModel bakedModel = ModelRegistry.getInstance().loadBakedModel(new ResourceLocation(file));
                if (bakedModel != null && bakedModel instanceof OBJModel.OBJBakedModel) {
                    ModelSpec modelspec = new ModelSpec(bakedModel, offset, rotation, file);
                    // ModelSpec modelspec = new ModelSpec(model, textures, offset, rotation, file);

                    ModelSpec existingspec = ModelRegistry.getInstance().put(MuseStringUtils.extractName(file), modelspec);
                    NodeList bindingNodeList = eElement.getElementsByTagName("binding");
                    for (int temp = 0; temp < bindingNodeList.getLength(); temp++) {
                        Node bindingnode = bindingNodeList.item(temp);
                        parseBinding(bindingnode, existingspec);
                    }
                } else {
                    MuseLogger.logError("Model file " + file + " not found! D:");
                }
            }
        }
    }

    public void parseBinding(Node bindingnode, ModelSpec modelspec) {
        if (bindingnode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) bindingnode;
            EntityEquipmentSlot slot = parseEquipmentSlot(eElement.getAttribute("slot"));
            MorphTarget target = parseTarget(eElement.getAttribute("target"));
            NodeList partNodeList = eElement.getElementsByTagName("part");
            for (int temp = 0; temp < partNodeList.getLength(); temp++) {
                Node partnode = partNodeList.item(temp);
                parseParts(partnode, modelspec, slot, target);
            }
        }
    }

    public void parseParts(Node partNode, ModelSpec modelspec, EntityEquipmentSlot slot, MorphTarget target) {
        if (partNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) partNode;
            Colour defaultcolor = parseColour(eElement.getAttribute("defaultcolor")); // not currently used
            Boolean defaultglow = parseBool(eElement.getAttribute("defaultglow"));
            String name = eElement.getAttribute("name");
            String polygroup = validatePolygroup(eElement.getAttribute("polygroup"), modelspec);

            if (polygroup != null) {
                ModelPartSpec partspec = new ModelPartSpec(modelspec, target, polygroup, slot, 0,
                        (defaultglow != null) ? defaultglow :false, name);
                modelspec.put(polygroup, partspec);
            }
        }
    }

    @Nullable
    public String validatePolygroup(String s, ModelSpec m) {
        return ((OBJModel.OBJBakedModel)m.getModel()).getModel().getMatLib().getGroups().keySet().contains(s) ? s : null;
    }

    @Nullable
    public Boolean parseBool(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public Colour parseColour(String s) {
        try {
            Color c = Color.decode(s);
            return new Colour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        } catch (Exception e){
            return null;
        }
    }

    /*
    * TODO: try something else.
    */
    EntityEquipmentSlot parseEquipmentSlot(String s) {
        switch (s.toUpperCase()) {
            case "HEAD": return EntityEquipmentSlot.HEAD;
            case "CHEST": return EntityEquipmentSlot.CHEST;
            case "LEGS": return EntityEquipmentSlot.LEGS;
            case "FEET": return EntityEquipmentSlot.FEET;
            // Left and right hand would have been better.
//            case "OFFHAND": return EntityEquipmentSlot.OFFHAND;
//            case "MAINHAND": return EntityEquipmentSlot.MAINHAND;
            default: return null;
        }
    }

    @Nullable
    public MorphTarget parseTarget(String s) {
        switch (s.toLowerCase()) {
            case "head": return Head;
            case "body": return Body;
            case "leftarm": return LeftArm;
            case "rightarm": return RightArm;
            case "leftleg": return LeftLeg;
            case "rightleg": return RightLeg;
//            case "cloak": return Cloak;
            default: return null;
        }
    }

    @Nullable
    public Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public Vec3d parseVector(String s) {
        try {
            String[] ss = s.split(",");
            double x = Double.parseDouble(ss[0]);
            double y = Double.parseDouble(ss[1]);
            double z = Double.parseDouble(ss[2]);
            return new Vec3d(x, y, z);
        } catch (Exception e) {
            return null;
        }
    }
}