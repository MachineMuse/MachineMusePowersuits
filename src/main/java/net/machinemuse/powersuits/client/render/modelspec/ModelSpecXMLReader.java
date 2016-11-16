package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
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
public class ModelSpecXMLReader {
    private ModelSpecXMLReader() {
    }

    private static ModelSpecXMLReader INSTANCE;

    public static ModelSpecXMLReader getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ModelSpecXMLReader();
        return INSTANCE;
    }

    public void parseFile(URL file) {
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
        String[] textures;
        Vec3 offset;
        Vec3 rotation;

        if (modelnode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) modelnode;
            file = eElement.getAttribute("file");
            textures = eElement.getAttribute("textures").split(",");

            // These are null because they are not used in the files
            offset = parseVector(eElement.getAttribute("offset"));
            rotation = parseVector(eElement.getAttribute("rotation"));

            WavefrontObject model = ModelRegistry.getInstance().loadModel(new ResourceLocation(file));
            if (model != null) {
                ModelSpec modelspec = new ModelSpec(model, textures, offset, rotation, file);
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

    public void parseBinding(Node bindingnode, ModelSpec modelspec) {
        if (bindingnode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) bindingnode;
            int slot = parseInt(eElement.getAttribute("slot"));
            MorphTarget target = parseTarget(eElement.getAttribute("target"));
            NodeList partNodeList = eElement.getElementsByTagName("part");
            for (int temp = 0; temp < partNodeList.getLength(); temp++) {
                Node partnode = partNodeList.item(temp);
                parseParts(partnode, modelspec, slot, target);
            }
        }
    }

    public void parseParts(Node partNode, ModelSpec modelspec, int slot, MorphTarget target) {
        if (partNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) partNode;
//            Colour defaultcolor = parseColour(eElement.getAttribute("defaultcolor"));
            Boolean defaultglow = parseBool(eElement.getAttribute("defaultglow"));
            String name = eElement.getAttribute("name");

            String polygroup = validatePolygroup(eElement.getAttribute("polygroup"), modelspec);

            if (polygroup != null) {
                ModelPartSpec partspec = new ModelPartSpec(modelspec, target, polygroup, slot, 0, (defaultglow != null) ? defaultglow :false, name);
                modelspec.put(polygroup, partspec);
            }
        }
    }

    @Nullable
    public String validatePolygroup(String s, ModelSpec m) {
        for (GroupObject groupObject : m.model.groupObjects) {
            if (groupObject.name.equals(s)) return s;
        }
        return null;
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

    @Nullable
    public MorphTarget parseTarget(String s) {
        switch (s.toLowerCase()) {
            case "head": return Head;
            case "body": return Body;
            case "leftarm": return LeftArm;
            case "rightarm": return RightArm;
            case "leftleg": return LeftLeg;
            case "rightleg": return RightLeg;
            case "cloak": return Cloak;
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
    public Vec3 parseVector(String s) {
        try {
            String[] ss = s.split(",");
            double x = Double.parseDouble(ss[0]);
            double y = Double.parseDouble(ss[1]);
            double z = Double.parseDouble(ss[2]);
            return Vec3.createVectorHelper(x, y, z);
        } catch (Exception e) {
            return null;
        }
    }
}