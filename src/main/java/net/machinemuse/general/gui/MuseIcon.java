package net.machinemuse.general.gui;


import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

/*
 * These are mostly icons for the Tinker Table GUI and PowerFist GUI
 * Other textures here may just be textures that need to be loaded through the stitch event
 * Icons without an item registered need to be added this way
 * Do not register the same icon twice as this produces weird side effects
 * @author Lehjr
 */
public class MuseIcon extends TextureAtlasSprite {
    protected MuseIcon(String spriteName) {
        super(spriteName);
    }

    /* Armor -------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite apiaristArmor= new MuseIcon("apiaristArmor");
    public static TextureAtlasSprite basicPlating = new MuseIcon("basicPlating");
    public static TextureAtlasSprite diamondPlating = new MuseIcon("diamondPlating");
    public static TextureAtlasSprite energyShield = new MuseIcon("energyShield");
    public static TextureAtlasSprite hazmat = new MuseIcon("hazmat");
    public static TextureAtlasSprite heatSink = new MuseIcon("heatSink");
    public static TextureAtlasSprite mechAssistance = new MuseIcon("mechAssistance");
    public static TextureAtlasSprite nitrogenCoolingSystem = new MuseIcon("nitrogenCoolingSystem");
    /* Water tank module uses vanilla bucket of water icon */

    /* Energy ------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite advancedBattery = new MuseIcon("advancedBattery");
    public static TextureAtlasSprite advSolarGenerator = new MuseIcon("advSolarGenerator");
    public static TextureAtlasSprite basicBattery = new MuseIcon("basicBattery");
    public static TextureAtlasSprite coalGenerator = new MuseIcon("coalGenerator"); //TODO: does this thing even work?
    public static TextureAtlasSprite eliteBattery = new MuseIcon("eliteBattery");
    public static TextureAtlasSprite kineticGenerator = new MuseIcon("kineticGenerator");
    public static TextureAtlasSprite solarGenerator = new MuseIcon("solarGenerator");
    public static TextureAtlasSprite thermalGenerator = new MuseIcon("thermalGenerator");
    public static TextureAtlasSprite ultimateBattery = new MuseIcon("ultimateBattery");

    /* Misc --------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite airtightSeal = new MuseIcon("airtightSeal");
    public static TextureAtlasSprite autoFeeder = new MuseIcon("autoFeeder");
    public static TextureAtlasSprite binoculars = new MuseIcon("binoculars");
    public static TextureAtlasSprite citizenJoe = new MuseIcon("citizenJoe");
    // clock uses vanilla icon
    // compass uses vanilla icon
    public static TextureAtlasSprite coolingSystem = new MuseIcon("coolingSystem");
    public static TextureAtlasSprite cosmeticGlow = new MuseIcon("cosmeticGlow");
    public static TextureAtlasSprite portableCraftingTable = new MuseIcon("portableCraftingTable");
    public static TextureAtlasSprite invisibility = new MuseIcon("invisibility");
    public static TextureAtlasSprite magnet = new MuseIcon("magnet");
    public static TextureAtlasSprite mobRepulsor = new MuseIcon("mobRepulsor");
    public static TextureAtlasSprite nightVision = new MuseIcon("nightVision");
    public static TextureAtlasSprite aurameter = new MuseIcon("aurameter");
    public static TextureAtlasSprite tint = new MuseIcon("tint");
    public static TextureAtlasSprite transparentArmor = new MuseIcon("transparentArmor");
    public static TextureAtlasSprite waterElectrolyzer = new MuseIcon("waterElectrolyzer");

    /* Movement ----------------------------------------------------------------------------------- */
    public static TextureAtlasSprite blinkDrive = new MuseIcon("blinkDrive");
    public static TextureAtlasSprite climbAssist = new MuseIcon("climbAssist");
    public static TextureAtlasSprite flightControl = new MuseIcon("climbAssist");
    public static TextureAtlasSprite glider = new MuseIcon("glider");
    public static TextureAtlasSprite jetBoots = new MuseIcon("jetBoots");
    public static TextureAtlasSprite jetpack = new MuseIcon("jetpack");
    public static TextureAtlasSprite jumpAssist = new MuseIcon("jumpAssist");
    public static TextureAtlasSprite parachute = new MuseIcon("parachute");
    public static TextureAtlasSprite shockAbsorber = new MuseIcon("shockAbsorber");
    public static TextureAtlasSprite sprintAssist = new MuseIcon("sprintAssist");
    public static TextureAtlasSprite swimAssist = new MuseIcon("swimAssist");

    /* Tools -------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite aoePickUpgrade = new MuseIcon("aoePickUpgrade");
    public static TextureAtlasSprite appengECWirelessFluid = new MuseIcon("appengECWirelessFluid");
    public static TextureAtlasSprite appengWireless = new MuseIcon("appengWireless");
    public static TextureAtlasSprite aquaAffinity = new MuseIcon("aquaAffinity");
    public static TextureAtlasSprite axe = new MuseIcon("axe");
    public static TextureAtlasSprite chisel = new MuseIcon("chisel");
    public static TextureAtlasSprite diamondPickUpgrade = new MuseIcon("diamondPickUpgrade");
    public static TextureAtlasSprite dimRiftGen = new MuseIcon("dimRiftGen");
    public static TextureAtlasSprite fieldTinkerer = new MuseIcon("fieldTinkerer");
    // flint and steel using vanilla texture
    // grafter using external texture
    // hoe using vanilla texture
    public static TextureAtlasSprite leafBlower = new MuseIcon("leafBlower");
    public static TextureAtlasSprite luxCapacitor = new MuseIcon("luxCapacitor");
    public static TextureAtlasSprite mffsFieldTeleporter = new MuseIcon("mffsFieldTeleporter");
    public static TextureAtlasSprite ocTerminal = new MuseIcon("ocTerminal");
    public static TextureAtlasSprite omniProbe = new MuseIcon("omniProbe");
    public static TextureAtlasSprite omniwrench = new MuseIcon("omniwrench");
    public static TextureAtlasSprite oreScanner = new MuseIcon("oreScanner");
    public static TextureAtlasSprite cmPSD = new MuseIcon("cmPSD");
    public static TextureAtlasSprite pickaxe = new MuseIcon("pickaxe");
    public static TextureAtlasSprite redstoneLaser = new MuseIcon("redstoneLaser");
    // emulatedTool using external mod texture
    // shears using vanilla texture
    public static TextureAtlasSprite shovel = new MuseIcon("shovel");


    /* Weapons ------------------------------------------------------------------------------------ */
    public static TextureAtlasSprite bladeLauncher = new MuseIcon("spinningblade");
    public static TextureAtlasSprite lightning = new MuseIcon("lightningSummoner");
    public static TextureAtlasSprite meleeAssist = new MuseIcon("meleeAssist");
    public static TextureAtlasSprite plasmaCannon = new MuseIcon("plasmaCannon");
    public static TextureAtlasSprite railgun = new MuseIcon("railgun");
    public static TextureAtlasSprite sonicWeapon = new MuseIcon("sonicWeapon");
    
    /* Things other than module icons ------------------------------------------------------------- */
//    public static TextureAtlasSprite powerFistIcon = new MuseIcon("powerFistIcon");


    public static void registerIcons(TextureStitchEvent.Pre event) {
        /* Armor -------------------------------------------------------------------------------------- */
        apiaristArmor = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/silkWisp"));
        basicPlating = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/basicplating2"));
        diamondPlating = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/advancedplating2"));
        energyShield = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/energyshield"));
        hazmat = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/greenstar"));
        heatSink = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/heatresistantplating2"));
        mechAssistance = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/mechassistance"));
        nitrogenCoolingSystem = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/coolingsystem"));
        /* Water tank module uses vanilla bucket of water icon */

        /* Energy ------------------------------------------------------------------------------------- */
        advancedBattery = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/mvbattery"));
        advSolarGenerator = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/advsolarhelmet"));
        basicBattery = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/lvbattery"));
        /* FIXME no icon "coalgen" */
        coalGenerator = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/coalgen"));
        eliteBattery = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/hvbattery"));
        kineticGenerator = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/kineticgen"));
        solarGenerator = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/solarhelmet"));
        thermalGenerator = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/heatgenerator"));
        ultimateBattery = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/crystalcapacitor"));

        /* Misc --------------------------------------------------------------------------------------- */
        airtightSeal = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/glasspane"));
        autoFeeder = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/autofeeder"));
        binoculars = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/binoculars"));
        citizenJoe = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/greendrone"));
        //coolingSystem = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/coolingsystem"));
        coolingSystem = nitrogenCoolingSystem;
        cosmeticGlow = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/netherstar"));
        portableCraftingTable = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/portablecrafting"));
        invisibility = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/bluedrone"));
        magnet = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/magnetmodule"));
        mobRepulsor = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/magneta"));
        nightVision = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/nightvision"));
        aurameter = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/bluestar"));
        // FIXME should not be the same as cosmetic glow
//        tint = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/netherstar"));
        tint = cosmeticGlow;
        transparentArmor = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/transparentarmor"));
        waterElectrolyzer = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/waterelectrolyzer"));

        /* Movement ----------------------------------------------------------------------------------- */
        blinkDrive = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/alien"));
        climbAssist = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/climbassist"));
        flightControl = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/FlightControlY"));
        glider = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/glider"));
        jetBoots = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/jetboots"));
        jetpack = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/jetpack"));
        jumpAssist = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/jumpassist"));
        parachute = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/parachute"));
        shockAbsorber = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/shockabsorber"));
        sprintAssist = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/sprintassist"));
        swimAssist = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/swimboost"));

        /* Tools -------------------------------------------------------------------------------------- */
        // FIXME this should be different than diamondPickUpgrade
        aoePickUpgrade = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/diamondupgrade1"));
        appengECWirelessFluid = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/ItemWirelessTerminalFluid"));
        appengWireless = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/ItemWirelessTerminal"));
        aquaAffinity = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/aquaaffinity"));
        axe = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/toolaxe"));
        chisel = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/toolpinch"));
        diamondPickUpgrade = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/diamondupgrade1"));
        //dimRiftGen = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/kineticgen"));
        dimRiftGen = kineticGenerator;

        // FIXME should not be the same as transparent armor module
//        fieldTinkerer = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/transparentarmor"));
        fieldTinkerer= transparentArmor;
        leafBlower = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/leafblower"));
        luxCapacitor = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/bluelight"));
        mffsFieldTeleporter = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/fieldteleporter"));
        ocTerminal = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/ocTerminal"));
        omniProbe = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/omniprobe"));
        omniwrench = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/omniwrench"));
        oreScanner = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/orescanner"));
        cmPSD = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/psd"));
        pickaxe = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/toolpick"));
        redstoneLaser = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/laser"));
        shovel = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX +  "modules/toolshovel"));

        /* Weapons ------------------------------------------------------------------------------------ */
        bladeLauncher = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/spinningblade"));
        // FIXME should not be the same as aurameter
        lightning = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/bluestar"));
        meleeAssist = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/toolfist"));
        plasmaCannon =  event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/gravityweapon"));
        railgun = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/electricweapon"));
        sonicWeapon = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "modules/soundweapon"));


        /* Things other than module icons ------------------------------------------------------------- */
//        powerFistIcon = event.getMap().registerSprite(new ResourceLocation(Config.RESOURCE_PREFIX + "items/handitem"));

    }
}