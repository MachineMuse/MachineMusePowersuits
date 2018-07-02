package net.machinemuse.powersuits.common.proxy;

import api.player.model.ModelPlayerAPI;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.client.event.EventRegisterRenderers;
import net.machinemuse.powersuits.client.model.obj.OBJPlusLoader;
import net.machinemuse.powersuits.client.render.item.SMovingArmorModel;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.event.ClientTickHandler;
import net.machinemuse.powersuits.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.machinemuse.powersuits.event.RenderEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
 *
 * Ported to Java by lehjr on 11/14/16.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModelLoaderRegistry.registerLoader(OBJPlusLoader.INSTANCE);
        OBJPlusLoader.INSTANCE.addDomain(MODID.toLowerCase());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        KeybindManager.readInKeybinds();
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new EventRegisterRenderers());
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();

        if (ModCompatibility.isRenderPlayerAPILoaded()) {
            ModelPlayerAPI.register(MODID, SMovingArmorModel.class);
        }
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }
}