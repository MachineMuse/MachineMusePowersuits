package net.machinemuse.powersuits.client.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class SoundDictionary {
    private static final String SOUND_PREFIX = "powersuits:";
    public static SoundEvent SOUND_EVENT_GLIDER = registerSound("glider");
    public static SoundEvent SOUND_EVENT_GUI_INSTALL = registerSound("gui_install");
    public static SoundEvent SOUND_EVENT_GUI_SELECT = registerSound("gui_select");
    public static SoundEvent SOUND_EVENT_JETBOOTS = registerSound("jet_boots");
    public static SoundEvent SOUND_EVENT_JETPACK = registerSound("jetpack");
    public static SoundEvent SOUND_EVENT_JUMP_ASSIST = registerSound("jump_assist");
    public static SoundEvent SOUND_EVENT_MPS_BOOP = registerSound("mmmps_boop");
    public static SoundEvent SOUND_EVENT_SWIM_ASSIST = registerSound("swim_assist");
    public static SoundEvent SOUND_EVENT_ELECTROLYZER = registerSound("water_electrolyzer");

    static {
        new SoundDictionary();
    }

    @SubscribeEvent
    public static void registerSoundEvent(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                SOUND_EVENT_GLIDER,
                SOUND_EVENT_GUI_INSTALL,
                SOUND_EVENT_GUI_SELECT,
                SOUND_EVENT_JETBOOTS,
                SOUND_EVENT_JETPACK,
                SOUND_EVENT_JUMP_ASSIST,
                SOUND_EVENT_MPS_BOOP,
                SOUND_EVENT_SWIM_ASSIST,
                SOUND_EVENT_ELECTROLYZER);
    }

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(MODID, soundName);
        SoundEvent event = new SoundEvent(location).setRegistryName(location);
        return event;
    }
}
