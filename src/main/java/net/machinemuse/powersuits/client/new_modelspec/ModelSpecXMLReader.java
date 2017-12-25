package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.common.MPSConstants;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;


/**
 *  This is called twice, once during the TextureStitchEvent to load and register the textures, and once to load and register
 * the models. Textures need to be registered before the models are even loaded or else the models will be baked with
 * missing textures. This is because IModels are stored in a cache on first load.
 *
 */

@SideOnly(Side.CLIENT)
public class ModelSpecXMLReader {
    private static ModelSpecXMLReader INSTANCE;

    public static ModelSpecXMLReader getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelSpecXMLReader.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelSpecXMLReader();
                }
            }
        }
        return INSTANCE;
    }

    private ModelSpecXMLReader() {
    }


    public static void parseFile(File file, @Nullable TextureStitchEvent event) {
        String filePath = file.getAbsoluteFile().getAbsolutePath();
        if (file.exists()) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document xml = dBuilder.parse(file);
                xml.normalizeDocument();

                if (xml.hasChildNodes()) {
                    NodeList specList = xml.getElementsByTagName("modelSpec");
                    for(int i = 0; i< specList.getLength(); i++) {
                        Node specNode = specList.item(i);
                        if(specNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) specNode;
                            EnumSpecType specType = EnumSpecType.getTypeFromName(eElement.getAttribute("type"));
                            String specName = eElement.getAttribute("name");

                            IModelState modelState = null;
                            NodeList cameraTransformList = eElement.getElementsByTagName("itemCameraTransforms");
                            if (cameraTransformList.getLength() > 0) {
                                Node cameraTransformNode = cameraTransformList.item(0);
                                modelState = getIModelState(cameraTransformNode);
                            } else {
                                // Get the transform for the model and add to the registry
                                NodeList transformNodeList = eElement.getElementsByTagName("trsrTransformation");
                                if (transformNodeList.getLength() > 0)
                                    modelState = getTransform(transformNodeList.item(0));
                            }



                            boolean isDefault = (eElement.hasAttribute("default") ? Boolean.parseBoolean(eElement.getAttribute("default")) : false);

                            switch(specType) {
                                case POWER_FIST:
                                    parseModelSpec(specNode, event, new ModelSpec(specName, isDefault, EnumSpecType.POWER_FIST), modelState);
                                    break;

                                case ARMOR_MODEL:
                                    parseModelSpec(specNode, event, new ModelSpec(specName, isDefault, EnumSpecType.ARMOR_MODEL), modelState);
                                    break;

                                case ARMOR_SKIN:
                                    TextureSpec textureSpec = new TextureSpec(specName, isDefault);
                                    parseTextureSpec(specNode, event, textureSpec);
                                    break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                MuseLogger.logException(new StringBuilder("Failed to load ModelSpecFile: ").append(filePath).append(": ").toString(), e);
            }
        }
    }


    /**
     * Biggest difference between the ModelSpec for Armor vs PowerFist is that the armor models don't need item camera transforms
     */
    public static void parseModelSpec(Node specNode, TextureStitchEvent event, ModelSpec modelSpec, IModelState modelState) {
        NodeList models = specNode.getOwnerDocument().getElementsByTagName("model");
        List<String> textures = new ArrayList<>();
        for (int i=0; i < models.getLength(); i++) {

            Node modelNode = models.item(i);
            if (modelNode.getNodeType() == Node.ELEMENT_NODE) {
                Element modelElement = (Element)modelNode;

                // Register textures
                if (event != null) {
                    List<String> tempTextures = Arrays.asList(modelElement.getAttribute("textures").split(","));
                    for (String texture: tempTextures)
                        if (!(textures.contains(texture)))
                            textures.add(texture);
                } else {
                    String modelLocation = modelElement.getAttribute("file");
//                    if (modelLocation.endsWith(".obj"))
//                        modelLocation = modelLocation.substring(0,modelLocation.length() - ".obj".length());

                    BakedModelRegistry.getInstance().putModelLocation(modelLocation);

                    if(modelState == null) {
                        // check for item camera transforms, then fall back on single transform for model
                        NodeList cameraTransformList = ((Element) modelNode).getElementsByTagName("itemCameraTransforms");
                        if (cameraTransformList.getLength() > 0) {
                            Node cameraTransformNode = cameraTransformList.item(0);
                            modelState = getIModelState(cameraTransformNode);
                        } else {
                            // Get the transform for the model and add to the registry
                            NodeList transformNodeList = ((Element) modelNode).getElementsByTagName("trsrTransformation");
                            if (transformNodeList.getLength() > 0)
                                modelState = getTransform(transformNodeList.item(0));
                            else
                                modelState = TRSRTransformation.identity();
                        }
                    }
                    BakedModelRegistry.getInstance().putIModelState(modelLocation, modelState);

                    // ModelSpec stuff
                    NodeList bindingNodeList = ((Element) modelNode).getElementsByTagName("binding");
                    if (bindingNodeList.getLength() > 0) {
                        for (int k = 0; k < bindingNodeList.getLength(); k++) {
                            Node bindingNode = bindingNodeList.item(k);
                            Binding binding = getBinding(bindingNode);
                            NodeList partNodeList = ((Element) bindingNode).getElementsByTagName("part");

                            for(int j=0; j<partNodeList.getLength(); j++) {
                                Map<ModelPartSpec, Boolean> partSpecMap =
                                        getModelPartSpec(partNodeList.item(j), binding, modelLocation.hashCode());
                                modelSpec.add(partSpecMap);
                            }
                        }
                    }
                }
            }
        }

        // Register textures
        if (event != null)
            for (String texture : textures)
                event.getMap().registerSprite(new ResourceLocation(texture));
        else
            ModelSpecRegistry.getInstance().putSpec(modelSpec.getName(), modelSpec);
    }

    /**
     * This gets the map of TransformType, TRSRTransformation> used for handheld items
     * @param itemCameraTransformsNode
     * @return
     */
    public static IModelState getIModelState(Node itemCameraTransformsNode) {
        ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
        NodeList transformationList = ((Element)itemCameraTransformsNode).getElementsByTagName("trsrTransformation");
        for (int i = 0; i < transformationList.getLength(); i++) {
            Node transformationNode = transformationList.item(i);
            ItemCameraTransforms.TransformType transformType =
                    ItemCameraTransforms.TransformType.valueOf(((Element) transformationNode).getAttribute("type").toUpperCase());
            TRSRTransformation trsrTransformation = getTransform(transformationNode);
            builder.put(transformType, trsrTransformation);
        }
        return new SimpleModelState(builder.build());
    }


    /**
     * This gets the transforms for baking the models. TRSRTransformation is also used for item camera transforms to alter the
     * position, scale, and translation of a held/dropped/framed item
     *
     * @param transformationNode
     * @return
     */
    public static TRSRTransformation getTransform(Node transformationNode) {
        Vector3f translation = parseVector(((Element) transformationNode).getAttribute("translation"));
        Vector3f rotation = parseVector(((Element) transformationNode).getAttribute("rotation"));
        Vector3f scale = parseVector(((Element) transformationNode).getAttribute("scale"));
        return getTransform(translation, rotation, scale);
    }

    public static void parseTextureSpec(Node specNode, TextureStitchEvent event, TextureSpec textureSpec) {
        NodeList textures = specNode.getOwnerDocument().getElementsByTagName("texture");
        for (int i = 0; i < textures.getLength(); i++) {
            Node textureNode = textures.item(i);
            if (textureNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element)textureNode;
                ResourceLocation fileLocation = new ResourceLocation(eElement.getAttribute("file"));
                if (event == null) {
                    NodeList bindings = eElement.getElementsByTagName("binding");
                    for (int j = 0; j < bindings.getLength(); j++) {
                        textureSpec.add(getBinding(bindings.item(j)), fileLocation);
                    }
                } else
                    event.getMap().registerSprite(fileLocation);
            }
        }
        if (event == null) {
            ModelSpecRegistry.getInstance().putSpec(textureSpec.getName(), textureSpec);
        }
    }

    /**
     * ModelPartSpec is a group of settings for each model part
     */
    public static Map<ModelPartSpec, Boolean>  getModelPartSpec(Node partSpecNode, Binding binding, int modelLocationHash) {
        Element partSpecElement = (Element) partSpecNode;
        String partname = partSpecElement.getAttribute("polygroup");
        ModelSpecRegistry.getInstance().setBinding(new StringBuilder("").append(modelLocationHash).append(".").append(partname).toString(), binding);
        boolean visible = Boolean.parseBoolean(partSpecElement.getAttribute("defaultVisible"));
        boolean glow = Boolean.parseBoolean(partSpecElement.getAttribute("defaultglow"));
        Colour colour = parseColour(partSpecElement.getAttribute("defaultcolor"));
        int enumColourIndex = colour!=null ? EnumColour.findClosestEnumColour(colour).getIndex() : MPSConstants.ENUM_COLOUR_WHITE_INDEX;

        return Collections.singletonMap(new ModelPartSpec(modelLocationHash, partname, enumColourIndex, glow), visible);
    }

    /**
     * Binding is a subset if settings for the ModelPartSpec
     */
    public static Binding getBinding(Node bindingNode) {
        return new Binding(
                (((Element) bindingNode).hasAttribute("target")) ?
                        MorphTarget.getMorph(((Element) bindingNode).getAttribute("target")) : null,
                (((Element) bindingNode).hasAttribute("slot")) ?
                        EntityEquipmentSlot.fromString(((Element) bindingNode).getAttribute("slot").toLowerCase()) : null,
                (((Element) bindingNode).hasAttribute("itemState")) ?
                        ((Element) bindingNode).getAttribute("itemState"): "all"
        );
    }

    /**
     * Simple transformation for armor models. Powerfist (and shield?) will need one of these for every conceivable case except GUI which will be an icon
     */
    public static TRSRTransformation getTransform(@Nullable Vector3f translation, @Nullable Vector3f rotation, @Nullable Vector3f scale) {
        if (translation == null)
            translation = new Vector3f(0, 0, 0);
        if (rotation == null)
            rotation = new Vector3f(0, 0, 0);
        if (scale == null)
            scale = new Vector3f(1, 1, 1);

        return new TRSRTransformation(
                // Transform
                new Vector3f(translation.x / 16, translation.y / 16, translation.z / 16),
                // Angles
                TRSRTransformation.quatFromXYZDegrees(rotation),
                // Scale
                scale,
                null);
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
    public static Vector3f parseVector(String s) {
        try {
            String[] ss = s.split(",");
            float x = Float.parseFloat(ss[0]);
            float y = Float.parseFloat(ss[1]);
            float z = Float.parseFloat(ss[2]);
            return new Vector3f(x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static Colour parseColour(String s) {
        try {
            int value = Integer.parseInt(s, 16);
            Color c = new Color(value);
            return new Colour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        } catch (Exception e){
            return null;
        }
    }
}
