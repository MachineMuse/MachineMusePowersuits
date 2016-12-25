package net.machinemuse.numina.general;

import net.machinemuse.numina.basemod.NuminaConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger access class. May become more fleshed out in the future.
 *
 * @author MachineMuse
 *
 *
 * Ported to Java by lehjr on 10/23/16.
 */
public final class MuseLogger {
    private static final Logger logger = LogManager.getLogger("MachineMuse");

    public static void logDebug(String string) {
        boolean debugging = true;
        try {
            if (!NuminaConfig.isDebugging()) debugging = false;
        } catch (Exception e){
        }
        if (debugging) logger.info(string);
    }

    public static void logError(String string) {
        logger.warn(string);
    }

    public static void logInfo(String string) {
        logger.info(string);
    }

    public static void logException(String string, Throwable exception) {
        logger.warn(string);
        exception.printStackTrace();
    }
}