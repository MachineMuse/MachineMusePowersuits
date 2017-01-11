package net.machinemuse.numina.basemod;

import net.machinemuse.numina.event.NuminaPlayerTracker;
import net.machinemuse.numina.recipe.JSONRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:06 AM, 6/18/13
 *
 * Ported to Java by lehjr on 11/15/16.
 */
@Mod(modid = "numina")
public class Numina {

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
        NuminaConfig.init(e);
        configDir = e.getModConfigurationDirectory();
        File recipesFolder = new File(configDir, "machinemuse/recipes");
        recipesFolder.mkdirs();
        recipesFolder.mkdir();
        //MinecraftForge.EVENT_BUS.register(PlayerTickHandler)
        //    MinecraftForge.EVENT_BUS.register(DeathEventHandler)
        //    NetworkRegistry.instance.registerGuiHandler(Numina, NuminaGuiHandler)
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

    @Mod.EventHandler private void serverstart(FMLServerStartedEvent e) {
        JSONRecipeList.loadRecipesFromDir(Numina.configDir.toString() + "/machinemuse/recipes/");
        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
    }
}