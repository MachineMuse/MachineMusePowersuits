package net.machinemuse.powersuits.common.proxy;

/**
 * Common side of the proxy. Provides functions which
 * the ClientProxy and ServerProxy will override if the behaviour is different for client and
 * server, and keeps some common behaviour.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public interface CommonProxy {
    void registerEvents();

    void registerRenderers();

    void registerHandlers();

    void postInit();

    void sendModeChange(int dMode, String newMode);
}