package net.machinemuse.powersuits.client.event;


import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
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
    /* Armor -------------------------------------------------------------------------------------- */
    public static TextureAtlasSprite apiaristArmor;
    public static TextureAtlasSprite basicPlating;
    public static TextureAtlasSprite diamondPlating;
    public static TextureAtlasSprite energyShield;
    public static TextureAtlasSprite hazmat;
    public static TextureAtlasSprite heatSink;
    public static TextureAtlasSprite mechAssistance;
    public static TextureAtlasSprite advancedCoolingSystem;
    /* Cosmetic ----------------------------------------------------------------------------------- */
    public static TextureAtlasSprite citizenJoe;
    /* Water tank module uses vanilla bucket of water icon */
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
    public static TextureAtlasSprite basicCoolingSystem;
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

    /* Mining Enhancements ------------------------------------------------------------------------ */
    public static TextureAtlasSprite emptyModule;


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
    public static TextureAtlasSprite madModule;
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
    protected MuseIcon() {
    }

    public static void registerIcons(TextureMap map) {
        /* Armor -------------------------------------------------------------------------------------- */
        apiaristArmor = register(map, "modules/silkWisp");
        basicPlating = register(map, "modules/basicplating2");
        diamondPlating = register(map, "modules/advancedplating2");
        energyShield = register(map, "modules/energyshield");
        hazmat = register(map, "modules/greenstar");
        heatSink = register(map, "modules/heatresistantplating2");
        mechAssistance = register(map, "modules/mechassistance");
        advancedCoolingSystem = register(map, "modules/coolingsystem");
        /* Water tank module uses vanilla bucket of water icon */

        /* Cosmetic ----------------------------------------------------------------------------------- */
        citizenJoe = register(map, "modules/greendrone");
        cosmeticGlow = register(map, "modules/netherstar");
        highPoly = register(map, "modules/bluedrone");
        tint = register(map, "modules/netherstar");

        /* Energy ------------------------------------------------------------------------------------- */
        advancedBattery = register(map, "modules/mvbattery");
        advSolarGenerator = register(map, "modules/advsolarhelmet");
        basicBattery = register(map, "modules/lvbattery");
        /* FIXME no icon "coalgen" */
        coalGenerator = register(map, "modules/coalgen");
        eliteBattery = register(map, "modules/hvbattery");
        kineticGenerator = register(map, "modules/kineticgen");
        solarGenerator = register(map, "modules/solarhelmet");
        thermalGenerator = register(map, "modules/heatgenerator");
        ultimateBattery = register(map, "modules/crystalcapacitor");

        /* Misc --------------------------------------------------------------------------------------- */
        airtightSeal = register(map, "modules/glasspane");
        autoFeeder = register(map, "modules/autofeeder");
        binoculars = register(map, "modules/binoculars");
        basicCoolingSystem = register(map, "modules/coolingsystem");
        portableCraftingTable = register(map, "modules/portablecrafting");
        invisibility = register(map, "modules/bluedrone");
        magnet = register(map, "modules/magnetmodule");
        mobRepulsor = register(map, "modules/magneta");
        nightVision = register(map, "modules/nightvision");
        aurameter = register(map, "modules/bluestar");
        transparentArmor = register(map, "modules/transparentarmor");
        waterElectrolyzer = register(map, "modules/waterelectrolyzer");

        /* Movement ----------------------------------------------------------------------------------- */
        blinkDrive = register(map, "modules/alien");
        climbAssist = register(map, "modules/climbassist");
        flightControl = register(map, "modules/FlightControlY");
        glider = register(map, "modules/glider");
        jetBoots = register(map, "modules/jetboots");
        jetpack = register(map, "modules/jetpack");
        jumpAssist = register(map, "modules/jumpassist");
        parachute = register(map, "modules/parachute");
        shockAbsorber = register(map, "modules/shockabsorber");
        sprintAssist = register(map, "modules/sprintassist");
        swimAssist = register(map, "modules/swimboost");

        /* Mining Enhancements ------------------------------------------------------------------------ */
        emptyModule = register(map, "modules/empty_set");


        /* Tools -------------------------------------------------------------------------------------- */
        aoePickUpgrade = register(map, "modules/diamondupgrade1");
        appengECWirelessFluid = register(map, "modules/ItemWirelessTerminalFluid");
        appengWireless = register(map, "modules/ItemWirelessTerminal");
        aquaAffinity = register(map, "modules/aquaaffinity");
        axe = register(map, "modules/toolaxe");
        chisel = register(map, "modules/toolpinch");
        diamondPickUpgrade = register(map, "modules/diamondupgrade1");
        dimRiftGen = register(map, "modules/kineticgen");
        fieldTinkerer = register(map, "modules/transparentarmor");
        leafBlower = register(map, "modules/leafblower");
        luxCapacitor = register(map, "modules/bluelight");
        madModule = register(map, "modules/atomic_disassembler");
        mffsFieldTeleporter = register(map, "modules/fieldteleporter");
        ocTerminal = register(map, "modules/ocTerminal");
        omniProbe = register(map, "modules/omniprobe");
        omniwrench = register(map, "modules/omniwrench");
        oreScanner = register(map, "modules/orescanner");
        cmPSD = register(map, "modules/psd");
        pickaxe = register(map, "modules/toolpick");
        redstoneLaser = register(map, "modules/laser");
        shovel = register(map, "modules/toolshovel");

        /* Weapons ------------------------------------------------------------------------------------ */
        bladeLauncher = register(map, "modules/spinningblade");
        lightning = register(map, "modules/bluestar");
        meleeAssist = register(map, "modules/toolfist");
        plasmaCannon = register(map, "modules/gravityweapon");
        railgun = register(map, "modules/electricweapon");
        sonicWeapon = register(map, "modules/soundweapon");

        /* Things other than module icons ------------------------------------------------------------- */
        luxCapacitorTexture = register(map, "models/luxcapacitor");
        powerFistTexture = register(map, "models/powerfist");
    }

    private static TextureAtlasSprite register(TextureMap map, String location) {
        return map.registerSprite(new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, location));
    }
}