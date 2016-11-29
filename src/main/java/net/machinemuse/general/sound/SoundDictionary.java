package net.machinemuse.general.sound;

import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
public class SoundDictionary {
    private static final String SOUND_PREFIX = "powersuits:";
    public static SoundEvent SOUND_EVENT_GLIDER = registerSound("Glider");
    public static SoundEvent SOUND_EVENT_GUI_INSTALL = registerSound("GUIInstall");
    public static SoundEvent SOUND_EVENT_GUI_SELECT = registerSound("MMMPSboop");
    public static SoundEvent SOUND_EVENT_JETBOOTS = registerSound("JetBoots");
    public static SoundEvent SOUND_EVENT_JETPACK = registerSound("Jetpack");
    public static SoundEvent SOUND_EVENT_JUMP_ASSIST = registerSound("JumpAssist");
    public static SoundEvent SOUND_EVENT_SWIM_ASSIST = registerSound("SwimAssist");
    public static SoundEvent SOUND_EVENT_ELECTROLYZER = registerSound("WaterElectrolyzer");


    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(ModularPowersuits.MODID, soundName);
        SoundEvent event = new SoundEvent(location);
        GameRegistry.register(event, location);
        return event;
    }
}
