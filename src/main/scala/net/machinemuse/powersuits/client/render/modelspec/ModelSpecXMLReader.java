package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.geometry.Colour;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.GroupObject;
import scala.xml.NodeSeq;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Iterator;

import static net.machinemuse.powersuits.client.render.modelspec.MorphTarget.*;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class ModelSpecXMLReader {





    //package net.machinemuse.powersuits.client.render.modelspec
//
//import java.awt.Color
//import java.net.URL
//
//import net.machinemuse.numina.general.MuseLogger
//import net.machinemuse.numina.geometry.Colour
//import net.machinemuse.utils.MuseStringUtils
//import net.minecraft.util.{ResourceLocation, Vec3}
//
//import scala.xml.{NodeSeq, XML}
//
//

    //object ModelSpecXMLReader {
//  def parseFile(file: URL) = {
//    val xml = XML.load(file)
//    (xml \\ "model") foreach {
//      modelnode => parseModel(modelnode)
//    }
//  }
//
//  def parseModel(modelnode: NodeSeq) = {
//    val file = (modelnode \ "@file").text
//    val textures = (modelnode \ "@textures").text.split(",")
//    val offset = parseVector((modelnode \ "@offset").text)
//    val rotation = parseVector((modelnode \ "@rotation").text)
//
//    ModelRegistry.loadModel(new ResourceLocation(file)) match {
//      case Some(m) => {
//        val modelspec = new ModelSpec(m, textures, offset, rotation, file)
//        val existingspec = ModelRegistry.put(MuseStringUtils.extractName(file), modelspec)
//        (modelnode \ "binding").foreach {
//          bindingnode => parseBinding(bindingnode, existingspec)
//        }
//      }
//      case None => MuseLogger logError "Model file " + file + " not found! D:"
//    }
//
//  }
//
  def parseBinding(bindingnode: NodeSeq, modelspec: ModelSpec) = {
    val slot = parseInt((bindingnode \ "@slot").text)
    val target = parseTarget((bindingnode \ "@target").text)
    slot.foreach(slot => {
      target.foreach(target =>
        (bindingnode \ "part").foreach {
          partnode =>
            parseParts(partnode, modelspec, slot, target)
        })
    })
  }

  public void parseParts(NodeSeq partNode, ModelSpec modelspec, int slot, MorphTarget target) {
    val defaultcolor = parseColour((partNode \ "@defaultcolor").text)
    val defaultglow = parseBool((partNode \ "@defaultglow").text)
    val name = (partNode \ "@name").text
    val polygroup = validatePolygroup((partNode \ "@polygroup").text, modelspec)
    polygroup.map(polygroup => {
      val partspec = new ModelPartSpec(modelspec, target, polygroup, slot, 0, defaultglow.getOrElse(false), name)
      modelspec.put(polygroup, partspec)
    })
  }

    @Nullable
    public String validatePolygroup(String s, ModelSpec m) {
        Iterator<GroupObject> it = m.model.groupObjects.iterator();
        while (it.hasNext()) {
            if (it.next().name.equals(s)) return s;
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
            return new Colour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha())
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