package net.machinemuse.powersuits.client.render.modelspec

import java.io.{File, FileOutputStream, PrintWriter}

import com.google.gson.Gson

/**
  * Author: MachineMuse (Claire Semple)
  * Created: 9:10 AM, 29/04/13
  */
object ModelSpecJSONWriter {
  def writeRegistry(file: String) {
    val gson = new Gson()
    import scala.collection.JavaConverters._
    val registry = ModelRegistry.apply.asJava
    val jsonwrite = gson.toJson(registry)
    val w = new PrintWriter(new FileOutputStream(new File(file)))
    w.print(jsonwrite)
    w.close()
  }
}
