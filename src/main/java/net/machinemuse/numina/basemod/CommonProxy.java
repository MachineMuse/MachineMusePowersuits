package net.machinemuse.numina.basemod;

import net.machinemuse.numina.network.NuminaPackets;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class CommonProxy {
    public void PreInit(FMLPreInitializationEvent event) {
        
    }

    public void Init(FMLInitializationEvent event) {
        NuminaPackets.init();
    }

    public void PostInit(FMLPostInitializationEvent event) {

    }
}