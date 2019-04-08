package net.machinemuse.powersuits.client.event;


import net.machinemuse.powersuits.constants.MPSResourceConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;

/*
 * These are mostly icons for the Tinker Table GUI and PowerFist GUI
 * Other textures here may just be textures that need to be loaded through the stitch event
 * Icons without an item registered need to be added this way
 * @author Lehjr
 */
@OnlyIn(Dist.CLIENT)
public class MuseIcon {
//    /* Armor -------------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite apiaristArmor;
//    public static TextureAtlasSprite basicPlating;
//    public static TextureAtlasSprite diamondPlating;
//    public static TextureAtlasSprite energyShield;
//    public static TextureAtlasSprite hazmat;
//    public static TextureAtlasSprite heatSink;
//
//
//    public static TextureAtlasSprite mechAssistance;
//    public static TextureAtlasSprite advancedCoolingSystem;
//    /* Cosmetic ----------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite citizenJoe;
//    /* Water tank module uses vanilla bucket of water icon */
//    public static TextureAtlasSprite cosmeticGlow;
//    public static TextureAtlasSprite highPoly;
//    public static TextureAtlasSprite tint;
//    /* Energy ------------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite advancedBattery;
//    public static TextureAtlasSprite advSolarGenerator;
//    public static TextureAtlasSprite basicBattery;
//    public static TextureAtlasSprite coalGenerator;//TODO: get this thing working & add portable fision and fusion cores ?
//    public static TextureAtlasSprite eliteBattery;
//    public static TextureAtlasSprite kineticGenerator;
//    public static TextureAtlasSprite solarGenerator;
//    public static TextureAtlasSprite thermalGenerator;
//    public static TextureAtlasSprite ultimateBattery;
//    /* Misc --------------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite airtightSeal;
//    public static TextureAtlasSprite autoFeeder;
//    public static TextureAtlasSprite binoculars;
//    // clock uses vanilla icon
//    // compass uses vanilla icon
//    public static TextureAtlasSprite basicCoolingSystem;
//    public static TextureAtlasSprite portableCraftingTable;
//    public static TextureAtlasSprite invisibility;
//    public static TextureAtlasSprite magnet;
//    public static TextureAtlasSprite mobRepulsor;
//    public static TextureAtlasSprite nightVision;
//    public static TextureAtlasSprite aurameter;
//    public static TextureAtlasSprite transparentArmor;
//    public static TextureAtlasSprite waterElectrolyzer;
//    /* Movement ----------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite blinkDrive;
//    public static TextureAtlasSprite climbAssist;
//    public static TextureAtlasSprite flightControl;
//    public static TextureAtlasSprite glider;
//    public static TextureAtlasSprite jetBoots;
//    public static TextureAtlasSprite jetpack;
//    public static TextureAtlasSprite jumpAssist;
//    public static TextureAtlasSprite parachute;
//    public static TextureAtlasSprite shockAbsorber;
//    public static TextureAtlasSprite sprintAssist;
//    public static TextureAtlasSprite swimAssist;
//
//    /* Mining Enhancements ------------------------------------------------------------------------ */
//    public static TextureAtlasSprite emptyModule;
//
//
//    /* Tools -------------------------------------------------------------------------------------- */
//    public static TextureAtlasSprite aoePickUpgrade;
//    public static TextureAtlasSprite appengECWirelessFluid;
//    public static TextureAtlasSprite appengWireless;
//    public static TextureAtlasSprite aquaAffinity;
//    public static TextureAtlasSprite axe;
//    public static TextureAtlasSprite chisel;
//    public static TextureAtlasSprite diamondPickUpgrade;
//    public static TextureAtlasSprite dimRiftGen;
//    public static TextureAtlasSprite fieldTinkerer;
//    // flint and steel using vanilla texture
//    // grafter using external texture
//    // hoe using vanilla texture
//    public static TextureAtlasSprite leafBlower;
//    public static TextureAtlasSprite luxCapacitor;
//    public static TextureAtlasSprite madModule;
//    public static TextureAtlasSprite mffsFieldTeleporter;
//    public static TextureAtlasSprite ocTerminal;
//    public static TextureAtlasSprite omniProbe;
//    public static TextureAtlasSprite omniwrench;
//    public static TextureAtlasSprite oreScanner;
//    public static TextureAtlasSprite cmPSD;
//    public static TextureAtlasSprite pickaxe;
//    public static TextureAtlasSprite redstoneLaser;
//    // emulatedTool using external mod texture
//    // shears using vanilla texture
//    public static TextureAtlasSprite shovel;
//    /* Weapons ------------------------------------------------------------------------------------ */
//    public static TextureAtlasSprite bladeLauncher;
//    public static TextureAtlasSprite lightning;
//    public static TextureAtlasSprite meleeAssist;
//    public static TextureAtlasSprite plasmaCannon;
//    public static TextureAtlasSprite railgun;
//    public static TextureAtlasSprite sonicWeapon;
    /* Things other than module icons ------------------------------------------------------------- */
    public static TextureAtlasSprite luxCapacitorTexture;
    public static TextureAtlasSprite powerFistTexture;
    public static TextureAtlasSprite tinkerTableTexture;

//
//
    protected MuseIcon() {
    }

    public static void registerIcons(TextureStitchEvent.Pre event) {
//        /* Armor -------------------------------------------------------------------------------------- */
//        apiaristArmor = register(event, "modules/silkwisp");
//        basicPlating = register(event, "modules/basicplating2");
//        diamondPlating = register(event, "modules/advancedplating2");
//        energyShield = register(event, "modules/energyshield");
//        hazmat = register(event, "modules/greenstar");
//        heatSink = register(event, "modules/heatresistantplating2");
//        mechAssistance = register(event, "modules/mechassistance");
//        advancedCoolingSystem = register(event, "modules/coolingsystem");
//        /* Water tank module uses vanilla bucket of water icon */
//
//        /* Cosmetic ----------------------------------------------------------------------------------- */
//        citizenJoe = register(event, "modules/greendrone");
//        cosmeticGlow = register(event, "modules/netherstar");
//        highPoly = register(event, "modules/bluedrone");
//        tint = register(event, "modules/netherstar");
//
//        /* Energy ------------------------------------------------------------------------------------- */
//        advancedBattery = register(event, "modules/mvbattery");
//        advSolarGenerator = register(event, "modules/advsolarhelmet");
//        basicBattery = register(event, "modules/lvbattery");
//        /* FIXME no icon "coalgen" */
//        coalGenerator = register(event, "modules/coalgen");
//        eliteBattery = register(event, "modules/hvbattery");
//        kineticGenerator = register(event, "modules/kineticgen");
//        solarGenerator = register(event, "modules/solarhelmet");
//        thermalGenerator = register(event, "modules/heatgenerator");
//        ultimateBattery = register(event, "modules/crystalcapacitor");
//
//        /* Misc --------------------------------------------------------------------------------------- */
//        airtightSeal = register(event, "modules/glasspane");
//        autoFeeder = register(event, "modules/autofeeder");
//        binoculars = register(event, "modules/binoculars");
//        basicCoolingSystem = register(event, "modules/coolingsystem");
//        portableCraftingTable = register(event, "modules/portablecrafting");
//        invisibility = register(event, "modules/bluedrone");
//        magnet = register(event, "modules/magnetmodule");
//        mobRepulsor = register(event, "modules/magneta");
//        nightVision = register(event, "modules/nightvision");
//        aurameter = register(event, "modules/bluestar");
//        transparentArmor = register(event, "modules/transparentarmor");
//        waterElectrolyzer = register(event, "modules/waterelectrolyzer");
//
//        /* Movement ----------------------------------------------------------------------------------- */
//        blinkDrive = register(event, "modules/alien");
//        climbAssist = register(event, "modules/climbassist");
//        flightControl = register(event, "modules/flight_control_y");
//        glider = register(event, "modules/glider");
//        jetBoots = register(event, "modules/jetboots");
//        jetpack = register(event, "modules/jetpack");
//        jumpAssist = register(event, "modules/jumpassist");
//        parachute = register(event, "modules/parachute");
//        shockAbsorber = register(event, "modules/shockabsorber");
//        sprintAssist = register(event, "modules/sprintassist");
//        swimAssist = register(event, "modules/swimboost");
//
//        /* Mining Enhancements ------------------------------------------------------------------------ */
//        emptyModule = register(event, "modules/empty_set");
//
//
//        /* Tools -------------------------------------------------------------------------------------- */
//        aoePickUpgrade = register(event, "modules/diamondupgrade1");
//        appengECWirelessFluid = register(event, "modules/item_wireless_terminal_fluid");
//        appengWireless = register(event, "modules/item_wireless_terminal");
//        aquaAffinity = register(event, "modules/aquaaffinity");
//        axe = register(event, "modules/toolaxe");
//        chisel = register(event, "modules/toolpinch");
//        diamondPickUpgrade = register(event, "modules/diamondupgrade1");
//        dimRiftGen = register(event, "modules/kineticgen");
//        fieldTinkerer = register(event, "modules/transparentarmor");
//        leafBlower = register(event, "modules/leafblower");
//        luxCapacitor = register(event, "modules/bluelight");
//        madModule = register(event, "modules/atomic_disassembler");
//        mffsFieldTeleporter = register(event, "modules/fieldteleporter");
//        ocTerminal = register(event, "modules/oc_terminal");
//        omniProbe = register(event, "modules/omniprobe");
//        omniwrench = register(event, "modules/omniwrench");
//        oreScanner = register(event, "modules/orescanner");
//        cmPSD = register(event, "modules/psd");
//        pickaxe = register(event, "modules/toolpick");
//        redstoneLaser = register(event, "modules/laser");
//        shovel = register(event, "modules/toolshovel");
//
//        /* Weapons ------------------------------------------------------------------------------------ */
//        bladeLauncher = register(event, "modules/spinningblade");
//        lightning = register(event, "modules/bluestar");
//        meleeAssist = register(event, "modules/toolfist");
//        plasmaCannon = register(event, "modules/gravityweapon");
//        railgun = register(event, "modules/electricweapon");
//        sonicWeapon = register(event, "modules/soundweapon");
//
        /* Things other than module icons ------------------------------------------------------------- */
        luxCapacitorTexture = register(event, "models/luxcapacitor");
        powerFistTexture = register(event, "models/powerfist");
        tinkerTableTexture = register(event, "block/powersuitsworkbench");
    }

    private static TextureAtlasSprite register(TextureStitchEvent.Pre event, String location) {
        ResourceLocation res = new ResourceLocation(MPSResourceConstants.RESOURCE_DOMAIN, location);
        event.getMap().registerSprite(Minecraft.getInstance().getResourceManager(), res);
        return event.getMap().getSprite(res);
    }
}