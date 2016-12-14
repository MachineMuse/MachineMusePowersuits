package net.machinemuse.general.gui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * MuseIcon is just a helper class to make it more convenient to have multiple sprite sheets and to keep all the icon indices in one place.
 *
 * @author MachineMuse
 */
public class MuseIcon {
    public static final String ICON_PREFIX = "powersuits:";

    // Placeholder icons
    public static final MuseIcon ORB_1_GREEN = new MuseIcon("greendrone");
    public static final MuseIcon ORB_1_RED = new MuseIcon("reddrone");
    public static final MuseIcon ORB_1_BLUE = new MuseIcon("bluedrone");
    public static final MuseIcon PLATE_1_GREEN = new MuseIcon("greenlight");
    public static final MuseIcon PLATE_1_RED = new MuseIcon("redlight");
    public static final MuseIcon PLATE_1_BLUE = new MuseIcon("bluelight");
    public static final MuseIcon NEXUS_1_RED = new MuseIcon("redstar");
    public static final MuseIcon NEXUS_1_GREEN = new MuseIcon("greenstar");
    public static final MuseIcon NEXUS_1_BLUE = new MuseIcon("bluestar");
    public static final MuseIcon PLATE_2_GREEN = new MuseIcon("greenplate");
    public static final MuseIcon PLATE_2_BLUE = new MuseIcon("blueplate");
    public static final MuseIcon PLATE_2_RED = new MuseIcon("redplate");
    public static final MuseIcon INDICATOR_1_GREEN = new MuseIcon("greenphaser");
    public static final MuseIcon INDICATOR_1_RED = new MuseIcon("redphaser");
    public static final MuseIcon INDICATOR_1_BLUE = new MuseIcon("bluephaser");

    // Item icons
    public static final MuseIcon TOOL_AXE = new MuseIcon("toolaxe");
    public static final MuseIcon TOOL_PICK = new MuseIcon("toolaxe");
    public static final MuseIcon TOOL_SHOVEL = new MuseIcon("toolaxe");
    public static final MuseIcon TOOL_SHEARS = new MuseIcon("toolaxe");
    public static final MuseIcon TOOL_FIST = new MuseIcon("toolaxe");
    public static final MuseIcon TOOL_PINCH = new MuseIcon("toolaxe");

    // Module icons
    public static final MuseIcon JETBOOTS = new MuseIcon("jetboots");
    public static final MuseIcon JETPACK = new MuseIcon("jetboots");
    public static final MuseIcon GLIDER = new MuseIcon("jetboots");
    public static final MuseIcon GRAVITY_ENGINE = new MuseIcon("jetboots");
    public static final MuseIcon SPRINT_ASSIST = new MuseIcon("jetboots");
    public static final MuseIcon JUMP_ASSIST = new MuseIcon("jetboots");
    public static final MuseIcon ENERGY_SHIELD = new MuseIcon("jetboots");
    public static final MuseIcon ITEM_IRON_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon ITEM_DIAMOND_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon ITEM_TUNGSTEN_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon CLAW_OPEN = new MuseIcon("jetboots");
    public static final MuseIcon WEAPON_FIRE = new MuseIcon("jetboots");
    public static final MuseIcon HEART = new MuseIcon("jetboots");
    public static final MuseIcon FIELD_EMITTER_GREEN = new MuseIcon("jetboots");
    public static final MuseIcon FIELD_EMITTER_RED = new MuseIcon("jetboots");
    public static final MuseIcon SHOCK_ABSORBER = new MuseIcon("jetboots");
    public static final MuseIcon PARACHUTE_MODULE = new MuseIcon("jetboots");
    public static final MuseIcon WATER_ELECTROLYZER = new MuseIcon("jetboots");
    public static final MuseIcon TRANSPARENT_ARMOR = new MuseIcon("jetboots");
    public static final MuseIcon SWIM_BOOST = new MuseIcon("jetboots");
    public static final MuseIcon STEP_ASSIST = new MuseIcon("jetboots");
    public static final MuseIcon DIAMOND_PICK = new MuseIcon("jetboots");
    public static final MuseIcon CLAW_CLOSED = new MuseIcon("jetboots");
    public static final MuseIcon WEAPON_ELECTRIC = new MuseIcon("jetboots");
    public static final MuseIcon BATTERY1 = new MuseIcon("jetboots");
    public static final MuseIcon CIRCUIT = new MuseIcon("jetboots");
    public static final MuseIcon WIRING = new MuseIcon("jetboots");
    public static final MuseIcon SOLENOID = new MuseIcon("jetboots");
    public static final MuseIcon SERVOMOTOR = new MuseIcon("jetboots");
    public static final MuseIcon GLIDERWING = new MuseIcon("jetboots");
    public static final MuseIcon IONTHRUSTER = new MuseIcon("jetboots");
    public static final MuseIcon LVCAPACITOR = new MuseIcon("jetboots");
    public static final MuseIcon PARACHUTE = new MuseIcon("jetboots");
    public static final MuseIcon AQUA_AFFINITY = new MuseIcon("jetboots");
    public static final MuseIcon CLAW_LASER = new MuseIcon("jetboots");
    public static final MuseIcon WEAPON_SOUND = new MuseIcon("jetboots");
    public static final MuseIcon BATTERY2 = new MuseIcon("jetboots");
    public static final MuseIcon LAMP = new MuseIcon("jetboots");
    public static final MuseIcon MYOFIBER_PASTE = new MuseIcon("jetboots");
    public static final MuseIcon CARBON_MYOFIBER = new MuseIcon("jetboots");
    public static final MuseIcon ARTIFICIAL_MUSCLE = new MuseIcon("jetboots");
    public static final MuseIcon CRYSTAL_BUBBLE = new MuseIcon("jetboots");
    public static final MuseIcon COOLING_SYSTEM = new MuseIcon("jetboots");
    public static final MuseIcon MVCAPACITOR = new MuseIcon("jetboots");
    public static final MuseIcon SCANNER = new MuseIcon("jetboots");
    public static final MuseIcon POWERTOOL = new MuseIcon("handitem");
    public static final MuseIcon WEAPON_GRAVITY = new MuseIcon("jetboots");
    public static final MuseIcon BATTERYCRYSTAL = new MuseIcon("jetboots");
    public static final MuseIcon FLOATING_CRYSTAL = new MuseIcon("jetboots");
    public static final MuseIcon MODULE_IRON_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon MODULE_DIAMOND_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon MODULE_TUNGSTEN_PLATING = new MuseIcon("jetboots");
    public static final MuseIcon FIELD_GENERATOR = new MuseIcon("jetboots");
    public static final MuseIcon HVCAPACITOR = new MuseIcon("jetboots");
    public static final MuseIcon HOLOGRAM_EMITTER = new MuseIcon("jetboots");
    public static final MuseIcon LASER = new MuseIcon("jetboots");
    public static final MuseIcon ALIEN = new MuseIcon("jetboots");
    public static final MuseIcon NETHERSTAR = new MuseIcon("jetboots");
    public static final MuseIcon ARCREACTOR = new MuseIcon("jetboots");
    public static final MuseIcon PUNCHY = new MuseIcon("jetboots");

    protected String texturename;
    protected IIcon icon;

    public MuseIcon(String texturename) {
        super();
        this.texturename = texturename;
        this.icon = null;
    }

    public String getTexturename() {
        return texturename;
    }

    public IIcon getIconRegistration() {
        return icon;
    }

    public void register(IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(ICON_PREFIX + texturename);
    }
}
