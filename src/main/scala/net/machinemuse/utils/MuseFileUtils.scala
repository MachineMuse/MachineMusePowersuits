package net.machinemuse.utils

import java.io.{File, FileInputStream, FileOutputStream}

import net.machinemuse.numina.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:07 PM, 6/28/13
 */
object MuseFileUtils {
  def copyFile(oldFile: File, newFile: File) {
    if (!newFile.exists) {
      newFile.createNewFile()
    }
    new FileOutputStream(newFile).getChannel transferFrom(new FileInputStream(oldFile).getChannel, 0, Long.MaxValue)
    MuseLogger.logDebug("Successfully moved MPS config to new location. :D")
  }
}
