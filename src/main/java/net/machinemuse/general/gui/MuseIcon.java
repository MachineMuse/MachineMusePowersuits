package net.machinemuse.general.gui;


import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

/*
 * Icons without an item registered need to be added this way
 * Do not register the same icon twice as this produces weird side effects
 * @author Lehjr
 */
public class MuseIcon extends TextureAtlasSprite {
    protected MuseIcon(String spriteName) {
        super(spriteName);
    }

    // Armor
    public static TextureAtlasSprite apiaristArmor= new MuseIcon("apiaristArmor");
    public static TextureAtlasSprite basicPlating = new MuseIcon("basicPlating");
    public static TextureAtlasSprite diamondPlating = new MuseIcon("diamondPlating");
    public static TextureAtlasSprite energyShield = new MuseIcon("energyShield");
    public static TextureAtlasSprite hazmat = new MuseIcon("hazmat");
    public static TextureAtlasSprite heatSink = new MuseIcon("heatSink");
    public static TextureAtlasSprite mechAssistance = new MuseIcon("mechAssistance");
    public static TextureAtlasSprite nitrogenCoolingSystem = new MuseIcon("nitrogenCoolingSystem");
    /* Water tank module uses vanilla bucket of water icon */

    // Energy
    public static TextureAtlasSprite advancedBattery = new MuseIcon("advancedBattery");
    public static TextureAtlasSprite advSolarGenerator = new MuseIcon("advSolarGenerator");
    public static TextureAtlasSprite basicBattery = new MuseIcon("basicBattery");
    public static TextureAtlasSprite coalGenerator = new MuseIcon("coalGenerator"); //TODO: does this thing even work?
    public static TextureAtlasSprite eliteBattery = new MuseIcon("eliteBattery");
    public static TextureAtlasSprite kineticGenerator = new MuseIcon("kineticGenerator");
    public static TextureAtlasSprite solarGenerator = new MuseIcon("solarGenerator");
    public static TextureAtlasSprite thermalGenerator = new MuseIcon("thermalGenerator");
    public static TextureAtlasSprite ultimateBattery = new MuseIcon("ultimateBattery");





    public static void registerIcons(TextureStitchEvent.Pre event) {
        // Armor
        apiaristArmor = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID,"modules/silkWisp"));
        basicPlating = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID,"modules/basicplating2"));
        diamondPlating = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/advancedplating2"));
        energyShield = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/energyshield"));
        hazmat = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/greenstar"));
        heatSink = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/heatresistantplating2"));
        mechAssistance = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/mechassistance"));
        nitrogenCoolingSystem = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/coolingsystem"));
        /* Water tank module uses vanilla bucket of water icon */

        // Energy
        advancedBattery = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/mvbattery"));
        advSolarGenerator = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/advsolarhelmet"));
        basicBattery = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/lvbattery"));
        /* FIXME no icon "coalgen" */
        coalGenerator = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/coalgen"));
        eliteBattery = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/hvbattery"));
        kineticGenerator = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/kineticgen"));
        solarGenerator = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/solarhelmet"));
        thermalGenerator = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/heatgenerator"));
        ultimateBattery = event.getMap().registerSprite(new ResourceLocation(ModularPowersuits.MODID, "modules/crystalcapacitor"));




    }
}