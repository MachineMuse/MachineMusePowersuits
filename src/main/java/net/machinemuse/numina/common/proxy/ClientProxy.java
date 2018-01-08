package net.machinemuse.numina.common.proxy;

import net.machinemuse.numina.event.FOVUpdateEventHandler;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

//import net.machinemuse.numina.event.KeybindKeyHandler;

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
    }
}