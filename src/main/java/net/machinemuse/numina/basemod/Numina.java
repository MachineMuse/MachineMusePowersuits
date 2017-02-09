package net.machinemuse.numina.basemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.io.File;

import static net.machinemuse.numina.basemod.Numina.MODID;
import static net.machinemuse.numina.basemod.Numina.VERSION;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:06 AM, 6/18/13
 *
 * Ported to Java by lehjr on 11/15/16.
 */
@Mod(modid = MODID, version = VERSION)
public class Numina {
    public static final String MODID = "numina";
    public static final String VERSION = "@numina_version@";

    @SidedProxy(clientSide = "net.machinemuse.numina.basemod.ClientProxy", serverSide = "net.machinemuse.numina.basemod.ServerProxy")
    static CommonProxy proxy = null;
    public static File configDir = null;

    @Nonnull
    private static Numina INSTANCE;

    @Nonnull
    @Mod.InstanceFactory
    public static Numina getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Numina();
        return INSTANCE;
    }

    @Mod.EventHandler
    private void preinit(FMLPreInitializationEvent e) {
        proxy.PreInit(e);
    }

    @Mod.EventHandler
    private void init(FMLInitializationEvent e) {
        proxy.Init(e);
    }

    @Mod.EventHandler
    private void postinit(FMLPostInitializationEvent e) {
        proxy.PostInit(e);
    }

    /* this runs when a client loads a world in single player, or when a dedicated server loads
    *  FIXME!!! this is currently broken. For some reason the recipe loading is happening on world exit, not world load
    *
    *
    * */
    @Mod.EventHandler
    private void serverstart(FMLServerStartedEvent e) {
        System.out.println("running FMLServerStartedEvent");

//        JSONRecipeList.loadRecipesFromDir(Numina.configDir.toString() + "/machinemuse/recipes/");
//        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
    }
}