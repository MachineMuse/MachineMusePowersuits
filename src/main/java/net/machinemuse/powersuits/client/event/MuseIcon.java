package net.machinemuse.powersuits.client.event;


import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * These are mostly icons for the Tinker Table GUI and PowerFist GUI
 * Other textures here may just be textures that need to be loaded through the stitch event
 * Icons without an item registered need to be added this way
 * @author Lehjr
 */
@SideOnly(Side.CLIENT)
public class MuseIcon {
    protected MuseIcon() {
    }

    /* Armor -------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite apiaristArmor;
    public static TextureAtlasSprite basicPlating;
    public static TextureAtlasSprite diamondPlating;
    public static TextureAtlasSprite energyShield;
    public static TextureAtlasSprite hazmat;
    public static TextureAtlasSprite heatSink;
    public static TextureAtlasSprite mechAssistance;
    public static TextureAtlasSprite nitrogenCoolingSystem;
    /* Water tank module uses vanilla bucket of water icon */

    /* Cosmetic ----------------------------------------------------------------------------------- */
    public static TextureAtlasSprite citizenJoe;
    public static TextureAtlasSprite cosmeticGlow;
    public static TextureAtlasSprite highPoly;
    public static TextureAtlasSprite tint;

    /* Energy ------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite advancedBattery;
    public static TextureAtlasSprite advSolarGenerator;
    public static TextureAtlasSprite basicBattery;
    public static TextureAtlasSprite coalGenerator;//TODO: get this thing working & add portable fision and fusion cores ?
    public static TextureAtlasSprite eliteBattery;
    public static TextureAtlasSprite kineticGenerator;
    public static TextureAtlasSprite solarGenerator;
    public static TextureAtlasSprite thermalGenerator;
    public static TextureAtlasSprite ultimateBattery;

    /* Misc --------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite airtightSeal;
    public static TextureAtlasSprite autoFeeder;
    public static TextureAtlasSprite binoculars;

    // clock uses vanilla icon
    // compass uses vanilla icon
    public static TextureAtlasSprite coolingSystem;

    public static TextureAtlasSprite portableCraftingTable;
    public static TextureAtlasSprite invisibility;
    public static TextureAtlasSprite magnet;
    public static TextureAtlasSprite mobRepulsor;
    public static TextureAtlasSprite nightVision;
    public static TextureAtlasSprite aurameter;
    public static TextureAtlasSprite transparentArmor;
    public static TextureAtlasSprite waterElectrolyzer;

    /* Movement ----------------------------------------------------------------------------------- */
    public static TextureAtlasSprite blinkDrive;
    public static TextureAtlasSprite climbAssist;
    public static TextureAtlasSprite flightControl;
    public static TextureAtlasSprite glider;
    public static TextureAtlasSprite jetBoots;
    public static TextureAtlasSprite jetpack;
    public static TextureAtlasSprite jumpAssist;
    public static TextureAtlasSprite parachute;
    public static TextureAtlasSprite shockAbsorber;
    public static TextureAtlasSprite sprintAssist;
    public static TextureAtlasSprite swimAssist;

    /* Tools -------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite aoePickUpgrade;
    public static TextureAtlasSprite appengECWirelessFluid;
    public static TextureAtlasSprite appengWireless;
    public static TextureAtlasSprite aquaAffinity;
    public static TextureAtlasSprite axe;
    public static TextureAtlasSprite chisel;
    public static TextureAtlasSprite diamondPickUpgrade;
    public static TextureAtlasSprite dimRiftGen;
    public static TextureAtlasSprite fieldTinkerer;
    // flint and steel using vanilla texture
    // grafter using external texture
    // hoe using vanilla texture
    public static TextureAtlasSprite leafBlower;
    public static TextureAtlasSprite luxCapacitor;
    public static TextureAtlasSprite mffsFieldTeleporter;
    public static TextureAtlasSprite ocTerminal;
    public static TextureAtlasSprite omniProbe;
    public static TextureAtlasSprite omniwrench;
    public static TextureAtlasSprite oreScanner;
    public static TextureAtlasSprite cmPSD;
    public static TextureAtlasSprite pickaxe;
    public static TextureAtlasSprite redstoneLaser;
    // emulatedTool using external mod texture
    // shears using vanilla texture
    public static TextureAtlasSprite shovel;

    /* Weapons ------------------------------------------------------------------------------------ */
    public static TextureAtlasSprite bladeLauncher;
    public static TextureAtlasSprite lightning;
    public static TextureAtlasSprite meleeAssist;
    public static TextureAtlasSprite plasmaCannon;
    public static TextureAtlasSprite railgun;
    public static TextureAtlasSprite sonicWeapon;
    
    /* Things other than module icons ------------------------------------------------------------- */
    public static TextureAtlasSprite luxCapacitorTexture;
    public static TextureAtlasSprite powerFistTexture;


    public static void registerIcons(TextureStitchEvent.Pre event) {
        /* Armor -------------------------------------------------------------------------------------- */
        apiaristArmor = register(event, "module/silkWisp");
        basicPlating = register(event,"module/basicplating2");
        diamondPlating = register(event, "module/advancedplating2");
        energyShield = register(event, "module/energyshield");
        hazmat = register(event, "module/greenstar");
        heatSink = register(event, "module/heatresistantplating2");
        mechAssistance = register(event, "module/mechassistance");
        nitrogenCoolingSystem = register(event, "module/coolingsystem");
        /* Water tank module uses vanilla bucket of water icon */

        /* Cosmetic ----------------------------------------------------------------------------------- */
        citizenJoe = register(event, "module/greendrone");
        cosmeticGlow = register(event, "module/netherstar");
        highPoly = register(event, "module/bluedrone");
        tint = register(event, "module/netherstar");

        /* Energy ------------------------------------------------------------------------------------- */
        advancedBattery = register(event, "module/mvbattery");
        advSolarGenerator = register(event, "module/advsolarhelmet");
        basicBattery = register(event, "module/lvbattery");
        /* FIXME no icon "coalgen" */
        coalGenerator = register(event, "module/coalgen");
        eliteBattery = register(event, "module/hvbattery");
        kineticGenerator = register(event, "module/kineticgen");
        solarGenerator = register(event, "module/solarhelmet");
        thermalGenerator = register(event, "module/heatgenerator");
        ultimateBattery = register(event, "module/crystalcapacitor");

        /* Misc --------------------------------------------------------------------------------------- */
        airtightSeal = register(event, "module/glasspane");
        autoFeeder = register(event, "module/autofeeder");
        binoculars = register(event, "module/binoculars");
        coolingSystem = register(event, "module/coolingsystem");
        portableCraftingTable = register(event, "module/portablecrafting");
        invisibility = register(event, "module/bluedrone");
        magnet = register(event, "module/magnetmodule");
        mobRepulsor = register(event, "module/magneta");
        nightVision = register(event, "module/nightvision");
        aurameter = register(event, "module/bluestar");
        transparentArmor = register(event, "module/transparentarmor");
        waterElectrolyzer = register(event, "module/waterelectrolyzer");

        /* Movement ----------------------------------------------------------------------------------- */
        blinkDrive = register(event, "module/alien");
        climbAssist = register(event, "module/climbassist");
        flightControl = register(event, "module/FlightControlY");
        glider = register(event, "module/glider");
        jetBoots = register(event, "module/jetboots");
        jetpack = register(event, "module/jetpack");
        jumpAssist = register(event, "module/jumpassist");
        parachute = register(event, "module/parachute");
        shockAbsorber = register(event, "module/shockabsorber");
        sprintAssist = register(event, "module/sprintassist");
        swimAssist = register(event, "module/swimboost");

        /* Tools -------------------------------------------------------------------------------------- */
        aoePickUpgrade = register(event, "module/diamondupgrade1");
        appengECWirelessFluid = register(event, "module/itemwirelessterminalfluid");
        appengWireless = register(event, "module/ItemWirelessTerminal");
        aquaAffinity = register(event, "module/aquaaffinity");
        axe = register(event, "module/toolaxe");
        chisel = register(event, "module/toolpinch");
        diamondPickUpgrade = register(event, "module/diamondupgrade1");
        dimRiftGen = register(event, "module/kineticgen");
        fieldTinkerer = register(event, "module/transparentarmor");
        leafBlower = register(event, "module/leafblower");
        luxCapacitor = register(event, "module/bluelight");
        mffsFieldTeleporter = register(event, "module/fieldteleporter");
        ocTerminal = register(event, "module/ocTerminal");
        omniProbe = register(event, "module/omniprobe");
        omniwrench = register(event, "module/omniwrench");
        oreScanner = register(event, "module/orescanner");
        cmPSD = register(event, "module/psd");
        pickaxe = register(event, "module/toolpick");
        redstoneLaser = register(event, "module/laser");
        shovel = register(event, "module/toolshovel");

        /* Weapons ------------------------------------------------------------------------------------ */
        bladeLauncher = register(event,"module/spinningblade");
        lightning = register(event,"module/bluestar");
        meleeAssist = register(event,"module/toolfist");
        plasmaCannon = register(event, "module/gravityweapon");
        railgun = register(event,"module/electricweapon");
        sonicWeapon = register(event,"module/soundweapon");

        /* Things other than module icons ------------------------------------------------------------- */
        luxCapacitorTexture = register(event, "models/luxcapacitor");
        powerFistTexture = register(event, "models/tool2");

        MPSConfig.loadModelSpecs(event);
    }

    private static TextureAtlasSprite register(TextureStitchEvent.Pre event, String location) {
        return event.getMap().registerSprite(new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, location));
    }
}