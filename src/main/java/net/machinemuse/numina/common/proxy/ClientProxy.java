package net.machinemuse.numina.common.proxy;

import net.machinemuse.numina.client.render.RenderGameOverlayEventHandler;
import net.machinemuse.numina.event.FOVUpdateEventHandler;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 10/26/16.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new MouseEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
    }
}