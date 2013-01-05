package net.machinemuse.general.gui;

public class MuseIcon {
	public static final String ICON_PATH = "/icons.png";
	public static final MuseIcon ORB_1_GREEN = new MuseIcon(ICON_PATH, 0);
	public static final MuseIcon ORB_1_RED = new MuseIcon(ICON_PATH, 1);
	public static final MuseIcon ORB_1_BLUE = new MuseIcon(ICON_PATH, 2);
	public static final MuseIcon PLATE_1_GREEN = new MuseIcon(ICON_PATH, 3);
	public static final MuseIcon PLATE_1_RED = new MuseIcon(ICON_PATH, 4);
	public static final MuseIcon PLATE_1_BLUE = new MuseIcon(ICON_PATH, 5);
	public static final MuseIcon NEXUS_1_GREEN = new MuseIcon(ICON_PATH, 6);
	public static final MuseIcon NEXUS_1_RED = new MuseIcon(ICON_PATH, 7);
	public static final MuseIcon NEXUS_1_BLUE = new MuseIcon(ICON_PATH, 8);
	public static final MuseIcon TOOL_AXE = new MuseIcon(ICON_PATH, 9);
	public static final MuseIcon TOOL_PICK = new MuseIcon(ICON_PATH, 10);
	public static final MuseIcon TOOL_SHOVEL = new MuseIcon(ICON_PATH, 11);
	public static final MuseIcon TOOL_SHEARS = new MuseIcon(ICON_PATH, 12);
	public static final MuseIcon TOOL_FIST = new MuseIcon(ICON_PATH, 13);
	public static final MuseIcon TOOL_PINCH = new MuseIcon(ICON_PATH, 14);
	public static final MuseIcon ARMOR_HEAD = new MuseIcon(ICON_PATH, 15);
	public static final MuseIcon PLATE_2_GREEN = new MuseIcon(ICON_PATH, 16);
	public static final MuseIcon PLATE_2_RED = new MuseIcon(ICON_PATH, 17);
	public static final MuseIcon PLATE_2_BLUE = new MuseIcon(ICON_PATH, 18);
	public static final MuseIcon INDICATOR_1_BLUE = new MuseIcon(ICON_PATH, 19);
	public static final MuseIcon INDICATOR_1_RED = new MuseIcon(ICON_PATH, 20);
	public static final MuseIcon INDICATOR_1_GREEN = new MuseIcon(ICON_PATH, 21);
	public static final MuseIcon ARMOR_TORSO = new MuseIcon(ICON_PATH, 31);
	public static final MuseIcon ARMOR_LEGS = new MuseIcon(ICON_PATH, 47);
	public static final MuseIcon ARMOR_FEET = new MuseIcon(ICON_PATH, 63);

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
