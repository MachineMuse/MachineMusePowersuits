package net.machinemuse.powersuits.client.render.modelspec

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:10 AM, 29/04/13
 */
object ModelSpecXMLWriter {
  def writeRegistry {
    for ((modelname, modelspec) <- ModelRegistry.apply) yield
      <model file={modelname} textures={concatList(modelspec.textures)}>
        {for ((partname, partspec) <- modelspec.apply) yield
        <binding slot={partspec.slot.toString} target={partspec.morph.name}>
          <part defaultcolor={partspec.defaultcolour.hexColour} defaultglow={partspec.defaultglow.toString} polygroup={partspec.partName} name={partspec.displayName}/>
        </binding>}
      </model>

  }

  def concatList(list: Seq[String]): String = {
    list.flatten mkString ","
  }


}
