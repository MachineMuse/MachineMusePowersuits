package net.machinemuse.general.sound;

public class SoundDictionary {
    /**
     * Sounds
     */
    private static final String SOUND_RESOURCE_LOCATION = "powersuits:";
    private static final String SOUND_PREFIX = "powersuits:";

    public static String[] soundFiles = {
            SOUND_RESOURCE_LOCATION + "Glider.ogg",
            SOUND_RESOURCE_LOCATION + "GUIInstall.ogg",
            SOUND_RESOURCE_LOCATION + "MMMPSboop.wav",
            SOUND_RESOURCE_LOCATION + "JetBoots.ogg",
            SOUND_RESOURCE_LOCATION + "Jetpack.ogg",
            SOUND_RESOURCE_LOCATION + "JumpAssist.ogg",
            SOUND_RESOURCE_LOCATION + "SwimAssist.ogg",
            SOUND_RESOURCE_LOCATION + "WaterElectrolyzer.ogg"};

    public static final String SOUND_GUI_INSTALL = SOUND_PREFIX + "GUIInstall";
    public static final String SOUND_GUI_SELECT = SOUND_PREFIX + "MMMPSboop";
    public static final String SOUND_JUMP_ASSIST = SOUND_PREFIX + "JumpAssist";


    public static final String SOUND_GLIDER = SOUND_PREFIX + "Glider";
    public static final String SOUND_JETBOOTS = SOUND_PREFIX + "JetBoots";
    public static final String SOUND_JETPACK = SOUND_PREFIX + "Jetpack";
    public static final String SOUND_SWIMASSIST = SOUND_PREFIX + "SwimAssist";
    public static final String SOUND_ELECTROLYZER = SOUND_PREFIX + "WaterElectrolyzer";

}
