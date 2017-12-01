package net.machinemuse.powersuits.common;


import net.machinemuse.powersuits.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

import static net.machinemuse.powersuits.common.MPSConstants.*;

@Mod(modid = MODID, name = NAME, version = VERSION)//, acceptedMinecraftVersions = "[1.12.2]")
public class ModularPowersuits {
    @Nonnull
    private static ModularPowersuits INSTANCE;

    @Nonnull
    @Mod.InstanceFactory
    public static ModularPowersuits getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModularPowersuits();
        return INSTANCE;
    }

    protected ModularPowersuits() {}

    @SidedProxy(clientSide = "net.machinemuse.powersuits.proxy.ClientProxy", serverSide = "net.machinemuse.powersuits.proxy.ServerProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        proxy.registerEvents();
//        proxy.registerTileEntities();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }



}
