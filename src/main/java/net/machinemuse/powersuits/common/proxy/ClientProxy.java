package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.numina.client.model.obj.MuseOBJLoader;
import net.machinemuse.powersuits.client.event.ClientTickHandler;
import net.machinemuse.powersuits.client.event.EventRegisterRenderers;
import net.machinemuse.powersuits.client.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.config.CosmeticPresetSaveLoad;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.client.event.RenderEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

/**
 * Client side of the proxy.
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // FIXME: delete? or leave it. Leaving it is harmless.
        CosmeticPresetSaveLoad.copyPresetsFromJar();
        MuseOBJLoader.INSTANCE.addDomain(MODID.toLowerCase());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
//        KeybindManager.readInKeybinds();
        MPSConfig.INSTANCE.configDoubleKVGen();
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new EventRegisterRenderers());
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }
}