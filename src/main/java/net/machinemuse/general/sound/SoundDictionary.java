package net.machinemuse.general.sound;

import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class SoundDictionary {
    static {
        new SoundDictionary();
    }

    private static final String SOUND_PREFIX = "powersuits:";
    public static SoundEvent SOUND_EVENT_GLIDER = registerSound("Glider");
    public static SoundEvent SOUND_EVENT_GUI_INSTALL = registerSound("GUIInstall");
    public static SoundEvent SOUND_EVENT_GUI_SELECT = registerSound("MMMPSboop");
    public static SoundEvent SOUND_EVENT_JETBOOTS = registerSound("JetBoots");
    public static SoundEvent SOUND_EVENT_JETPACK = registerSound("Jetpack");
    public static SoundEvent SOUND_EVENT_JUMP_ASSIST = registerSound("JumpAssist");
    public static SoundEvent SOUND_EVENT_MPS_BOOP = registerSound("mps_boop");
    public static SoundEvent SOUND_EVENT_SWIM_ASSIST = registerSound("SwimAssist");
    public static SoundEvent SOUND_EVENT_ELECTROLYZER = registerSound("WaterElectrolyzer");

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