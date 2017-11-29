package net.machinemuse.numina.common;

import net.machinemuse.numina.common.recipe.JSONRecipeList;
import net.machinemuse.numina.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.io.File;

import static net.machinemuse.numina.common.NuminaConstants.*;


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:06 AM, 6/18/13
 *
 * Ported to Java by lehjr on 11/15/16.
 */
@Mod(modid = MODID, name = NAME, version = VERSION)
public class Numina {
    @SidedProxy(clientSide = "net.machinemuse.numina.proxy.ClientProxy", serverSide = "net.machinemuse.numina.proxy.ServerProxy")
    static CommonProxy proxy;
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
    private void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

    }

    @Mod.EventHandler
    private void init(FMLInitializationEvent event) {
        proxy.init(event);

    }

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        JSONRecipeList.loadRecipesFromDir(Numina.getInstance().configDir.toString() + "/machinemuse_old/recipes/");
    }

    @Mod.EventHandler
    private void serverstart(FMLServerStartedEvent event) {

    }
}