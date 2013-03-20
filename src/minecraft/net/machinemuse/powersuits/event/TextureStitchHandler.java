package net.machinemuse.powersuits.event;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class TextureStitchHandler {

	@ForgeSubscribe
	public void onTextureStitch(TextureStitchEvent.Post event) {
		// MuseIcon.registerAllIcons(event.map);
	}
}
