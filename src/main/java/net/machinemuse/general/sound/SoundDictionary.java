package net.machinemuse.general.sound;

import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
public class SoundDictionary {
    private static final String SOUND_PREFIX = "powersuits:";
    public static SoundEvent SOUND_EVENT_GLIDER = registerSound("glider");
    public static SoundEvent SOUND_EVENT_GUI_INSTALL = registerSound("guiinstall");
    public static SoundEvent SOUND_EVENT_GUI_SELECT = registerSound("mmmpsboop");
    public static SoundEvent SOUND_EVENT_JETBOOTS = registerSound("jetboots");
    public static SoundEvent SOUND_EVENT_JETPACK = registerSound("jetpack");
    public static SoundEvent SOUND_EVENT_JUMP_ASSIST = registerSound("jumpassist");
    public static SoundEvent SOUND_EVENT_SWIM_ASSIST = registerSound("swimassist");
    public static SoundEvent SOUND_EVENT_ELECTROLYZER = registerSound("waterelectrolyzer");


    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(ModularPowersuits.MODID, soundName);
        SoundEvent event = new SoundEvent(location);
        GameRegistry.register(event, location);
        return event;
    }
}
