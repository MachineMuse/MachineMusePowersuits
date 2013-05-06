package net.machinemuse.powersuits.client.render.modelspec


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:10 AM, 29/04/13
 */
object ModelSpecXMLWriter {
  def writeRegistry(file: String) {
    val xmlwrite =
      <models>
        {for ((modelname, modelspec) <- ModelRegistry.apply) yield
        <model file={modelspec.filename} textures={concatList(modelspec.textures)}>
          {for ((partname, partspec) <- modelspec.apply) yield
          <binding slot={partspec.slot.toString} target={partspec.morph.name}>
            <part defaultcolor={partspec.defaultcolourindex.toString} defaultglow={partspec.defaultglow.toString} polygroup={partspec.partName} name={partspec.displayName}/>
          </binding>}
        </model>}
      </models>

    scala.xml.XML.save(file, xmlwrite)
  }

  def concatList(list: Seq[String]): String = list mkString ","


}
