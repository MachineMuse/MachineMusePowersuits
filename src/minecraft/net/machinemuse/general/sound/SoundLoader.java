package net.machinemuse.general.sound;

import net.machinemuse.general.MuseLogger;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundLoader {
	/**
	 * Sounds
	 */
	private static final String SOUND_RESOURCE_LOCATION = "mods/mmmPowersuits/sound/";
	private static final String SOUND_PREFIX = "mods.mmmPowersuits.sound.";

	public static String[] soundFiles = { SOUND_RESOURCE_LOCATION + "Glider.ogg", SOUND_RESOURCE_LOCATION + "GUIInstall.ogg",
			SOUND_RESOURCE_LOCATION + "GUISelect.ogg", SOUND_RESOURCE_LOCATION + "JetBoots.ogg", SOUND_RESOURCE_LOCATION + "Jetpack.ogg",
			SOUND_RESOURCE_LOCATION + "JumpAssist.ogg", SOUND_RESOURCE_LOCATION + "SwimAssist.ogg",
			SOUND_RESOURCE_LOCATION + "WaterElectrolyzer.ogg" };
	public static final String SOUND_GUI_INSTALL = SOUND_PREFIX + "GUIInstall";
	public static final String SOUND_GUI_SELECT = SOUND_PREFIX + "GUISelect";
	public static final String SOUND_JUMP_ASSIST = SOUND_PREFIX + "JumpAssist";


    public static final String SOUND_GLIDER = SOUND_PREFIX + "Glider";
    public static final float GLIDER_PRIORITY = 5;
    public static final String SOUND_JETBOOTS = SOUND_PREFIX + "JetBoots";
    public static final float JETBOOTS_PRIORITY = 4;
    public static final String SOUND_JETPACK = SOUND_PREFIX + "Jetpack";
    public static final float JETPACK_PRIORITY = 3;
	public static final String SOUND_SWIMASSIST = SOUND_PREFIX + "SwimAssist";
    public static final float SWIMASSIST_PRIORITY = 2;
	public static final String SOUND_ELECTROLYZER = SOUND_PREFIX + "WaterElectrolyzer";
    public static final float ELECTROLYZER_PRIORITY = 1;

	@ForgeSubscribe
	public void onSoundLoad(SoundLoadEvent event) {
		for (String soundFile : soundFiles) {
			try {
				event.manager.soundPoolSounds.addSound(soundFile, ModularPowersuits.class.getResource('/' + soundFile));
			} catch (Exception e) {
				MuseLogger.logError("Failed to register sound:" + soundFile);
			}
		}
	}
}
