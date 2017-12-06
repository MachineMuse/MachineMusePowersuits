package net.machinemuse.powersuits.client.new_modelspec;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;



/**
 *  This is called twice, once during the TextureStitchEvent to load and register the textures, and once to load and register
 * the models. Textures need to be registered before the models are even loaded or else the models will be baked with
 * missing textures. This is because IModels are stored in a cache on first load.
 *
 */
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
            System.out.println("URLFile exists!!");
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
                            boolean isDefault = (eElement.hasAttribute("default") ? Boolean.parseBoolean(eElement.getAttribute("default")) : false);

                            switch(specType) {
                                case ARMOR_MODEL:
                                    //TODO: new ModelSpec and ModelSpecParser
                                    break;

                                case POWER_FIST:
                                    //TODO: new ModelSpec and ModelSpecParser
                                    break;


                                case ARMOR_SKIN:
                                    TextureSpec textureSpec = new TextureSpec(specName, isDefault);
                                    parseTextureSpec(specNode, event, textureSpec);
                                    break;
                            }



                            System.out.println("specNode Name: " + eElement.getAttribute("name"));
                            System.out.println("isDefault: " + (eElement.hasAttribute("default") ? eElement.getAttribute("default") : false));
                            System.out.println("specType: " + eElement.getAttribute("type"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MuseLogger.logException(new StringBuilder("Failed to load ModelSpecFile: ").append(filePath).append(": ").toString(), e);
            }
        } else {
            System.out.println("URLFile does not exist");
        }
    }



    public static void parseArmorModelSpec(Node specNode, TextureStitchEvent event, )

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
                        Element binding = (Element) bindings.item(j);
                        EntityEquipmentSlot slot = EntityEquipmentSlot.fromString(binding.getAttribute("slot").toLowerCase());
                        String itemState = binding.getAttribute("itemState");
                        textureSpec.add(slot, fileLocation);
                    }
                } else
                    event.getMap().registerSprite(fileLocation);
            }
        }

        // TODO: register TextureSpec to a map/list

    }





















}
