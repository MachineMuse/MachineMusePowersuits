package net.machinemuse.utils;

import net.machinemuse.numina.utils.MuseLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:07 PM, 6/28/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class MuseFileUtils {
    public void copyFile(File oldFile, File newFile) throws IOException {
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
        new FileOutputStream(newFile).getChannel().transferFrom(new FileInputStream(oldFile).getChannel(), 0, Long.MAX_VALUE);
        MuseLogger.logDebug("Successfully moved MPS config to new location. :D");
    }
}


