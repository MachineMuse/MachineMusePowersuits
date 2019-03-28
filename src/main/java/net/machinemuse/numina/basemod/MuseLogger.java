package net.machinemuse.numina.basemod;

import net.machinemuse.numina.config.NuminaConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger access class. May become more fleshed out in the future.
 *
 * @author MachineMuse
 * <p>
 * <p>
 * Ported to Java by lehjr on 10/23/16.
 */
public final class MuseLogger {
    public static final Logger logger = LogManager.getLogger("MachineMuse");

    public static void logDebug(String string) {
        boolean debugging = true;
        try {
            if (!NuminaConfig.isDebugging())
                debugging = false;
        } catch (Exception ignored) {
        }
        if (debugging) logger.info(string);
    }

    public static void logError(String string) {
        logger.warn(string);
    }

    public static void logException(String string, Throwable exception) {
        logger.warn(string);
        exception.printStackTrace();
    }
}