package net.machinemuse.numina.proxy;

import net.machinemuse.numina.client.events.FOVUpdateEventHandler;
import net.machinemuse.numina.client.events.KeybindKeyHandler;
import net.machinemuse.numina.client.render.RenderGameOverlayEventHandler;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new MouseEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
    }
}