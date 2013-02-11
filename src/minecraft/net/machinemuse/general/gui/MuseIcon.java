package net.machinemuse.general.gui;

import net.machinemuse.powersuits.common.Config;

/**
 * MuseIcon is just a helper class to make it more convenient to have multiple
 * sprite sheets and to keep all the icon indices in one place.
 * 
 * @author MachineMuse
 */
public class MuseIcon {
	public static final String SEBK_ICON_PATH = Config.SEBK_ICON_PATH;
	public static final String WC_ICON_PATH = Config.WC_ICON_PATH;
	public static final String MUSE_ICON_PATH = Config.MUSE_ICON_PATH;

	// Placeholder icons
	public static final MuseIcon ORB_1_GREEN = new MuseIcon(SEBK_ICON_PATH, 0);
	public static final MuseIcon ORB_1_RED = new MuseIcon(SEBK_ICON_PATH, 1);
	public static final MuseIcon ORB_1_BLUE = new MuseIcon(SEBK_ICON_PATH, 2);
	public static final MuseIcon PLATE_1_GREEN = new MuseIcon(SEBK_ICON_PATH, 3);
	public static final MuseIcon PLATE_1_RED = new MuseIcon(SEBK_ICON_PATH, 4);
	public static final MuseIcon PLATE_1_BLUE = new MuseIcon(SEBK_ICON_PATH, 5);
	public static final MuseIcon NEXUS_1_RED = new MuseIcon(SEBK_ICON_PATH, 6);
	public static final MuseIcon NEXUS_1_GREEN = new MuseIcon(SEBK_ICON_PATH, 7);
	public static final MuseIcon NEXUS_1_BLUE = new MuseIcon(SEBK_ICON_PATH, 8);
	public static final MuseIcon PLATE_2_GREEN = new MuseIcon(SEBK_ICON_PATH, 16);
	public static final MuseIcon PLATE_2_BLUE = new MuseIcon(SEBK_ICON_PATH, 17);
	public static final MuseIcon PLATE_2_RED = new MuseIcon(SEBK_ICON_PATH, 18);
	public static final MuseIcon INDICATOR_1_GREEN = new MuseIcon(SEBK_ICON_PATH, 19);
	public static final MuseIcon INDICATOR_1_RED = new MuseIcon(SEBK_ICON_PATH, 20);
	public static final MuseIcon INDICATOR_1_BLUE = new MuseIcon(SEBK_ICON_PATH, 21);

	// Item icons
	public static final MuseIcon ARMOR_HEAD = new MuseIcon(SEBK_ICON_PATH, 15);
	public static final MuseIcon ARMOR_TORSO = new MuseIcon(SEBK_ICON_PATH, 31);
	public static final MuseIcon ARMOR_LEGS = new MuseIcon(SEBK_ICON_PATH, 47);
	public static final MuseIcon ARMOR_FEET = new MuseIcon(SEBK_ICON_PATH, 63);
	public static final MuseIcon TOOL_AXE = new MuseIcon(SEBK_ICON_PATH, 9);
	public static final MuseIcon TOOL_PICK = new MuseIcon(SEBK_ICON_PATH, 10);
	public static final MuseIcon TOOL_SHOVEL = new MuseIcon(SEBK_ICON_PATH, 11);
	public static final MuseIcon TOOL_SHEARS = new MuseIcon(SEBK_ICON_PATH, 12);
	public static final MuseIcon TOOL_FIST = new MuseIcon(SEBK_ICON_PATH, 13);
	public static final MuseIcon TOOL_PINCH = new MuseIcon(SEBK_ICON_PATH, 14);

	// Module icons
	public static final MuseIcon JETBOOTS = new MuseIcon(WC_ICON_PATH, 0);
	public static final MuseIcon JETPACK = new MuseIcon(WC_ICON_PATH, 1);
	public static final MuseIcon GLIDER = new MuseIcon(WC_ICON_PATH, 2);
	public static final MuseIcon GRAVITY_ENGINE = new MuseIcon(WC_ICON_PATH, 3);
	public static final MuseIcon SPRINT_ASSIST = new MuseIcon(WC_ICON_PATH, 4);
	public static final MuseIcon JUMP_ASSIST = new MuseIcon(WC_ICON_PATH, 5);
	public static final MuseIcon ENERGY_SHIELD = new MuseIcon(WC_ICON_PATH, 6);
	public static final MuseIcon ITEM_IRON_PLATING = new MuseIcon(WC_ICON_PATH, 7);
	public static final MuseIcon ITEM_DIAMOND_PLATING = new MuseIcon(WC_ICON_PATH, 8);
	public static final MuseIcon ITEM_TUNGSTEN_PLATING = new MuseIcon(WC_ICON_PATH, 9);
	public static final MuseIcon CLAW_OPEN = new MuseIcon(WC_ICON_PATH, 11);
	public static final MuseIcon WEAPON_FIRE = new MuseIcon(WC_ICON_PATH, 12);
	public static final MuseIcon HEART = new MuseIcon(WC_ICON_PATH, 13);
	public static final MuseIcon FIELD_EMITTER_GREEN = new MuseIcon(WC_ICON_PATH, 14);
	public static final MuseIcon FIELD_EMITTER_RED = new MuseIcon(WC_ICON_PATH, 15);
	public static final MuseIcon SHOCK_ABSORBER = new MuseIcon(WC_ICON_PATH, 16);
	public static final MuseIcon PARACHUTE_MODULE = new MuseIcon(WC_ICON_PATH, 18);
	public static final MuseIcon WATER_ELECTROLYZER = new MuseIcon(WC_ICON_PATH, 19);
	public static final MuseIcon TRANSPARENT_ARMOR = new MuseIcon(WC_ICON_PATH, 20);
	public static final MuseIcon SWIM_BOOST = new MuseIcon(WC_ICON_PATH, 21);
	public static final MuseIcon STEP_ASSIST = new MuseIcon(WC_ICON_PATH, 22);
	public static final MuseIcon DIAMOND_PICK = new MuseIcon(WC_ICON_PATH, 26);
	public static final MuseIcon CLAW_CLOSED = new MuseIcon(WC_ICON_PATH, 27);
	public static final MuseIcon WEAPON_ELECTRIC = new MuseIcon(WC_ICON_PATH, 28);
	public static final MuseIcon BATTERY1 = new MuseIcon(WC_ICON_PATH, 30);
	public static final MuseIcon CIRCUIT = new MuseIcon(WC_ICON_PATH, 31);
	public static final MuseIcon WIRING = new MuseIcon(WC_ICON_PATH, 32);
	public static final MuseIcon SOLENOID = new MuseIcon(WC_ICON_PATH, 33);
	public static final MuseIcon SERVOMOTOR = new MuseIcon(WC_ICON_PATH, 34);
	public static final MuseIcon GLIDERWING = new MuseIcon(WC_ICON_PATH, 35);
	public static final MuseIcon IONTHRUSTER = new MuseIcon(WC_ICON_PATH, 36);
	public static final MuseIcon LVCAPACITOR = new MuseIcon(WC_ICON_PATH, 37);
	public static final MuseIcon PARACHUTE = new MuseIcon(WC_ICON_PATH, 38);
	public static final MuseIcon AQUA_AFFINITY = new MuseIcon(WC_ICON_PATH, 42);
	public static final MuseIcon CLAW_LASER = new MuseIcon(WC_ICON_PATH, 43);
	public static final MuseIcon WEAPON_SOUND = new MuseIcon(WC_ICON_PATH, 44);
	public static final MuseIcon BATTERY2 = new MuseIcon(WC_ICON_PATH, 46);
	public static final MuseIcon LAMP = new MuseIcon(WC_ICON_PATH, 47);
	public static final MuseIcon MYOFIBER_PASTE = new MuseIcon(WC_ICON_PATH, 48);
	public static final MuseIcon CARBON_MYOFIBER = new MuseIcon(WC_ICON_PATH, 49);
	public static final MuseIcon ARTIFICIAL_MUSCLE = new MuseIcon(WC_ICON_PATH, 50);
	public static final MuseIcon CRYSTAL_BUBBLE = new MuseIcon(WC_ICON_PATH, 51);
	public static final MuseIcon COOLING_SYSTEM = new MuseIcon(WC_ICON_PATH, 52);
	public static final MuseIcon MVCAPACITOR = new MuseIcon(WC_ICON_PATH, 53);
	public static final MuseIcon SCANNER = new MuseIcon(WC_ICON_PATH, 54);
	public static final MuseIcon POWERTOOL = new MuseIcon(WC_ICON_PATH, 59);
	public static final MuseIcon WEAPON_GRAVITY = new MuseIcon(WC_ICON_PATH, 60);
	public static final MuseIcon BATTERYCRYSTAL = new MuseIcon(WC_ICON_PATH, 62);
	public static final MuseIcon FLOATING_CRYSTAL = new MuseIcon(WC_ICON_PATH, 63);
	public static final MuseIcon MODULE_IRON_PLATING = new MuseIcon(WC_ICON_PATH, 64);
	public static final MuseIcon MODULE_DIAMOND_PLATING = new MuseIcon(WC_ICON_PATH, 65);
	public static final MuseIcon MODULE_TUNGSTEN_PLATING = new MuseIcon(WC_ICON_PATH, 66);
	public static final MuseIcon FIELD_GENERATOR = new MuseIcon(WC_ICON_PATH, 67);
	public static final MuseIcon HVCAPACITOR = new MuseIcon(WC_ICON_PATH, 69);
	public static final MuseIcon HOLOGRAM_EMITTER = new MuseIcon(WC_ICON_PATH, 70);
	public static final MuseIcon LASER = new MuseIcon(WC_ICON_PATH, 71);
	public static final MuseIcon ALIEN = new MuseIcon(WC_ICON_PATH, 78);
	public static final MuseIcon NETHERSTAR = new MuseIcon(WC_ICON_PATH, 94);
	public static final MuseIcon ARCREACTOR = new MuseIcon(WC_ICON_PATH, 110);
	public static final MuseIcon PUNCHY = new MuseIcon(WC_ICON_PATH, 233);

	String texturefile;
	int index;

	public MuseIcon(String texturefile, int index) {
		super();
		this.texturefile = texturefile;
		this.index = index;
	}

	public String getTexturefile() {
		return texturefile;
	}

	public void setTexturefile(String texturefile) {
		this.texturefile = texturefile;
	}

	public int getIconIndex() {
		return index;
	}

	public void setIconIndex(int index) {
		this.index = index;
	}

}
