package net.machinemuse.numina.basemod;

import net.machinemuse.numina.event.FOVUpdateEventHandler;
import net.machinemuse.numina.event.KeybindKeyHandler;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class NuminaProxyClient implements NuminaProxy
{
    @Override
    public void PreInit() {
    }

    @Override
    public void PostInit() {
    }

    @Override
    public void Init() {
        MuseLogger.logDebug("Client Proxy Started");
        MinecraftForge.EVENT_BUS.register((Object)new MouseEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new KeybindKeyHandler());
    }

    public NuminaProxyClient() {
    }
}
