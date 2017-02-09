package net.machinemuse.numina.basemod;

import net.machinemuse.numina.event.FOVUpdateEventHandler;
import net.machinemuse.numina.event.KeybindKeyHandler;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void PreInit(FMLPreInitializationEvent event) {
        super.PreInit(event);
    }

    @Override
    public void Init(FMLInitializationEvent event) {
        super.Init(event);
        MuseLogger.logDebug("Client Proxy Started");
        MinecraftForge.EVENT_BUS.register((Object)new MouseEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new KeybindKeyHandler());
    }

    @Override
    public void PostInit(FMLPostInitializationEvent event) {
        super.PostInit(event);
    }
}