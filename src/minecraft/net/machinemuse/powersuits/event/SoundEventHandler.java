package net.machinemuse.powersuits.event;

import net.machinemuse.api.ModularCommon;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundEventHandler {
	    @ForgeSubscribe
	    public void onSound(SoundLoadEvent event) {
	    	for (String soundFile : ModularCommon.soundFiles) {
		        try {
		            event.manager.soundPoolSounds.addSound(soundFile, this.getClass().getResource("/" + soundFile));
		        }
		        catch (Exception e) {
		            System.err.println("[ModularPowersuits] Failed to register one or more sounds.");
		        }
	    	}
	    }
}

