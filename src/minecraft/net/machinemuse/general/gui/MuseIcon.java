package net.machinemuse.general.gui;

import net.machinemuse.powersuits.common.Config;

public class MuseIcon {
	public static final String SEBK_ICON_PATH = Config.SEBK_ICON_PATH;
	public static final String WC_ICON_PATH = Config.WC_ICON_PATH;
	public static final String MUSE_ICON_PATH = Config.MUSE_ICON_PATH;
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
	public static final MuseIcon IRON_PLATING = new MuseIcon(WC_ICON_PATH, 7);
	public static final MuseIcon DIAMOND_PLATING = new MuseIcon(WC_ICON_PATH, 8);
	public static final MuseIcon HEART = new MuseIcon(WC_ICON_PATH, 13);
	public static final MuseIcon BATTERY_FULL = new MuseIcon(WC_ICON_PATH, 14);
	public static final MuseIcon BATTERY_EMPTY = new MuseIcon(WC_ICON_PATH, 15);
	public static final MuseIcon SHOCK_ABSORBER = new MuseIcon(WC_ICON_PATH, 16);
	public static final MuseIcon PARACHUTE_MODULE = new MuseIcon(WC_ICON_PATH, 18);
	public static final MuseIcon TRANSPARENT_ARMOR = new MuseIcon(WC_ICON_PATH, 20);
	public static final MuseIcon GO_FAST = new MuseIcon(WC_ICON_PATH, 21);

	// Component icons
	public static final MuseIcon COMPONENT_WIRING = new MuseIcon(WC_ICON_PATH, 32);
	public static final MuseIcon COMPONENT_SOLENOID = new MuseIcon(WC_ICON_PATH, 33);
	public static final MuseIcon COMPONENT_GLIDERWING = new MuseIcon(WC_ICON_PATH, 35);
	public static final MuseIcon COMPONENT_SERVOMOTOR = new MuseIcon(WC_ICON_PATH, 34);
	public static final MuseIcon COMPONENT_IONTHRUSTER = new MuseIcon(WC_ICON_PATH, 36);
	public static final MuseIcon COMPONENT_LVCAPACITOR = new MuseIcon(WC_ICON_PATH, 37);
	public static final MuseIcon COMPONENT_PARACHUTE = new MuseIcon(WC_ICON_PATH, 38);
	public static final MuseIcon COMPONENT_MVCAPACITOR = new MuseIcon(WC_ICON_PATH, 53);
	public static final MuseIcon COMPONENT_HVCAPACITOR = new MuseIcon(WC_ICON_PATH, 69);

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
